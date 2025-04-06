package com.example.duantn.service;

import com.example.duantn.repository.TrangThaiHoaDonRepository;
import com.example.duantn.repository.XaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class XaService {
    @Autowired
    private XaRepository xaRepository;

}
