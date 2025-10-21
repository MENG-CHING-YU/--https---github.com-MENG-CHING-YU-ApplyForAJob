package com.project.machine.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.machine.Bean.MachineRepairBean;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachineRepairRepository extends JpaRepository<MachineRepairBean, Integer> {
    
    // 依機台ID查詢維修記錄
    @Query("FROM MachineRepairBean r LEFT JOIN FETCH r.machine WHERE r.machineId = :machineId ORDER BY r.repairTime DESC")
    List<MachineRepairBean> findByMachineIdWithMachineInfo(@Param("machineId") int machineId);
    
    // 依狀態查詢維修記錄
    @Query("FROM MachineRepairBean r LEFT JOIN FETCH r.machine WHERE r.repairStatus = :status ORDER BY r.repairTime DESC")
    List<MachineRepairBean> findByRepairStatusWithMachineInfo(@Param("status") String status);
    
    // 取得所有維修記錄並包含機台資訊
    @Query("FROM MachineRepairBean r LEFT JOIN FETCH r.machine ORDER BY r.repairTime DESC")
    List<MachineRepairBean> findAllWithMachineInfo();
    
    // 依ID查詢單筆維修記錄並包含機台資訊
    @Query("FROM MachineRepairBean r LEFT JOIN FETCH r.machine WHERE r.repairId = :repairId")
    Optional<MachineRepairBean> findByIdWithMachineInfo(@Param("repairId") int repairId);
    
    // 更新維修狀態
    @Modifying
    @Transactional
    @Query("UPDATE MachineRepairBean SET repairStatus = :status WHERE repairId = :repairId")
    int updateRepairStatus(@Param("repairId") int repairId, @Param("status") String status);
    
    // 依員工ID查詢維修記錄
    @Query("FROM MachineRepairBean r LEFT JOIN FETCH r.machine WHERE r.employeeId = :employeeId ORDER BY r.repairTime DESC")
    List<MachineRepairBean> findByEmployeeIdWithMachineInfo(@Param("employeeId") int employeeId);
    
    // 多條件搜尋
    @Query("FROM MachineRepairBean r LEFT JOIN FETCH r.machine " +
           "WHERE (:machineId IS NULL OR r.machineId = :machineId) " +
           "AND (:employeeId IS NULL OR r.employeeId = :employeeId) " +
           "AND (:status IS NULL OR :status = '' OR r.repairStatus = :status) " +
           "ORDER BY r.repairTime DESC")
    List<MachineRepairBean> findByConditions(@Param("machineId") Integer machineId,
                                           @Param("employeeId") Integer employeeId,
                                           @Param("status") String status);
}