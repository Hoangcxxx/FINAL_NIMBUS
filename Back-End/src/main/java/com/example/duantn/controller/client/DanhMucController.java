package com.example.duantn.controller.client;

import com.example.duantn.entity.DanhMuc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/danh_muc")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class DanhMucController {
    @Autowired
    private com.example.duantn.service.DanhMucService danhMucService;
    @GetMapping
    public ResponseEntity<List<DanhMuc>> getAllDanhMuc() {
        List<DanhMuc> DanhMuccList = danhMucService.getAllDanhMuc();
        return new ResponseEntity<>(DanhMuccList, HttpStatus.OK);
    }
    @GetMapping("/{tenDanhMuc}")
    public ResponseEntity<List<DanhMuc>> searchDanhMuc(@PathVariable String tenDanhMuc) {
        List<DanhMuc> danhMucList = danhMucService.searchDanhMucByTen(tenDanhMuc);
        return new ResponseEntity<>(danhMucList, HttpStatus.OK);
    }
}
