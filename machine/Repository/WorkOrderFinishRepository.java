package com.project.machine.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.machine.Bean.WorkOderFinishBean;

import java.util.List;

/**
 * 生產回報資料存取介面
 * 提供對 WorkOderFinishBean 的 CRUD 操作
 */
@Repository
public interface WorkOrderFinishRepository extends JpaRepository<WorkOderFinishBean, Integer> {

    /**
     * 根據工單ID查詢該工單所有生產回報紀錄
     * 
     * @param woId 工單ID
     * @return 回報紀錄清單
     */
    List<WorkOderFinishBean> findByWoId(Long woId);

}
