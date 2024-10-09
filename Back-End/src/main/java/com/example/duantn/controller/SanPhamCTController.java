package com.example.duantn.controller;

import com.example.duantn.entity.SanPhamChiTiet;
import com.example.duantn.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/san-pham-chi-tiet")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class SanPhamCTController {
    @Autowired
    private SanPhamChiTietService service;

    @GetMapping
    public ResponseEntity<List<SanPhamChiTiet>> getAll() {
        List<SanPhamChiTiet> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamChiTiet> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SanPhamChiTiet> create(@RequestBody SanPhamChiTiet sanPhamChiTiet) {
        SanPhamChiTiet created = service.create(sanPhamChiTiet);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPhamChiTiet> update(@PathVariable Integer id, @RequestBody SanPhamChiTiet sanPhamChiTiet) {
        return ResponseEntity.ok(service.update(id, sanPhamChiTiet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
