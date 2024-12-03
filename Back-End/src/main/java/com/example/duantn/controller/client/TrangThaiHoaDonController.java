package com.example.duantn.controller.client;

import com.example.duantn.dto.CapNhatTrangThaiRequest;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.service.TrangThaiHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/trang-thai-hoa-don")
public class TrangThaiHoaDonController {

    @Autowired
    private TrangThaiHoaDonService trangThaiHoaDonService;

    // API để hiển thị trạng thái của tất cả các đơn hàng
    @GetMapping("/tat-ca")
    public List<TrangThaiHoaDon> getAllTrangThaiHoaDon() {
        return trangThaiHoaDonService.getAllTrangThaiHoaDon();
    }

    // API để hiển thị trạng thái của một đơn hàng theo ID
    @GetMapping("/hoa-don/{hoaDonId}")
    public TrangThaiHoaDon getTrangThaiHoaDonByHoaDonId(@PathVariable Integer hoaDonId) {
        return trangThaiHoaDonService.getTrangThaiHoaDonByHoaDonId(hoaDonId);
    }
    // API để cập nhật trạng thái của đơn hàng
    @PostMapping("/cap-nhat")
    public TrangThaiHoaDon capNhatTrangThai(@RequestBody CapNhatTrangThaiRequest request) {
        HoaDon hoaDon = new HoaDon();  // Giả sử bạn lấy hoaDon từ DB
        hoaDon.setIdHoaDon(request.getHoaDonId()); // ID đơn hàng truyền vào
        return trangThaiHoaDonService.capNhatTrangThai(hoaDon, request.getLoaiTrangThaiId());
    }
}
