package com.project.machine.Service.workOrderFinish;

import com.project.depot.model.InventoryTransaction;
import com.project.depot.model.Material;
import com.project.machine.Bean.WorkOderFinishBean;
import com.project.machine.Repository.WorkOrderFinishRepository;
import com.project.workorder.dao.WorkOrderRepository;
import com.project.workorder.model.WorkOrder;
import com.project.depot.dao.InventoryTransactionRepository;
import com.project.depot.dao.MaterialRepository;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkOrderFinishService {

    private final WorkOrderFinishRepository finishRepository;
    private final WorkOrderRepository workOrderRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final MaterialRepository materialRepository;

    public WorkOrderFinishService(
            WorkOrderFinishRepository finishRepository,
            WorkOrderRepository workOrderRepository,
            InventoryTransactionRepository inventoryTransactionRepository,
            MaterialRepository materialRepository) {
        this.finishRepository = finishRepository;
        this.workOrderRepository = workOrderRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.materialRepository = materialRepository;
    }

    /**
     * 新增生產回報前，先確認工單是否存在
     * 
     * @param report 要新增的回報物件
     * @return 新增成功的回報物件
     * @throws ResourceNotFoundException 工單不存在時拋出
     */
    public WorkOderFinishBean createReport(WorkOderFinishBean report) {
        if (report.getWoId() == null) {
            throw new IllegalArgumentException("工單資訊不可為空");
        }

        Long woId = report.getWoId();
        WorkOrder workOrder = workOrderRepository.findById(woId)
                .orElseThrow(() -> new ResourceNotFoundException("工單不存在，無法新增回報，woId=" + woId));

        // 將查詢到的 WorkOrder 設置回 report bean 中，以便保存關聯
        WorkOderFinishBean newReport = new WorkOderFinishBean();
        newReport.setWoId(woId);
        newReport.setQuantityDone(report.getQuantityDone());
        newReport.setQuantityFailed(report.getQuantityFailed());

        if (report.getQuantityDone() > 0) {
            createInventoryTransactionForFinish(
                    workOrder.getMaterial().getMaterialId(),
                    report.getQuantityDone(),
                    workOrder.getWoId());
        }
        return finishRepository.save(newReport);
    }

    /**
     * 依回報ID查詢單筆回報
     * 
     * @param reportId 回報ID
     * @return 回報物件
     * @throws ResourceNotFoundException 找不到回報時拋出
     */
    public WorkOderFinishBean getReportById(int reportId) {
        return finishRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到回報紀錄，reportId=" + reportId));
    }

    /**
     * 依工單ID查詢該工單所有生產回報紀錄
     * 
     * @param woId 工單ID
     * @return 該工單所有回報列表
     */
    public List<WorkOderFinishBean> getReportsByWorkOrderId(Long woId) {
        return finishRepository.findByWoId(woId);
    }

    /**
     * 查詢所有生產回報紀錄
     * 
     * @return 所有回報列表
     */
    public List<WorkOderFinishBean> getAllReports() {
        return finishRepository.findAll();
    }

    /**
     * 修改回報，必須存在且工單也存在
     * 
     * @param reportId     回報ID
     * @param updateReport 更新的回報物件
     * @return 更新後的回報物件
     * @throws ResourceNotFoundException 找不到回報或工單不存在時拋出
     */
    public WorkOderFinishBean updateReport(int reportId, WorkOderFinishBean updateReport) {
        WorkOderFinishBean existing = finishRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到回報紀錄，reportId=" + reportId));

        if (updateReport.getWoId() == null) {
            throw new IllegalArgumentException("工單資訊不可為空");
        }

        Long woId = updateReport.getWoId();
        WorkOrder workOrder = workOrderRepository.findById(woId)
                .orElseThrow(() -> new ResourceNotFoundException("工單不存在，無法更新回報，woId=" + woId));

        existing.setQuantityDone(updateReport.getQuantityDone());
        existing.setQuantityFailed(updateReport.getQuantityFailed());
        existing.setWoId(woId);

        return finishRepository.save(existing);
    }

    /**
     * 刪除回報紀錄，依回報ID
     * 
     * @param reportId 回報ID
     * @throws ResourceNotFoundException 找不到回報時拋出
     */
    public void deleteReport(int reportId) {
        WorkOderFinishBean existing = finishRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("找不到回報紀錄，reportId=" + reportId));
        finishRepository.delete(existing);
    }

    /**
     * 自訂例外，表示找不到資源（工單或回報紀錄）
     */
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    public void createInventoryTransactionForFinish(Long materialId, int successQuantity, Long woId) {
        Material material = materialRepository.findById(materialId).orElseThrow();
        InventoryTransaction transaction = new InventoryTransaction();
        transaction.setMaterial(material); // 關聯物料物件
        transaction.setTransactionType("PRODUCTION_INBOUND"); // 生產入庫
        transaction.setQuantity(BigDecimal.valueOf(successQuantity)); // 用成功數量
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setReferenceTable("work_orders");
        transaction.setReferenceId(woId);
        inventoryTransactionRepository.save(transaction);

        // 更新成品庫存
        material.setStockCurrent(material.getStockCurrent().add(BigDecimal.valueOf(successQuantity)));
        materialRepository.save(material);
    }

}
