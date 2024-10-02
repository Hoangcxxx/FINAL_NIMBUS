package com.example.duantn.controller.admin;

import com.example.duantn.entity.HinhAnhSanPham;
import com.example.duantn.repository.HinhAnhSanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hinh-anh")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class HinhAnhSanPhamController {
    @Autowired
    private HinhAnhSanPhamRepository hinhAnhSanPhamRepository;
//    @Autowired
//    private HinhAnhSanPhamRepo hinhAnhSanPhamRepo;

    @GetMapping("/hien-thi")
    public List<HinhAnhSanPham> hienthi() {
        return hinhAnhSanPhamRepository.findAll();
    }
}



