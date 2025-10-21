package com.project.machine.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "task_assignments")
@SuppressWarnings("serial")
public class TaskAssignments implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id") // ← 修正為 assignment_id
    private int assignmentId;

    @Column(name = "task_type", length = 100)
    private String taskType;
    @Column(name = "task_id")
    private int taskId;
    @Column(name = "employee_id", length = 255)
    private Integer employeeId;
    @Column(name = "assigned_time")
    private LocalDateTime assignedTime;

    public TaskAssignments() {
    }

    public TaskAssignments(String taskType, int taskId, Integer employeeId, LocalDateTime assignedTime) {
        this.taskType = taskType;
        this.taskId = taskId;
        this.employeeId = employeeId;
        this.assignedTime = assignedTime;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getAssignedTime() {
        return assignedTime;
    }

    public void setAssignedTime(LocalDateTime assignedTime) {
        this.assignedTime = assignedTime;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId) {
        this.assignmentId = assignmentId;
    }

}
