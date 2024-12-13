package com.example.duantn.service;

import com.example.duantn.entity.TrangThaiGiamGia;
import com.example.duantn.repository.TrangThaiGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TrangThaiGiamGiaService {
    @Autowired
    private TrangThaiGiamGiaRepository trangThaiGiamGiaRepository;

    public List<TrangThaiGiamGia> getAllTrangThaiGiamGia() {
        return trangThaiGiamGiaRepository.findAll();
    }



}
