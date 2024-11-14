package com.example.duantn.controller.client;

import com.example.duantn.entity.VaiTro;
import com.example.duantn.service.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vai-tro")
public class VaiTroController {

    @Autowired
    private VaiTroService vaiTroService;

    // Tạo một vai trò mới
    @PostMapping("/create")
    public ResponseEntity<VaiTro> createVaiTro(@RequestBody VaiTro vaiTro) {
        VaiTro createdVaiTro = vaiTroService.createVaiTro(vaiTro);
        return new ResponseEntity<>(createdVaiTro, HttpStatus.CREATED);
    }

    // Lấy tất cả vai trò
    @GetMapping("/all")
    public ResponseEntity<List<VaiTro>> getAllVaiTro() {
        List<VaiTro> vaiTros = vaiTroService.getAllVaiTro();
        return new ResponseEntity<>(vaiTros, HttpStatus.OK);
    }

    // Lấy một vai trò theo ID
    @GetMapping("/{idVaiTro}")
    public ResponseEntity<VaiTro> getVaiTroById(@PathVariable Integer idVaiTro) {
        VaiTro vaiTro = vaiTroService.getVaiTroById(idVaiTro);
        return new ResponseEntity<>(vaiTro, HttpStatus.OK);
    }

    // Cập nhật một vai trò theo ID
    @PutMapping("/update/{idVaiTro}")
    public ResponseEntity<VaiTro> updateVaiTro(@PathVariable Integer idVaiTro, @RequestBody VaiTro vaiTroDetails) {
        VaiTro updatedVaiTro = vaiTroService.updateVaiTro(idVaiTro, vaiTroDetails);
        return new ResponseEntity<>(updatedVaiTro, HttpStatus.OK);
    }

    // Xóa một vai trò theo ID
    @DeleteMapping("/delete/{idVaiTro}")
    public ResponseEntity<Void> deleteVaiTro(@PathVariable Integer idVaiTro) {
        vaiTroService.deleteVaiTro(idVaiTro);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
