package com.project.machine.Bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
@Table(name = "machines")
public class MachinesBean implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_id")
    private int machineId;

    @Column(name = "machine_name", length = 50)
    private String machineName;

    @Column(name = "serial_number", length = 50, unique = true)
    private String serialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mstatus", referencedColumnName = "status_code")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private StatusCodesBean statusCode;

    @Column(name = "machine_location", length = 50)
    private String machineLocation;

    @Column(name = "machine_model", length = 50)
    private String machineModel;

    @Column(name = "machine_brand", length = 50)
    private String machineBrand;

    @Column(name = "machine_manufacturer", length = 50)
    private String machineManufacturer;

    @Column(name = "machine_purchase_date")
    private java.time.LocalDate machinePurchaseDate;

    @Column(name = "machine_service_life")
    private Integer machineServiceLife;

    @Column(name = "machine_remark", length = 255)
    private String machineRemark;

    // 無參數建構子 - JPA 需要
    public MachinesBean() {
    }

    // 原有的建構子
    public MachinesBean(String machineName, String serialNumber, String machineLocation) {
        this.machineName = machineName;
        this.serialNumber = serialNumber;
        this.machineLocation = machineLocation;
    }

    public MachinesBean(int machineId, String machineName, String serialNumber, String machineLocation) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.serialNumber = serialNumber;
        this.machineLocation = machineLocation;
    }

    // Getter 和 Setter 方法
    public int getMachineId() {
        return machineId;
    }

    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMachineLocation() {
        return machineLocation;
    }

    public void setMachineLocation(String machineLocation) {
        this.machineLocation = machineLocation;
    }

    public String getMachineModel() {
        return machineModel;
    }

    public void setMachineModel(String machineModel) {
        this.machineModel = machineModel;
    }

    public String getMachineBrand() {
        return machineBrand;
    }

    public void setMachineBrand(String machineBrand) {
        this.machineBrand = machineBrand;
    }

    public String getMachineManufacturer() {
        return machineManufacturer;
    }

    public void setMachineManufacturer(String machineManufacturer) {
        this.machineManufacturer = machineManufacturer;
    }

    public java.time.LocalDate getMachinePurchaseDate() {
        return machinePurchaseDate;
    }

    public void setMachinePurchaseDate(java.time.LocalDate machinePurchaseDate) {
        this.machinePurchaseDate = machinePurchaseDate;
    }

    public Integer getMachineServiceLife() {
        return machineServiceLife;
    }

    public void setMachineServiceLife(Integer machineServiceLife) {
        this.machineServiceLife = machineServiceLife;
    }

    public String getMachineRemark() {
        return machineRemark;
    }

    public void setMachineRemark(String machineRemark) {
        this.machineRemark = machineRemark;
    }

    public StatusCodesBean getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(StatusCodesBean statusCode) {
        this.statusCode = statusCode;
    }

    // toString 方法（排除關聯集合以避免循環引用）
    @Override
    public String toString() {
        return "MachinesBean{" +
                "machineId=" + machineId +
                ", machineName='" + machineName + "'" +
                ", serialNumber='" + serialNumber + "'" +
                ", machineLocation='" + machineLocation + "'" +
                ", machineModel='" + machineModel + "'" +
                ", machineBrand='" + machineBrand + "'" +
                ", machineManufacturer='" + machineManufacturer + "'" +
                ", machinePurchaseDate=" + machinePurchaseDate +
                ", machineServiceLife=" + machineServiceLife +
                ", machineRemark='" + machineRemark + "'" +
                '}';
    }

    // equals 和 hashCode 方法
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MachinesBean that = (MachinesBean) o;
        return machineId == that.machineId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(machineId);
    }
}
