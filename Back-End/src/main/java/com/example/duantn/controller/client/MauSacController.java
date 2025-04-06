package com.example.duantn.controller.client;

import com.example.duantn.entity.MauSac;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/mau_sac")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class MauSacController {
    @Autowired
    private com.example.duantn.service.MauSacService mauSacService;
    @GetMapping
    public ResponseEntity<List<MauSac>> getAllMauSac() {
        List<MauSac> MauSacList = mauSacService.getAllMauSac();
        return new ResponseEntity<>(MauSacList, HttpStatus.OK);
    }
}
