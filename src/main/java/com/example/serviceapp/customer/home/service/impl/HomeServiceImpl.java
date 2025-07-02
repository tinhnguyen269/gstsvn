package com.example.serviceapp.customer.home.service.impl;

import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.customer.home.repository.HomeRepository;
import com.example.serviceapp.customer.home.service.HomeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    private final HomeRepository homeRepository;

    public HomeServiceImpl(HomeRepository homeRepository) {
        this.homeRepository = homeRepository;
    }

}
