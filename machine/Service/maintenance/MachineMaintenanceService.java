package com.project.machine.Service.maintenance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.machine.Bean.MachineMaintenanceBean;
import com.project.machine.Repository.MachineMaintenanceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MachineMaintenanceService {

    @Autowired
    private MachineMaintenanceRepository maintenanceRepository;

    // 輕量版查詢全部保養紀錄，不帶 machine 物件
    public List<MachineMaintenanceBean> findAllMaintenancesSimple() {
        return maintenanceRepository.findAllSimple();
    }

    // 詳細版查詢全部保養紀錄，帶 machine 物件
    public List<MachineMaintenanceBean> findAllMaintenances() {
        return maintenanceRepository.findAllWithMachineInfo();
    }

    // 查詢所有保養記錄詳細資訊（與 findAllMaintenances 相同）
    public List<MachineMaintenanceBean> findAllMaintenancesDetail() {
        return maintenanceRepository.findAllWithMachineInfo();
    }

    // 根據ID查詢保養記錄詳細資訊
    public MachineMaintenanceBean findMaintenanceDetailById(int scheduleId) {
        Optional<MachineMaintenanceBean> maintenance = maintenanceRepository.findByIdWithMachineInfo(scheduleId);
        return maintenance.orElse(null);
    }

    // 新增保養記錄
    // 新增保養記錄（不自動填 scheduleDate）
    public boolean insertMaintenance(MachineMaintenanceBean maintenance) {
        try {
            validateMaintenance(maintenance); // 保留驗證邏輯
            // 不要覆蓋 scheduleDate
            maintenanceRepository.save(maintenance);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新保養記錄
    public boolean updateMaintenance(MachineMaintenanceBean maintenance) {
        try {
            validateMaintenance(maintenance);
            if (!maintenanceRepository.existsById(maintenance.getScheduleId())) {
                return false;
            }
            maintenanceRepository.save(maintenance);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 刪除保養記錄
    public boolean deleteMaintenance(int scheduleId) {
        try {
            if (!maintenanceRepository.existsById(scheduleId)) {
                return false;
            }
            maintenanceRepository.deleteById(scheduleId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 根據ID查詢保養記錄（不帶 machine）
    public MachineMaintenanceBean findMaintenanceById(int scheduleId) {
        return maintenanceRepository.findById(scheduleId).orElse(null);
    }

    // 根據機台ID查詢保養記錄，帶 machine
    public List<MachineMaintenanceBean> findMaintenanceByMachineId(int machineId) {
        return maintenanceRepository.findByMachineIdWithMachineInfo(machineId);
    }

    // 根據狀態查詢保養記錄，帶 machine
    public List<MachineMaintenanceBean> findMaintenanceByStatus(String status) {
        return maintenanceRepository.findByMaintenanceStatusWithMachineInfo(status);
    }

    // 根據員工ID查詢保養記錄，帶 machine
    public List<MachineMaintenanceBean> findMaintenanceByEmployeeId(int employeeId) {
        return maintenanceRepository.findByEmployeeIdWithMachineInfo(employeeId);
    }

    // 根據日期範圍查詢保養記錄，帶 machine
    public List<MachineMaintenanceBean> findMaintenanceByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return maintenanceRepository.findByScheduleDateBetweenWithMachineInfo(startDate, endDate);
    }

    // 查詢即將到期的保養記錄（未完成且七天內），帶 machine
    public List<MachineMaintenanceBean> findUpcomingMaintenance() {
        LocalDateTime endDate = LocalDateTime.now().plusDays(7);
        return maintenanceRepository.findUpcomingMaintenance(endDate);
    }

    // 驗證保養記錄資料
    private void validateMaintenance(MachineMaintenanceBean maintenance) {
        if (maintenance.getMachineId() <= 0) {
            throw new IllegalArgumentException("機台ID必須為正整數");
        }
        if (maintenance.getMaintenanceDescription() == null
                || maintenance.getMaintenanceDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("保養描述不可為空");
        }
        if (maintenance.getMaintenanceStatus() == null || maintenance.getMaintenanceStatus().trim().isEmpty()) {
            throw new IllegalArgumentException("保養狀態不可為空");
        }
        if (maintenance.getEmployeeId() == null || maintenance.getEmployeeId() <= 0) {
            throw new IllegalArgumentException("員工ID必須為正整數");
        }
        // 只用 code，不用 label
        if ("WAIT_MAINTENANCE".equals(maintenance.getMaintenanceStatus())) {
            // 待保養邏輯
        }
    }

    public MachineMaintenanceBean saveAndReturn(MachineMaintenanceBean maintenance) {
        validateMaintenance(maintenance);
        return maintenanceRepository.save(maintenance);
    }

    public boolean updateMaintenanceStatus(int id, String newStatus) {
        MachineMaintenanceBean maintenance = maintenanceRepository.findById(id).orElse(null);
        if (maintenance != null) {
            maintenance.setMaintenanceStatus(newStatus);
            maintenanceRepository.save(maintenance);
            return true;
        }
        return false;
    }
}
