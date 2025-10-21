package com.project.machine.Service.machine;

import com.project.machine.Bean.MachinesBean;
import com.project.machine.Bean.StatusCodesBean;
import com.project.machine.Repository.MachinesRepository;
import com.project.machine.Repository.StatusCodesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MachinesService {

    @Autowired
    private MachinesRepository machinesRepository;

    @Autowired
    private StatusCodesRepository statusCodesRepository;

    // 查詢所有機台
    public List<MachinesBean> findAllMachines() {
        return machinesRepository.findAll();
    }

    // 依機台ID取得單筆資料
    public MachinesBean findMachineById(int machineId) {
        return machinesRepository.findById(machineId).orElse(null);
    }

    // 新增機台
    public void insertMachine(MachinesBean machine) {
        validateMachine(machine);

        // 檢查出廠編號是否已存在
        Optional<MachinesBean> existing = machinesRepository.findBySerialNumber(machine.getSerialNumber());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("出廠編號已存在，請勿重複新增");
        }

        // Set a default status for the new machine
        if (machine.getStatusCode() == null) {
            StatusCodesBean defaultStatus = statusCodesRepository.findById("STOP")
                    .orElseThrow(() -> new IllegalStateException("Default status 'STOP' not found in database"));
            machine.setStatusCode(defaultStatus);
        }

        machinesRepository.save(machine);
    }

    // 刪除機台
    public void deleteMachine(int machineId) {
        MachinesBean machine = machinesRepository.findById(machineId)
                .orElseThrow(() -> new IllegalArgumentException("找不到該機台資料，無法刪除"));

        // 只有停機狀態才能刪除
        if (machine.getStatusCode() == null || !"STOP".equals(machine.getStatusCode().getStatusCode())) {
            throw new IllegalStateException("僅停機狀態的機台才能刪除");
        }

        machinesRepository.deleteById(machineId);
    }

    // 更新機台
    @Transactional
    public void updateMachine(MachinesBean machine) {
        // 檢查機台是否存在
        MachinesBean existingMachine = machinesRepository.findById(machine.getMachineId())
                .orElseThrow(() -> new IllegalArgumentException("機台不存在"));

        // 檢查出廠編號是否重複（不包含自己）
        Optional<MachinesBean> existingSerialNumberMachine = machinesRepository.findBySerialNumber(machine.getSerialNumber());
        if (existingSerialNumberMachine.isPresent() && existingSerialNumberMachine.get().getMachineId() != machine.getMachineId()) {
            throw new IllegalArgumentException("出廠編號已存在，請勿重複");
        }

        // 更新機台資料，只更新非空的欄位
        if (machine.getMachineName() != null && !machine.getMachineName().trim().isEmpty()) {
            existingMachine.setMachineName(machine.getMachineName());
        }
        if (machine.getSerialNumber() != null && !machine.getSerialNumber().trim().isEmpty()) {
            existingMachine.setSerialNumber(machine.getSerialNumber());
        }
        if (machine.getMachineLocation() != null && !machine.getMachineLocation().trim().isEmpty()) {
            existingMachine.setMachineLocation(machine.getMachineLocation());
        }
        if (machine.getMachineModel() != null && !machine.getMachineModel().trim().isEmpty()) {
            existingMachine.setMachineModel(machine.getMachineModel());
        }
        if (machine.getMachineBrand() != null && !machine.getMachineBrand().trim().isEmpty()) {
            existingMachine.setMachineBrand(machine.getMachineBrand());
        }
        if (machine.getMachineManufacturer() != null && !machine.getMachineManufacturer().trim().isEmpty()) {
            existingMachine.setMachineManufacturer(machine.getMachineManufacturer());
        }
        if (machine.getMachinePurchaseDate() != null) {
            existingMachine.setMachinePurchaseDate(machine.getMachinePurchaseDate());
        }
        if (machine.getMachineServiceLife() != null) {
            existingMachine.setMachineServiceLife(machine.getMachineServiceLife());
        }
        if (machine.getMachineRemark() != null) {
            existingMachine.setMachineRemark(machine.getMachineRemark());
        }
        // 只有當新的 statusCode 不為 null 時才更新
        if (machine.getStatusCode() != null) {
            existingMachine.setStatusCode(machine.getStatusCode());
        }

        // 移除 validateMachine(machine); 因為現在是部分更新，且部分欄位可能為空
        // 如果需要對更新後的整體資料進行驗證，可以在這裡添加新的驗證邏輯
        machinesRepository.save(existingMachine);
    }

    // 依狀態查詢機台
    public List<MachinesBean> findMachinesByStatus(String status) {
        return machinesRepository.findByStatusCode_StatusCode(status);
    }

    // 依出廠編號查詢機台
    public MachinesBean findMachineBySerialNumber(String serialNumber) {
        return machinesRepository.findBySerialNumber(serialNumber).orElse(null);
    }

    // 模糊搜尋機台
    public List<MachinesBean> searchMachines(String keyword) {
        return machinesRepository.findByMachineNameContainingOrSerialNumberContainingOrderByMachineId(keyword);
    }

    // 驗證機台資料
    private void validateMachine(MachinesBean machine) {
        if (machine.getMachineName() == null || machine.getMachineName().trim().isEmpty()) {
            throw new IllegalArgumentException("機台名稱不可為空");
        }
        if (machine.getSerialNumber() == null || machine.getSerialNumber().trim().isEmpty()) {
            throw new IllegalArgumentException("出廠編號不可為空");
        }
    }

    public void updateMachineStatus(int id, String newStatus) {
        MachinesBean machine = machinesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("找不到該機台資料"));

        StatusCodesBean status = statusCodesRepository.findById(newStatus)
                .orElseThrow(() -> new EntityNotFoundException("找不到該狀態"));

        machine.setStatusCode(status);
        machinesRepository.save(machine);
    }
}
