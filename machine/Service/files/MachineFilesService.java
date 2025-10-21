package com.project.machine.Service.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.machine.Bean.MachineFilesBean;
import com.project.machine.Repository.MachineFilesRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MachineFilesService {

    @Autowired
    private MachineFilesRepository machineFilesRepository;

    // 取得所有檔案
    public List<MachineFilesBean> getAllFiles() {
        return machineFilesRepository.findAll();
    }

    // 依ID取得檔案
    public MachineFilesBean getFileById(int fileId) {
        return machineFilesRepository.findById(fileId).orElse(null);
    }

    // 依機台ID取得檔案
    public List<MachineFilesBean> getFilesByMachineId(int machineId) {
        return machineFilesRepository.findByMachineIdOrderByUploadTimeDesc(machineId);
    }

    // 搜尋檔案
    public List<MachineFilesBean> searchFiles(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllFiles();
        }
        return machineFilesRepository.searchByKeyword(keyword.trim());
    }

    // 取得檔案並包含機台資訊
    public List<MachineFilesBean> getFilesWithMachineInfo() {
        return machineFilesRepository.findAllWithMachineInfo();
    }

    // 新增檔案
    public boolean addFile(MachineFilesBean file) {
        try {
            validateFile(file);
            
            if (file.getUploadTime() == null) {
                file.setUploadTime(LocalDateTime.now());
            }
            
            machineFilesRepository.save(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新檔案
    public boolean updateFile(MachineFilesBean file) {
        try {
            validateFile(file);
            
            if (!machineFilesRepository.existsById(file.getFileId())) {
                return false;
            }
            
            // 保留原上傳時間
            MachineFilesBean originalFile = machineFilesRepository.findById(file.getFileId()).orElse(null);
            if (originalFile != null) {
                file.setUploadTime(originalFile.getUploadTime());
            }
            
            machineFilesRepository.save(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新檔案（透過參數）
    public boolean updateFile(int fileId, String fileName, String filePath, int machineId) {
        try {
            MachineFilesBean originalFile = getFileById(fileId);
            if (originalFile == null) {
                return false;
            }

            MachineFilesBean file = new MachineFilesBean();
            file.setFileId(fileId);
            file.setFileName(fileName);
            file.setFilePath(filePath);
            file.setMachineId(machineId);
            file.setUploadTime(originalFile.getUploadTime());

            return updateFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 刪除檔案
    public boolean deleteFile(int fileId) {
        try {
            if (!machineFilesRepository.existsById(fileId)) {
                return false;
            }
            
            machineFilesRepository.deleteById(fileId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 驗證檔案資料
    private void validateFile(MachineFilesBean file) {
        if (file.getFileName() == null || file.getFileName().trim().isEmpty()) {
            throw new IllegalArgumentException("檔案名稱不可為空");
        }
        if (file.getFilePath() == null || file.getFilePath().trim().isEmpty()) {
            throw new IllegalArgumentException("檔案路徑不可為空");
        }
        if (file.getMachineId() <= 0) {
            throw new IllegalArgumentException("機台ID必須為正整數");
        }
    }
}