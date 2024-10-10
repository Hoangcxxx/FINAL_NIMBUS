package com.example.duantn.controller;

import com.example.duantn.entity.GioHang;
import com.example.duantn.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gio_hang")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class GioHangController {

    @Autowired
    private GioHangService ghsv;

    @GetMapping("/hien-thi")
    public ResponseEntity<List<GioHang>> getGioHangList() {
        List<GioHang> list = ghsv.loading();
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
}
