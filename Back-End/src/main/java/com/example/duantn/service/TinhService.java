package com.example.duantn.service;

import com.example.duantn.repository.TinhRepository;
import com.example.duantn.repository.TrangThaiHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TinhService {
    @Autowired
    private TinhRepository tinhRepository;

}
