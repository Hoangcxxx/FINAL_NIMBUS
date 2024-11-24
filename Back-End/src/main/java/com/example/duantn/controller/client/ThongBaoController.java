package com.example.duantn.controller.client;

import com.example.duantn.entity.ThongBao;
import com.example.duantn.service.ThongBaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/thong_bao")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class ThongBaoController {
    @Autowired
    private ThongBaoService thongBaoService;
    // API để lấy danh sách thông báo của người dùng
    @GetMapping("/{idNguoiDung}")
    public ResponseEntity<List<ThongBao>> getThongBaosByNguoiDungId(@PathVariable Integer idNguoiDung) {
        List<ThongBao> thongBaos = thongBaoService.getThongBaosByNguoiDungId(idNguoiDung);
        if (thongBaos.isEmpty()) {
            return ResponseEntity.noContent().build();  // Trả về 204 No Content nếu không có thông báo nào
        }
        return ResponseEntity.ok(thongBaos);  // Trả về 200 OK với danh sách thông báo
    }

}