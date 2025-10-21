package com.project.machine.controller;

import com.project.machine.Bean.MachineFilesBean;
import com.project.machine.Service.files.MachineFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "機台檔案管理", description = "處理機台相關檔案的相關操作")
@RequestMapping("/api/files")
public class FilesController {

	@Autowired
	private MachineFilesService machineFilesService;

	@Value("${file.upload-dir}")
	private String uploadBaseDir; // 從設定檔讀取路徑

	@Operation(summary = "查詢機台檔案", description = "根據關鍵字或機台ID查詢機台檔案")
	@GetMapping
	// GET http://localhost:8080/api/files
	public ResponseEntity<?> getFiles(@Parameter(description = "檔案名稱或機台名稱的關鍵字", required = false) @RequestParam(required = false) String keyword,
			@Parameter(description = "機台ID", required = false) @RequestParam(required = false) Integer machineId) {
		try {
			List<MachineFilesBean> files;
			if ((keyword != null && !keyword.trim().isEmpty()) || machineId != null) {
				if (machineId != null) {
					files = machineFilesService.getFilesByMachineId(machineId);
				} else {
					files = machineFilesService.searchFiles(keyword);
				}
			} else {
				files = machineFilesService.getFilesWithMachineInfo();
			}
			return ResponseEntity.ok(files);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("檔案查詢失敗：" + e.getMessage());
		}
	}

	@Operation(summary = "根據ID查詢檔案", description = "根據檔案ID查詢單一檔案的詳細資訊")
	@GetMapping("/{id}")
	// GET http://localhost:8080/api/files/5
	public ResponseEntity<?> getFileById(@Parameter(description = "檔案ID", required = true) @PathVariable int id) {
		try {
			MachineFilesBean file = machineFilesService.getFileById(id);
			if (file != null) {
				return ResponseEntity.ok(file);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到該檔案");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("錯誤：" + e.getMessage());
		}
	}

	@Operation(summary = "上傳新檔案", description = "上傳一個新的機台相關檔案")
	@PostMapping
	// POST http://localhost:8080/api/files
	/*
	 * fileName Text 檔案顯示名稱（例如：測試文件） machineId Text 2（或其他機台ID） fileUpload File
	 * 上傳一個檔案（點選選擇檔案）
	 */
	public ResponseEntity<?> insertFile(@Parameter(description = "檔案顯示名稱", required = true) @RequestParam String fileName, @Parameter(description = "機台ID", required = true) @RequestParam int machineId,
			@Parameter(description = "要上傳的檔案", required = true) @RequestParam("fileUpload") MultipartFile file) {

		try {
			if (file.isEmpty() || fileName.trim().isEmpty() || machineId <= 0) {
				return ResponseEntity.badRequest().body("請填寫完整資訊並選擇檔案");
			}
//因為file.upload-dir=D:/websideimage/設定在application.properties
// 上船路徑會變D:/websideimage/machine-files/machine-3/xxx_檔案名稱.副檔名

			Path uploadPath = Paths.get(uploadBaseDir, "machine-files", "machine-" + machineId);

			File directory = uploadPath.toFile();
			if (!directory.exists()) {
				directory.mkdirs();
			}

			String originalFileName = file.getOriginalFilename();
			String savedFileName = System.currentTimeMillis() + "_" + originalFileName;

			Path fullFilePath = uploadPath.resolve(savedFileName);

// 儲存檔案
			file.transferTo(fullFilePath.toFile());

			MachineFilesBean fileBean = new MachineFilesBean();
			fileBean.setFileName(fileName);
			fileBean.setFilePath(fullFilePath.toString()); // 儲存絕對路徑字串
			fileBean.setMachineId(machineId);
			fileBean.setUploadTime(LocalDateTime.now());

			boolean success = machineFilesService.addFile(fileBean);
			return success ? ResponseEntity.ok("檔案上傳成功")
					: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("資料儲存失敗");

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("檔案儲存錯誤：" + e.getMessage());
		}
	}

	@Operation(summary = "更新檔案", description = "根據檔案ID更新檔案資訊，可選擇重新上傳檔案")
	@PutMapping("/{id}")
	// PUT http://localhost:8080/api/files
	/*
	 * fileName Text 檔案顯示名稱（例如：測試文件） machineId Text 2（或其他機台ID） fileUpload File
	 * 上傳一個檔案（點選選擇檔案）
	 */
	public ResponseEntity<?> updateFile(@Parameter(description = "檔案ID", required = true) @PathVariable int id, @Parameter(description = "檔案顯示名稱", required = true) @RequestParam String fileName,
			@Parameter(description = "機台ID", required = true) @RequestParam int machineId, @Parameter(description = "要上傳的新檔案 (可選)", required = false) @RequestParam(value = "fileUpload", required = false) MultipartFile file) {

		try {
			MachineFilesBean original = machineFilesService.getFileById(id);
			if (original == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到檔案");
			}

			String filePath = original.getFilePath();

			if (file != null && !file.isEmpty()) {
				// 正確組合絕對路徑
				Path uploadPath = Paths.get(uploadBaseDir, "machine-files", "machine-" + machineId);
				File directory = uploadPath.toFile();
				if (!directory.exists()) {
					directory.mkdirs();
				}

				// 產生檔案名稱與儲存位置
				String originalFileName = file.getOriginalFilename();
				String savedFileName = System.currentTimeMillis() + "_" + originalFileName;
				Path fullFilePath = uploadPath.resolve(savedFileName);

				// 儲存新檔案
				file.transferTo(fullFilePath.toFile());

				// 刪除舊檔案（如果存在）
				File oldFile = new File(original.getFilePath());
				if (oldFile.exists()) {
					oldFile.delete();
				}

				filePath = fullFilePath.toString(); // 更新路徑
			}

			boolean updated = machineFilesService.updateFile(id, fileName, filePath, machineId);
			return updated ? ResponseEntity.ok("檔案更新成功")
					: ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失敗");

		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("檔案儲存錯誤：" + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("錯誤：" + e.getMessage());
		}
	}

	@Operation(summary = "刪除檔案", description = "根據檔案ID刪除指定的檔案")
	@DeleteMapping("/{id}")
	// DELETE http://localhost:8080/api/files/5
	public ResponseEntity<?> deleteFile(@Parameter(description = "要刪除的檔案ID", required = true) @PathVariable int id) {
		try {
			boolean deleted = machineFilesService.deleteFile(id);
			return deleted ? ResponseEntity.ok("檔案刪除成功")
					: ResponseEntity.status(HttpStatus.NOT_FOUND).body("找不到檔案或刪除失敗");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("刪除失敗：" + e.getMessage());
		}
	}
}