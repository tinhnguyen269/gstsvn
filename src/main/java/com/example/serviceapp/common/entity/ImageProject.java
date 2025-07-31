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
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

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
