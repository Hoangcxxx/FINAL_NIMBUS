package com.example.duantn.controller;

import com.example.duantn.entity.DanhMuc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/danh_muc")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class DanhMucController {
    @Autowired
    private com.example.duantn.service.DanhMucService danhMucService;

    @GetMapping
    @PreAuthorize("hasRole('Khach_Hang') or hasAnyRole('nhan_vien')")
    public ResponseEntity<List<DanhMuc>> getAllDanhMuc() {
        List<DanhMuc> DanhMuccList = danhMucService.getAllDanhMuc();
        return new ResponseEntity<>(DanhMuccList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DanhMuc> createDanhMucc(@RequestBody DanhMuc DanhMucc) {
        DanhMuc newDanhMuc = danhMucService.createDanhMuc(DanhMucc);
        return new ResponseEntity<>(newDanhMuc, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DanhMuc> updateDanhMucc(@PathVariable Integer id, @RequestBody DanhMuc DanhMuccDetails) {
        DanhMuc updatedDanhMuc = danhMucService.updateDanhMuc(id, DanhMuccDetails);
        return new ResponseEntity<>(updatedDanhMuc, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhMucc(@PathVariable Integer id) {
        danhMucService.deleteDanhMuc(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
