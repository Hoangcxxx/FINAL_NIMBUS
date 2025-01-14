package com.example.duantn.controller.client;

import com.example.duantn.entity.KichThuoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/kich_thuoc")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class KichThuocController {
    @Autowired
    private com.example.duantn.service.KichThuocService KichThuocService;
    @GetMapping
    public ResponseEntity<List<KichThuoc>> getAllKichThuoc() {
        List<KichThuoc> KichThuocList = KichThuocService.getAllKichThuoc();
        return new ResponseEntity<>(KichThuocList, HttpStatus.OK);
    }
}
