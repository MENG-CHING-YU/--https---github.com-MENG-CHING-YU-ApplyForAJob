package com.project.machine.Repository;

import com.project.machine.Bean.TaskAssignments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskAssignmentsRepository extends JpaRepository<TaskAssignments, Integer> {
    // 查詢某員工的所有派工
    List<TaskAssignments> findByEmployeeId(Integer employeeId);

    List<TaskAssignments> findByTaskTypeAndTaskId(String taskType, Integer taskId);
}
