
package com.example.serviceapp.customer.home.service;


import com.example.serviceapp.common.entity.Feedback;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Project;
import com.example.serviceapp.common.entity.Services;

import java.util.List;

public interface HomeService {
    List<Feedback> findFeedback10();

    List<Post> findPost9();

    List<Project> findProject9();
}
