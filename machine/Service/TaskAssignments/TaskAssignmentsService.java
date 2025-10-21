package com.project.machine.Service.TaskAssignments;

import com.project.machine.Bean.TaskAssignments;
import com.project.machine.Repository.TaskAssignmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskAssignmentsService {

    @Autowired
    private TaskAssignmentsRepository repository;

    // 新增派工（不驗證機台）
    public TaskAssignments createAssignment(TaskAssignments assignment) {
        if (assignment.getEmployeeId() == null) {
            throw new IllegalArgumentException("員工ID不可為空");
        }
        // 不驗證機台ID
        return repository.save(assignment);
    }

    // 查詢某員工的所有派工
    public List<TaskAssignments> getAssignmentsByEmployee(Integer employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    // 查詢某任務的所有派工（依 taskType + taskId）
    public List<TaskAssignments> getAssignmentsByTask(String taskType, Integer taskId) {
        return repository.findByTaskTypeAndTaskId(taskType, taskId);
    }

    // 查詢單筆派工
    public TaskAssignments getAssignmentById(Integer id) {
        Optional<TaskAssignments> result = repository.findById(id);
        if (result.isEmpty()) {
            throw new IllegalArgumentException("找不到該派工資料");
        }
        return result.get();
    }

    // 刪除派工
    public void deleteAssignment(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("找不到該派工資料");
        }
        repository.deleteById(id);
    }
}
