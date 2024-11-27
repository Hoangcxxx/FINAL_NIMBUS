package com.example.duantn.controller.admin;

import com.example.duantn.entity.DanhMuc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/danh_muc")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADDanhMucController {
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
    @PostMapping
    public ResponseEntity<DanhMuc> createDanhMuc(@RequestBody DanhMuc DanhMuc) {
        DanhMuc newDanhMuc = danhMucService.createDanhMuc(DanhMuc);
        return new ResponseEntity<>(newDanhMuc, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DanhMuc> updateDanhMuc(@PathVariable Integer id, @RequestBody DanhMuc DanhMucDetails) {
        DanhMuc updatedDanhMuc = danhMucService.updateDanhMuc(id, DanhMucDetails);
        return new ResponseEntity<>(updatedDanhMuc, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDanhMuc(@PathVariable Integer id) {
        danhMucService.deleteDanhMuc(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
