package com.example.duantn.controller;


import com.example.duantn.entity.GioHang;
import com.example.duantn.repository.GioHangRepository;
import com.example.duantn.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/gio_hang")
public class GioHangController {

    @Autowired
    private GioHangService ghsv;


    @GetMapping("/hien-thi")
    public ResponseEntity<List<GioHang>> finalsl(){
        List<GioHang> list = ghsv.loading();
        return ResponseEntity.ok(list);
    }
}
