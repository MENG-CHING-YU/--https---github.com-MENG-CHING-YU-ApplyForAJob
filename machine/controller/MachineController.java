package com.project.machine.controller;

import com.project.machine.Bean.MachinesBean;
import com.project.machine.Service.machine.MachinesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "機台管理", description = "處理機台資訊的相關操作")
@RequestMapping("/api/machines")
public class MachineController {

	@Autowired
	private MachinesService machinesService;

	// 查詢所有機台與篩選機台
	// GET全部http://localhost:8080/api/machines
	// GET篩選網址http://localhost:8080/api/machines?search=焊接&statusFilter=維修中
	@GetMapping
	public ResponseEntity<List<MachinesBean>> getAllMachines(
			// 模糊搜尋參數(前端會是text一格)
			@RequestParam(required = false) String search,
			// 搜尋狀態參數(前端會是select一格)
			@RequestParam(required = false) String statusFilter) {

		List<MachinesBean> machinesList;
        if (statusFilter != null && !statusFilter.isEmpty()) {
            machinesList = machinesService.findMachinesByStatus(statusFilter);
        } else {
            machinesList = machinesService.findAllMachines();
        }

		if (search != null && !search.trim().isEmpty()) {
			String keyword = search.trim().toLowerCase();
			List<MachinesBean> filteredList = new ArrayList<>();
			for (MachinesBean m : machinesList) {
				if (m.getMachineName().toLowerCase().contains(keyword)
						|| m.getSerialNumber().toLowerCase().contains(keyword)
						|| String.valueOf(m.getMachineId()).contains(keyword)) {
					filteredList.add(m);
				}
			}
			machinesList = filteredList;
		}

		return ResponseEntity.ok(machinesList);
	}

	// 查詢單筆機台詳細資料
	// GET網址http://localhost:8080/api/machines/3
	@GetMapping("/{id}")
	public ResponseEntity<?> getMachineById(@PathVariable int id) {
		MachinesBean machine = machinesService.findMachineById(id);
		if (machine == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到該機台資料");
		}
		return ResponseEntity.ok(machine);
	}

	// 新增機台
	// POST網址http://localhost:8080/api/machines
	// Body JSON
	/*
	 * { "machineName": "自動焊接機 B2", "serialNumber": "SN10586", "mstatus": "維修中",
	 * "machineLocation": "一樓-B區" }
	 */
	@PostMapping
	public ResponseEntity<?> insertMachine(@RequestBody MachinesBean machine) {
		try {
			machinesService.insertMachine(machine);
			return ResponseEntity.status(HttpStatus.CREATED).body("新增成功");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系統錯誤");
		}
	}

	// 更新機台
	// PUT 網址http://localhost:8080/api/machines/3
	// Body JSON
	/*
	 * { "machineName": "雷射切割機 C3 (更新版)", "serialNumber": "SN10003", "mstatus":
	 * "運轉中", "machineLocation": "二樓-C區" }
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateMachine(@PathVariable int id, @RequestBody MachinesBean machine) {
		try {
			machine.setMachineId(id); // 確保ID一致
			machinesService.updateMachine(machine);
			return ResponseEntity.ok("更新成功");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系統錯誤");
		}
	}

	// 刪除機台
	// DELETE 網址http://localhost:8080/api/machines/8
	@Operation(summary = "刪除機台", description = "根據機台ID刪除指定的機台記錄")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteMachine(@Parameter(description = "要刪除的機台ID", required = true) @PathVariable int id) {
		try {
			machinesService.deleteMachine(id);
			return ResponseEntity.ok("刪除成功");
		} catch (IllegalArgumentException | IllegalStateException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("系統錯誤");
		}
	}

	// 取得所有機台狀態（不重複）
	@GetMapping("/status-options")
	public ResponseEntity<List<String>> getAllStatusOptions() {
		List<MachinesBean> machinesList = machinesService.findAllMachines();
		List<String> statusList = new ArrayList<>();
		for (MachinesBean m : machinesList) {
			if (m.getStatusCode() != null && m.getStatusCode().getStatusCode() != null && !statusList.contains(m.getStatusCode().getStatusCode())) {
				statusList.add(m.getStatusCode().getStatusCode());
			}
		}
		return ResponseEntity.ok(statusList);
	}

	// 更新機台狀態
	// PUT 網址http://localhost:8080/api/machines/3/status?newStatus=運轉中
	@PutMapping("/{id}/status")
	public ResponseEntity<?> updateMachineStatus(@PathVariable int id, @RequestParam String newStatus) {
		try {
			// 檢查新狀態是否為空
			if (newStatus == null || newStatus.trim().isEmpty()) {
				return ResponseEntity.badRequest().body("狀態不可為空");
			}

			machinesService.updateMachineStatus(id, newStatus);

			return ResponseEntity.ok("機台狀態更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失敗：" + e.getMessage());
		}
	}
}
