package com.example.serviceapp.admin.project_image.service.impl;

import com.example.serviceapp.admin.project_image.repository.ImageProjectRepository;
import com.example.serviceapp.admin.project_image.service.ADImageProjectService;
import com.example.serviceapp.common.entity.ImageProject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ADImageProjectServiceImpl implements ADImageProjectService {

    private final ImageProjectRepository imageRepo;

    public ADImageProjectServiceImpl(ImageProjectRepository imageRepo) {
        this.imageRepo = imageRepo;
    }

    @Transactional
    public ImageProject save(ImageProject img) {
        return imageRepo.save(img);
    }

    public List<ImageProject> findByProjectId(Long projectId) {
        return imageRepo.findByProjectId(projectId);
    }
    @Transactional
    public void deleteByProjectId(Long projectId) {
        imageRepo.deleteByProjectId(projectId);
    }

    @Transactional
    public void deleteByProjectIds(List<Long> ids) {
        for (Long id : ids) {
            imageRepo.deleteByProjectId(id);
        }
    }
}
