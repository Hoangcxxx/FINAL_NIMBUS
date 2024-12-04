package com.example.duantn.controller.admin;

import com.example.duantn.dto.HoaDonDTO;
import com.example.duantn.dto.HoaDonResponseDTO;
import com.example.duantn.dto.HoaDonUpdateDTO;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.service.HoaDonService;
import com.example.duantn.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/hoa_don_ban_hang")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class HoaDonRestController {
    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    private NguoiDungService nguoiDungService;


    @GetMapping("/nguoi-dung")
    public List<NguoiDung> getNguoiDung() {
        return nguoiDungService.getAllNguoiDungsByRoleId();
    }

    @GetMapping("/chua-thanh-toan")
    public ResponseEntity<List<HoaDon>> getHDChuaThanhToan() {
        List<HoaDon> hoaDons = hoaDonService.getHoaDonChuaThanhToan();
        return ResponseEntity.ok(hoaDons);  // Trả về danh sách HoaDon
    }
    @GetMapping("/get-hoa-don")
    public ResponseEntity<List<HoaDonDTO>> getHoaDonWithDetails() {
        List<HoaDonDTO> hoaDonList = hoaDonService.getHoaDonWithDetails();
        return ResponseEntity.ok(hoaDonList);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createHoaDon(@RequestBody HoaDon hoaDon) {
        System.out.println("Nhận được dữ liệu hóa đơn: " + hoaDon);

        if (hoaDon.getNguoiDung() == null || hoaDon.getNguoiDung().getIdNguoiDung() == null) {
            return ResponseEntity.badRequest().body("Vui lòng chọn người dùng để tạo hóa đơn!!.");
        }

        NguoiDung nguoiDung = nguoiDungService.findById(hoaDon.getNguoiDung().getIdNguoiDung());
        if (nguoiDung != null) {
            hoaDon.setNguoiDung(nguoiDung);
            HoaDon createdHoaDon = hoaDonService.createHoaDon(hoaDon);
            return ResponseEntity.ok(createdHoaDon);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Người dùng không tồn tại.");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteHoaDon(@PathVariable Integer id) {

        hoaDonService.deleteHoaDon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoaDon> getHoaDonById(@PathVariable Integer id) {
        HoaDon hoaDon = hoaDonService.findById(id);

        if (hoaDon == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(hoaDon);
    }


    @PutMapping("/{id}/update-status")
    public ResponseEntity<?> updateInvoiceStatus(@PathVariable Integer id) {
        try {
            boolean isUpdated = hoaDonService.updateInvoiceStatus(id, true);

            if (isUpdated) {
                HoaDon updatedInvoice = hoaDonService.getInvoiceById(id);
                return ResponseEntity.ok(updatedInvoice);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Không thể cập nhật trạng thái hóa đơn.");
            }
        } catch (Exception e) {
            // Xử lý lỗi bất ngờ
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi trong quá trình cập nhật: " + e.getMessage());
        }
    }

    @PutMapping("/cap-nhat/{id}")
    public ResponseEntity<HoaDonResponseDTO> updateHoaDon(
            @PathVariable int id,
            @RequestBody HoaDonUpdateDTO updateHoaDonDTO) {
        HoaDonResponseDTO updatedResponse = hoaDonService.updateHoaDon(id, updateHoaDonDTO);
        return ResponseEntity.ok(updatedResponse);
    }

}
