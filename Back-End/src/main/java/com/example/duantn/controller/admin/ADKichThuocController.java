package com.example.duantn.controller.admin;

import com.example.duantn.entity.KichThuoc;
import com.example.duantn.service.KichThuocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ad_kich_thuoc")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADKichThuocController {
    @Autowired
    private KichThuocService kichThuocService;

    @GetMapping
    public ResponseEntity<List<KichThuoc>> getAllKichThuoc() {
        List<KichThuoc> kichThuoccList = kichThuocService.getAllKichThuoc();
        return new ResponseEntity<>(kichThuoccList, HttpStatus.OK);
    }
    @GetMapping("/{tenKichThuoc}")
    public ResponseEntity<List<KichThuoc>> searchKichThuoc(@PathVariable String tenKichThuoc) {
        List<KichThuoc> kichThuocList = kichThuocService.searchKichThuocByTen(tenKichThuoc);
        return new ResponseEntity<>(kichThuocList, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<KichThuoc> createKichThuocc(@RequestBody KichThuoc kichThuoc) {
        KichThuoc newKichThuoc = kichThuocService.createKichThuoc(kichThuoc);
        return new ResponseEntity<>(newKichThuoc, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KichThuoc> updateKichThuoc(@PathVariable Integer id, @RequestBody KichThuoc kichThuocDetails) {
        KichThuoc updatedKichThuoc = kichThuocService.updateKichThuoc(id, kichThuocDetails);
        return new ResponseEntity<>(updatedKichThuoc, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKichThuoc(@PathVariable Integer id) {
        kichThuocService.deleteKichThuoc(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
