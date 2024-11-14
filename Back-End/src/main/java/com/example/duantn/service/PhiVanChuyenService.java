package com.example.duantn.service;

import com.example.duantn.entity.PhiVanChuyen;
import com.example.duantn.repository.PhiVanChuyenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhiVanChuyenService {

    @Autowired
    private PhiVanChuyenRepository phiVanChuyenRepository;

    public PhiVanChuyen createPhiVanChuyen(PhiVanChuyen phiVanChuyen) {
        return phiVanChuyenRepository.save(phiVanChuyen);
    }

    public List<PhiVanChuyen> getAllPhiVanChuyen() {
        return phiVanChuyenRepository.findAll();
    }
}
