package com.project.machine.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.machine.Bean.MachinesBean;

import java.util.List;
import java.util.Optional;

@Repository
public interface MachinesRepository extends JpaRepository<MachinesBean, Integer> {

        // 依狀態查詢機器
        List<MachinesBean> findByStatusCode_StatusCode(String statusCode);

        // 依出廠編號查詢（唯一）
        Optional<MachinesBean> findBySerialNumber(String serialNumber);

        // 依出廠編號模糊查詢
        @Query("FROM MachinesBean WHERE serialNumber LIKE %:pattern% ORDER BY machineId")
        List<MachinesBean> findBySerialNumberContainingOrderByMachineId(@Param("pattern") String pattern);

        // 依機台名稱模糊查詢
        @Query("FROM MachinesBean WHERE machineName LIKE %:keyword% OR serialNumber LIKE %:keyword% ORDER BY machineId")
        List<MachinesBean> findByMachineNameContainingOrSerialNumberContainingOrderByMachineId(
                        @Param("keyword") String keyword);

        // 分頁查詢（可用於大資料量）
        @Query("FROM MachinesBean WHERE statusCode.statusCode = :status ORDER BY machineId")
        List<MachinesBean> findByMstatusWithPage(@Param("status") String status,
                        org.springframework.data.domain.Pageable pageable);
}