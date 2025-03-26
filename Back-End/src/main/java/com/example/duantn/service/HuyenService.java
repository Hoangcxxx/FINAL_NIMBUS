package com.example.duantn.service;

import com.example.duantn.repository.HuyenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HuyenService {
    @Autowired
    private HuyenRepository huyenRepository;

}
