package com.project.machine.controller;

import com.project.machine.Bean.TaskAssignments;
import com.project.machine.Service.TaskAssignments.TaskAssignmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-assignments")
public class TaskAssignmentsController {

    @Autowired
    private TaskAssignmentsService service;

    // 新增派工
    @PostMapping
    public ResponseEntity<TaskAssignments> createAssignment(@RequestBody TaskAssignments assignment) {
        TaskAssignments saved = service.createAssignment(assignment);
        return ResponseEntity.ok(saved);
    }

    // 查詢某員工的所有派工
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<TaskAssignments>> getAssignmentsByEmployee(@PathVariable Integer employeeId) {
        List<TaskAssignments> list = service.getAssignmentsByEmployee(employeeId);
        return ResponseEntity.ok(list);
    }

    // 查詢單筆派工
    @GetMapping("/{id}")
    public ResponseEntity<TaskAssignments> getAssignmentById(@PathVariable Integer id) {
        TaskAssignments assignment = service.getAssignmentById(id);
        return ResponseEntity.ok(assignment);
    }

    // 刪除派工
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssignment(@PathVariable Integer id) {
        service.deleteAssignment(id);
        return ResponseEntity.noContent().build();
    }

    // 查詢某任務的所有派工（依 taskType + taskId）
    @GetMapping("/search")
    public ResponseEntity<List<TaskAssignments>> getAssignmentsByTask(
            @RequestParam String taskType,
            @RequestParam Integer taskId) {
        List<TaskAssignments> list = service.getAssignmentsByTask(taskType, taskId);
        return ResponseEntity.ok(list);
    }
}
