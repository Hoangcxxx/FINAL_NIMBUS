package com.example.duantn.controller.admin;

import com.example.duantn.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ad_san_pham_ct")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ADSanPhamCTController {
    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @DeleteMapping("/{idSanPhamCT}")
    public ResponseEntity<Void> deleteSanPham(@PathVariable Integer idSanPhamCT) {
        sanPhamChiTietService.deleteById(idSanPhamCT);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
