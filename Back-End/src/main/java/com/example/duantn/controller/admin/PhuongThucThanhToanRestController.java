package com.example.duantn.controller.admin;

import com.example.duantn.dto.PhuongThucThanhToanDTO;
import com.example.duantn.service.PhuongThucThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/phuong_thuc_thanh_toan")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PhuongThucThanhToanRestController {

    @Autowired
    private PhuongThucThanhToanService phuongThucThanhToanService;
    @GetMapping("/ten-phuong-thuc")
    public List<PhuongThucThanhToanDTO> getAllPhuongThucThanhToan() {
        return phuongThucThanhToanService.getAllTenPhuongThucThanhToan();
    }
}
