package com.project.machine.controller;

import com.project.machine.Bean.WorkOderFinishBean;
import com.project.machine.Service.workOrderFinish.WorkOrderFinishService;
import com.project.machine.Service.workOrderFinish.WorkOrderFinishService.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workorderfinish")
public class WorkOrderFinishController {

    private final WorkOrderFinishService service;

    public WorkOrderFinishController(WorkOrderFinishService service) {
        this.service = service;
    }

    // 新增生產回報
    @PostMapping
    public ResponseEntity<?> createReport(@RequestBody WorkOderFinishBean report) {
        try {
            WorkOderFinishBean created = service.createReport(report);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException | ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // 根據 reportId 查詢回報
    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReportById(@PathVariable int reportId) {
        try {
            WorkOderFinishBean report = service.getReportById(reportId);
            return ResponseEntity.ok(report);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/workorder/{woId}")
    public ResponseEntity<?> getReportsByWorkOrderId(@PathVariable Long woId) {
        try {
            List<WorkOderFinishBean> reports = service.getReportsByWorkOrderId(woId);
            if (reports == null || reports.isEmpty()) {
                return ResponseEntity.ok(List.of()); // 回傳空陣列
            }
            return ResponseEntity.ok(reports);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error: " + ex.getMessage()));
        }
    }

    // 修改回報 (依 reportId)
    @PutMapping("/{reportId}")
    public ResponseEntity<?> updateReport(@PathVariable int reportId, @RequestBody WorkOderFinishBean updateReport) {
        try {
            WorkOderFinishBean updated = service.updateReport(reportId, updateReport);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException | ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // 刪除回報 (依 reportId)
    @DeleteMapping("/{reportId}")
    public ResponseEntity<?> deleteReport(@PathVariable int reportId) {
        try {
            service.deleteReport(reportId);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    // 獲取所有生產回報
    @GetMapping
    public ResponseEntity<List<WorkOderFinishBean>> getAllReports() {
        List<WorkOderFinishBean> reports = service.getAllReports();
        return ResponseEntity.ok(reports);
    }
}
