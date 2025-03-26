package com.example.duantn.controller.admin;

import com.example.duantn.dto.PhuongThucThanhToanHoaDonDTO;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.PhuongThucThanhToan;
import com.example.duantn.entity.PhuongThucThanhToanHoaDon;
import com.example.duantn.service.HoaDonService;
import com.example.duantn.service.PhuongThucThanhToanService;
import com.example.duantn.service.PtThanhToanHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/phuong_thuc_thanh_toan_hoa_don")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class PtThanhToanHoaDonController {

    @Autowired
    private PtThanhToanHoaDonService ptThanhToanHoaDonService;

    @Autowired
    private PhuongThucThanhToanService phuongThucThanhToanService;

    @Autowired
    private HoaDonService hoaDonService;

    @PostMapping("/{id}/thanh-toan")
    public ResponseEntity<?> createPaymentMethod(
            @PathVariable Integer id, @RequestBody PhuongThucThanhToanHoaDon paymentMethod) {
        // Kiểm tra id hóa đơn
        if (id == null) {
            return ResponseEntity.badRequest().body("ID hóa đơn không được để trống.");
        }

        HoaDon hoaDon = hoaDonService.findById(id);
        if (hoaDon == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy hóa đơn với ID: " + id);
        }

        // Kiểm tra dữ liệu phương thức thanh toán
        if (paymentMethod == null || paymentMethod.getPhuongThucThanhToan() == null) {
            return ResponseEntity.badRequest().body("Dữ liệu phương thức thanh toán không hợp lệ.");
        }

        Integer phuongThucId = paymentMethod.getPhuongThucThanhToan().getIdPTThanhToan();
        if (phuongThucId == null) {
            return ResponseEntity.badRequest().body("ID phương thức thanh toán không được để trống.");
        }

        PhuongThucThanhToan phuongThuc = phuongThucThanhToanService.findById(phuongThucId);
        if (phuongThuc == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy phương thức thanh toán với ID: " + phuongThucId);
        }

        // Thiết lập dữ liệu cho phương thức thanh toán
        paymentMethod.setPhuongThucThanhToan(phuongThuc);
        paymentMethod.setHoaDon(hoaDon);
        paymentMethod.setTrangThai("Hoàn Thành");

        try {
            PhuongThucThanhToanHoaDon createdPaymentMethod = ptThanhToanHoaDonService.createPaymentMethod(paymentMethod);

            // Chuyển đổi sang DTO
            PhuongThucThanhToanHoaDonDTO dto = new PhuongThucThanhToanHoaDonDTO(
                    createdPaymentMethod.getIdThanhToanHoaDon(),
                    phuongThuc.getTenPhuongThuc(), // Lấy tên phương thức thanh toán
                    hoaDon.getMaHoaDon(),
                    createdPaymentMethod.getNgayGiaoDich(),
                    createdPaymentMethod.getMoTa(),
                    createdPaymentMethod.getTrangThai(),
                    createdPaymentMethod.getNoiDungThanhToan()
            );

            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi tạo phương thức thanh toán: " + e.getMessage());
        }
    }
}
