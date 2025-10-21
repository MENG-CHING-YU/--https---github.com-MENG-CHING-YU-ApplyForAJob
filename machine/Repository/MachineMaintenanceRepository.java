package com.project.machine.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.machine.Bean.MachineMaintenanceBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MachineMaintenanceRepository extends JpaRepository<MachineMaintenanceBean, Integer> {

       // 輕量版：取得所有保養記錄，不帶 machine 資訊
       @Query("FROM MachineMaintenanceBean m ORDER BY m.scheduleId")
       List<MachineMaintenanceBean> findAllSimple();

       // 詳細版：取得所有保養記錄並包含機台資訊
       @Query("FROM MachineMaintenanceBean m LEFT JOIN FETCH m.machine ORDER BY m.scheduleId")
       List<MachineMaintenanceBean> findAllWithMachineInfo();

       // 依機台ID查詢保養記錄（帶 machine）
       @Query("FROM MachineMaintenanceBean m LEFT JOIN FETCH m.machine WHERE m.machineId = :machineId ORDER BY m.scheduleDate DESC")
       List<MachineMaintenanceBean> findByMachineIdWithMachineInfo(@Param("machineId") int machineId);

       // 依狀態查詢保養記錄（帶 machine）
       @Query("FROM MachineMaintenanceBean m LEFT JOIN FETCH m.machine WHERE m.maintenanceStatus = :status ORDER BY m.scheduleDate DESC")
       List<MachineMaintenanceBean> findByMaintenanceStatusWithMachineInfo(@Param("status") String status);

       // 依ID查詢單筆保養記錄並包含機台資訊
       @Query("FROM MachineMaintenanceBean m LEFT JOIN FETCH m.machine WHERE m.scheduleId = :scheduleId")
       Optional<MachineMaintenanceBean> findByIdWithMachineInfo(@Param("scheduleId") int scheduleId);

       // 依員工ID查詢保養記錄（帶 machine）
       @Query("FROM MachineMaintenanceBean m LEFT JOIN FETCH m.machine WHERE m.employeeId = :employeeId ORDER BY m.scheduleDate DESC")
       List<MachineMaintenanceBean> findByEmployeeIdWithMachineInfo(@Param("employeeId") int employeeId);

       // 依日期範圍查詢保養記錄（帶 machine）
       @Query("FROM MachineMaintenanceBean m LEFT JOIN FETCH m.machine " +
                     "WHERE m.scheduleDate BETWEEN :startDate AND :endDate " +
                     "ORDER BY m.scheduleDate DESC")
       List<MachineMaintenanceBean> findByScheduleDateBetweenWithMachineInfo(
                     @Param("startDate") LocalDateTime startDate,
                     @Param("endDate") LocalDateTime endDate);

       // 查詢即將到期的保養（未來7天內）（帶 machine）
       @Query("FROM MachineMaintenanceBean m LEFT JOIN FETCH m.machine " +
                     "WHERE m.scheduleDate <= :endDate AND m.maintenanceStatus != 'MAINTAINED' " +
                     "ORDER BY m.scheduleDate ASC")
       List<MachineMaintenanceBean> findUpcomingMaintenance(@Param("endDate") LocalDateTime endDate);
}
