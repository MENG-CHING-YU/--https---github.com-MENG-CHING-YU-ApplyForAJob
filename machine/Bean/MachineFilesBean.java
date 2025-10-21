package com.project.machine.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "machine_files")
public class MachineFilesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private int fileId;

    @Column(name = "file_name", length = 100)
    private String fileName;

    @Column(name = "file_path", length = 255)
    private String filePath;

    @Column(name = "machine_id")
    private int machineId;

    @Column(name = "upload_time", nullable = false)
    private LocalDateTime uploadTime;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", referencedColumnName = "machine_id", insertable = false, updatable = false)
    private MachinesBean machine;

    public MachineFilesBean() {
    }

    public MachineFilesBean(String fileName, String filePath, int machineId, LocalDateTime uploadTime) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.machineId = machineId;
        this.uploadTime = uploadTime;
    }

    public MachineFilesBean(int fileId, String fileName, String filePath, int machineId, LocalDateTime uploadTime) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.filePath = filePath;
        this.machineId = machineId;
        this.uploadTime = uploadTime;
    }

    // Getter & Setter

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public MachinesBean getMachine() {
        return machine;
    }

    public void setMachine(MachinesBean machine) {
        this.machine = machine;
    }
}
