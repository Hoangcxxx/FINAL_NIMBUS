package com.example.duantn.controller.admin;

import com.example.duantn.entity.TrangThaiGiamGia;
import com.example.duantn.service.TrangThaiGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/trang_thai_giam_gia")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADTrangThaiGiamGiaController {
    @Autowired
    private TrangThaiGiamGiaService trangThaiGiamGiaService;
    @GetMapping
    public List<TrangThaiGiamGia> getAllTrangThaiGiamGias() {
        return trangThaiGiamGiaService.getAllTrangThaiGiamGia();
    }

}
