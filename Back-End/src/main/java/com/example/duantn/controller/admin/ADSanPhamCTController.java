package com.example.duantn.controller.admin;

import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.service.SanPhamChiTietService;
import com.example.duantn.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/ad_san_pham_ct")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADSanPhamCTController {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;


}
