package com.example.duantn.controller.client;

import com.example.duantn.dto.DoiTraDTO;
import com.example.duantn.entity.DoiTra;

import com.example.duantn.service.DoiTraSevice.DoiTraService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/doi-tra")
public class DoiTraController {

    private final DoiTraService doiTraService;

    public DoiTraController(DoiTraService doiTraService) {
        this.doiTraService = doiTraService;
    }

    // API tạo yêu cầu đổi trả
    @PostMapping("/tao")
    public ResponseEntity<DoiTra> taoDoiTra(@RequestBody DoiTraDTO doiTraDTO) {
        DoiTra doiTra = doiTraService.createDoiTra(doiTraDTO);
        return ResponseEntity.ok(doiTra);
    }

    // API lấy danh sách đổi trả theo hóa đơn
    @GetMapping("/hoa-don/{idHoaDon}")
    public ResponseEntity<List<DoiTra>> layDoiTraTheoHoaDon(@PathVariable Integer idHoaDon) {
        List<DoiTra> doiTras = doiTraService.getDoiTraByHoaDonId(idHoaDon);
        return ResponseEntity.ok(doiTras);
    }
}
