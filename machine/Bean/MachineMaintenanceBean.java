package com.project.machine.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "machine_maintenance")
public class MachineMaintenanceBean implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private int scheduleId;

    @Column(name = "machine_id")
    private int machineId;

    @Column(name = "schedule_date")
    private LocalDateTime scheduleDate;

    @Column(name = "maintenance_description", columnDefinition = "TEXT")
    private String maintenanceDescription;

    @Column(name = "maintenance_status", length = 20)
    private String maintenanceStatus; // 存 status_code，如 "SCHEDULED"

    @Column(name = "employee_id")
    private Integer employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id", referencedColumnName = "machine_id", insertable = false, updatable = false)
    private MachinesBean machine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maintenance_status", referencedColumnName = "status_code", insertable = false, updatable = false)
    @JsonIgnore // 避免序列化造成 500
    private StatusCodesBean statusCode;

    // 無參數建構子
    public MachineMaintenanceBean() {
    }

    // Getter 和 Setter
    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public LocalDateTime getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDateTime scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getMaintenanceDescription() {
        return maintenanceDescription;
    }

    public void setMaintenanceDescription(String maintenanceDescription) {
        this.maintenanceDescription = maintenanceDescription;
    }

    public String getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public MachinesBean getMachine() {
        return machine;
    }

    public void setMachine(MachinesBean machine) {
        this.machine = machine;
    }

    public StatusCodesBean getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCodesBean statusCode) {
        this.statusCode = statusCode;
    }

    // 新增一個 getter 提供中文 label 給前端
    public String getMaintenanceStatusLabel() {
        return statusCode != null ? statusCode.getStatusLabel() : maintenanceStatus;
    }

    // toString（排除 machine 和 statusCode 避免循環）
    @Override
    public String toString() {
        return "MachineMaintenanceBean{" +
                "scheduleId=" + scheduleId +
                ", machineId=" + machineId +
                ", scheduleDate=" + scheduleDate +
                ", maintenanceDescription='" + maintenanceDescription + '\'' +
                ", maintenanceStatus='" + maintenanceStatus + '\'' +
                ", employeeId=" + employeeId +
                '}';
    }

    // equals 和 hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MachineMaintenanceBean that = (MachineMaintenanceBean) o;
        return scheduleId == that.scheduleId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(scheduleId);
    }
}
