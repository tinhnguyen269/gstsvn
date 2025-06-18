package com.example.serviceapp.admin.login.service.impl;

import com.example.serviceapp.admin.login.repository.ADLoginRepository;
import com.example.serviceapp.admin.login.service.ADLoginService;
import org.springframework.stereotype.Service;

@Service
public class ADLoginServiceImpl implements ADLoginService {
    private final ADLoginRepository adLoginRepository;

    public ADLoginServiceImpl(ADLoginRepository adLoginRepository) {
        this.adLoginRepository = adLoginRepository;
    }


}
