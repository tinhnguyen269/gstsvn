
package com.example.serviceapp.customer.home.service;


import com.example.serviceapp.common.entity.ProjectActual;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.ProjectImage;

import java.util.List;

public interface HomeService {
    List<ProjectActual> findProjectActual10();

    List<Post> findPost9();

    List<ProjectImage> findProject9();
}
