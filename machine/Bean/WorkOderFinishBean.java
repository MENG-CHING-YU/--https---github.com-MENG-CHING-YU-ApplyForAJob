package com.project.machine.Bean;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.project.workorder.model.WorkOrder;

@Entity
@Table(name = "production_reports")
public class WorkOderFinishBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // 無參數建構子（JPA 必須）
    public WorkOderFinishBean() {
    }

    // 主鍵
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private int reportId;

    // 關聯到 WorkOrder 的 ID
    @Column(name = "wo_id")
    private Long woId;

    // 成功數量
    @Column(name = "quantity_done", nullable = false)
    private int quantityDone = 0;

    // 失敗數量
    @Column(name = "quantity_failed", nullable = false)
    private int quantityFailed = 0;

    // 回報時間（由資料庫自動填入）
    @Column(name = "report_time", insertable = false, updatable = false)
    private LocalDateTime reportTime;

    // Getter & Setter
    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Long getWoId() {
        return woId;
    }

    public void setWoId(Long woId) {
        this.woId = woId;
    }

    public int getQuantityDone() {
        return quantityDone;
    }

    public void setQuantityDone(int quantityDone) {
        this.quantityDone = quantityDone;
    }

    public int getQuantityFailed() {
        return quantityFailed;
    }

    public void setQuantityFailed(int quantityFailed) {
        this.quantityFailed = quantityFailed;
    }

    public LocalDateTime getReportTime() {
        return reportTime;
    }

    public void setReportTime(LocalDateTime reportTime) {
        this.reportTime = reportTime;
    }

    @Override
    public String toString() {
        return "WorkOderFinishBean{" +
                "reportId=" + reportId +
                ", woId=" + woId +
                ", quantityDone=" + quantityDone +
                ", quantityDone=" + quantityDone +
                ", quantityFailed=" + quantityFailed +
                ", reportTime=" + reportTime +
                '}';
    }
}
