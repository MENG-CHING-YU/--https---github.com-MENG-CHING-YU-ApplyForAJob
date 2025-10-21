package com.project.machine.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.machine.Bean.MachineFilesBean;

import java.util.List;

@Repository
public interface MachineFilesRepository extends JpaRepository<MachineFilesBean, Integer> {
    
    // 依機台ID查詢檔案
    List<MachineFilesBean> findByMachineIdOrderByUploadTimeDesc(int machineId);
    
    // 關鍵字搜尋（檔案名稱或機台名稱）
    @Query("SELECT f FROM MachineFilesBean f LEFT JOIN f.machine m " +
           "WHERE f.fileName LIKE %:keyword% OR m.machineName LIKE %:keyword% " +
           "ORDER BY f.uploadTime DESC")
    List<MachineFilesBean> searchByKeyword(@Param("keyword") String keyword);
    
    // 取得所有檔案並包含機台資訊
    @Query("FROM MachineFilesBean f LEFT JOIN FETCH f.machine ORDER BY f.uploadTime DESC")
    List<MachineFilesBean> findAllWithMachineInfo();
    
    // 依檔案名稱查詢
    List<MachineFilesBean> findByFileNameContainingOrderByUploadTimeDesc(String fileName);
    
    // 依檔案路徑查詢
    List<MachineFilesBean> findByFilePathContainingOrderByUploadTimeDesc(String filePath);
}