
package com.example.serviceapp.customer.home.service;


import com.example.serviceapp.common.entity.Feedback;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.common.entity.Services;

import java.util.List;

public interface HomeService {
    List<Post> findAll();

    List<Feedback> findAllFeedback();
}
