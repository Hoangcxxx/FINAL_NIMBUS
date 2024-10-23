package com.example.duantn.controller.admin;

import com.example.duantn.dto.PhuongThucThanhToanDTO;
import com.example.duantn.service.PhuongThucThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phuong-thuc-thanh-toan")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class PhuongThucThanhToanRestController {

    @Autowired
    private PhuongThucThanhToanService phuongThucThanhToanService;
    @GetMapping("/ten-phuong-thuc")
    public List<PhuongThucThanhToanDTO> getAllPhuongThucThanhToan() {
        return phuongThucThanhToanService.getAllTenPhuongThucThanhToan();
    }

}
