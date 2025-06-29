
package com.example.serviceapp.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Where(clause = "delete_flag = 0")
public class ImgService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgServiceId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String imageServiceUrl;
    private LocalDateTime createAt;
    private Long createBy;
    private LocalDateTime updateAt;
    private Long updateBy;
    private int deleteFlag;

    public Long getImgServiceId() {
        return imgServiceId;
    }

    public void setImgServiceId(Long imgServiceId) {
        this.imgServiceId = imgServiceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageServiceUrl() {
        return imageServiceUrl;
    }

    public void setImageServiceUrl(String imageServiceUrl) {
        this.imageServiceUrl = imageServiceUrl;
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
