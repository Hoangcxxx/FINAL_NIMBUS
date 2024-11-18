package com.example.duantn.controller.admin;

import com.example.duantn.dto.*;
import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.service.HoaDonService;
import com.example.duantn.service.InvoicePDFService;
import com.example.duantn.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hoa-don")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class HoaDonRestController {
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private NguoiDungService nguoiDungService;
    @Autowired
    private InvoicePDFService invoicePDFService;

    @GetMapping("/nguoi-dung")
    public List<NguoiDung> getNguoiDung() {
        return nguoiDungService.getAllNguoiDung();
    }

    @GetMapping("/chua-thanh-toan")
    public List<HoaDon> getHoaDonChuaThanhToan() {
        List<HoaDon> hoaDonList = hoaDonService.getHoaDonChuaThanhToan();
        return hoaDonList.size() > 5 ? hoaDonList.subList(0, 5) : hoaDonList;
    }
    @GetMapping("/get-hoa-don")
    public ResponseEntity<List<HoaDonDTO>> getHoaDonWithDetails() {
        List<HoaDonDTO> hoaDonList = hoaDonService.getHoaDonWithDetails();
        return ResponseEntity.ok(hoaDonList);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createHoaDon(@RequestBody HoaDon hoaDon) {
        System.out.println("Nhận được dữ liệu hóa đơn: " + hoaDon);

        // Kiểm tra ID người dùng
        if (hoaDon.getNguoiDung() == null || hoaDon.getNguoiDung().getId() == null) {
            return ResponseEntity.badRequest().body("Vui lòng chọn người dùng để tạo hóa đơn!!.");
        }

        NguoiDung nguoiDung = nguoiDungService.findById(hoaDon.getNguoiDung().getId());
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
    public ResponseEntity<String> updateInvoiceStatus(@PathVariable Integer id) {
        boolean isUpdated = hoaDonService.updateInvoiceStatus(id, true);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/cap-nhat/{id}")
    public ResponseEntity<HoaDonResponseDTO> updateHoaDon(
            @PathVariable int id,
            @RequestBody HoaDonUpdateDTO updateHoaDonDTO) {
        HoaDonResponseDTO updatedResponse = hoaDonService.updateHoaDon(id, updateHoaDonDTO);
        return ResponseEntity.ok(updatedResponse);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportInvoices() {
        try {
            byte[] excelFile = hoaDonService.exportInvoicesToExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=hoa_don.xlsx");
            return new ResponseEntity<>(excelFile, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/cap-nhat-trang-thai/{idHoaDon}")
    public ResponseEntity<Map<String, String>> capNhatTrangThaiHoaDon(
            @PathVariable int idHoaDon,
            @RequestParam("idTrangThaiMoi") int idTrangThaiMoi) {
        try {
            TrangThaiHoaDon trangThaiMoi = new TrangThaiHoaDon();
            trangThaiMoi.setId(idTrangThaiMoi);
            hoaDonService.capNhatTrangThaiHoaDon(idHoaDon, trangThaiMoi);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cập nhật trạng thái hóa đơn thành công.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Không tìm thấy hóa đơn với ID: " + idHoaDon);

            return ResponseEntity.status(404).body(errorResponse);
        }
    }
}
