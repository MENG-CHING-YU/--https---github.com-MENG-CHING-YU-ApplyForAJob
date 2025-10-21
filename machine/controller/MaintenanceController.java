package com.project.machine.controller;

import com.project.machine.Bean.MachineMaintenanceBean;
import com.project.machine.Service.maintenance.MachineMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

    @Autowired
    private MachineMaintenanceService maintenanceService;

    // 查詢所有保養(含機台詳細)
    // GET http://localhost:8080/api/maintenance
    @GetMapping
    public ResponseEntity<?> findAllMaintenance() {
        try {
            List<MachineMaintenanceBean> list = maintenanceService.findAllMaintenances();
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("載入保養資料失敗：" + e.getMessage());
        }
    }

    // 查詢單筆保養（詳細資料，含機台資訊）
    // GET http://localhost:8080/api/maintenance/3
    @GetMapping("/{id}")
    public ResponseEntity<?> getMaintenanceDetail(@PathVariable int id) {
        try {
            MachineMaintenanceBean maintenance = maintenanceService.findMaintenanceDetailById(id);
            if (maintenance != null) {
                return ResponseEntity.ok(maintenance);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("查無該筆保養資料");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("錯誤：" + e.getMessage());
        }
    }

    // 新增保養
    // POST http://localhost:8080/api/maintenance
    /*
     * Body JSON 範例:
     * {
     * "machineId": 5,
     * "maintenanceDescription": "更換濾網",
     * "employeeId": 101
     * }
     * 注意: maintenanceStatus 和 scheduleDate 由後端預設
     */
    @PostMapping
    public ResponseEntity<?> insertMaintenance(@RequestBody MachineMaintenanceBean request) {
        try {
            request.setMaintenanceStatus("WAIT_MAINTENANCE");
            MachineMaintenanceBean saved = maintenanceService.saveAndReturn(request);
            // 這裡要回傳 scheduleId
            return ResponseEntity.ok(Map.of("scheduleId", saved.getScheduleId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("新增失敗：" + e.getMessage());
        }
    }

    // 更新保養
    // PUT http://localhost:8080/api/maintenance/{id}
    /*
     * Body JSON 範例:
     * {
     * "machineId": 5,
     * "maintenanceDescription": "已完成清潔",
     * "maintenanceStatus": "已完成",
     * "employeeId": 101,
     * "scheduleDate": "2025-07-29T15:30:00"
     * }
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMaintenance(@PathVariable int id, @RequestBody MachineMaintenanceBean request) {
        System.out.println("接收到的保養ID: " + id + ", 新狀態: " + request.getMaintenanceStatus());
        try {
            request.setScheduleId(id);
            boolean updated = maintenanceService.updateMaintenance(request);
            if (!updated) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到該筆保養資料");
            }
            return ResponseEntity.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新失敗：" + e.getMessage());
        }
    }

    // 更新保養狀態
    // PUT http://localhost:8080/api/maintenance/{id}/status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateMaintenanceStatus(@PathVariable int id, @RequestParam String newStatus) {
        System.out.println("接收到的保養ID: " + id + ", 新狀態: " + newStatus);
        maintenanceService.updateMaintenanceStatus(id, newStatus);
        return ResponseEntity.ok("保養狀態更新成功");
    }

    // 刪除保養
    // DELETE http://localhost:8080/api/maintenance/{id}
    @Operation(summary = "刪除保養記錄", description = "根據保養記錄ID刪除指定的保養記錄")
    @Parameter(name = "id", description = "保養記錄ID", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMaintenance(@PathVariable int id) {
        try {
            boolean deleted = maintenanceService.deleteMaintenance(id);
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到該筆保養資料");
            }
            return ResponseEntity.ok("刪除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("刪除失敗：" + e.getMessage());
        }
    }
}
