package com.example.serviceapp.common.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
@Entity
@Where(clause = "delete_flag = 0")
public class ImageProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageProjectId;

    private String imageProjectUrl;
    private Long projectId;
    private LocalDateTime createAt;
    private Long createBy;
    private LocalDateTime updateAt;
    private Long updateBy;
    private int deleteFlag;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDateTime.now();
    }

    // Getter, Setter

    public Long getImageProjectId() {
        return imageProjectId;
    }

    public void setImageProjectId(Long imageProjectId) {
        this.imageProjectId = imageProjectId;
    }

    public String getImageProjectUrl() {
        return imageProjectUrl;
    }

    public void setImageProjectUrl(String imageProjectUrl) {
        this.imageProjectUrl = imageProjectUrl;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
