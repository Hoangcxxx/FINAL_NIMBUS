package com.example.duantn.service;

import com.example.duantn.dto.*;

import com.example.duantn.entity.*;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.PhuongThucThanhToanHoaDonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private PhuongThucThanhToanHoaDonRepository ptThanhToanHoaDonRepository;
    public List<HoaDon> getHoaDonChuaThanhToan() {
        return hoaDonRepository.findAllByTrangThaiFalse();
    }
    public byte[] exportInvoicesToExcel() throws IOException {
        List<HoaDonDTO> hoaDons = getHoaDonWithDetails(); // Lấy danh sách hóa đơn

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Hóa Đơn");

            // Tạo tiêu đề cho các cột
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Mã Hóa Đơn");
            headerRow.createCell(1).setCellValue("Tên Người Dùng");
            headerRow.createCell(2).setCellValue("SĐT Người Dùng");
            headerRow.createCell(3).setCellValue("Ngày Tạo");
            headerRow.createCell(4).setCellValue("Phương Thức");
            headerRow.createCell(5).setCellValue("Tổng Tiền");
            headerRow.createCell(6).setCellValue("Trạng Thái");
            headerRow.createCell(7).setCellValue("Loại");

            // Thêm dữ liệu hóa đơn vào sheet
            int rowNum = 1;
            for (HoaDonDTO hoaDon : hoaDons) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(hoaDon.getMaHoaDon());
                row.createCell(1).setCellValue(hoaDon.getTenNguoiDung());
                row.createCell(2).setCellValue(hoaDon.getSdtNguoiDung());
                row.createCell(3).setCellValue(hoaDon.getNgayTao().toString());
                row.createCell(4).setCellValue(hoaDon.getTenPhuongThuc());
                row.createCell(5).setCellValue(hoaDon.getTongTien().toString());
                row.createCell(6).setCellValue(hoaDon.getTenTrangThai());
                row.createCell(7).setCellValue(hoaDon.getLoai());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    public void deleteHoaDon(Integer id) {
        if (!hoaDonRepository.existsById(id)) {
            throw new EntityNotFoundException("Hóa đơn không tồn tại với ID: " + id);
        }
        hoaDonRepository.deleteById(id);
    }
    //xem danh sách hóa đơn
    public List<HoaDonDTO> getHoaDonWithDetails() {
        return hoaDonRepository.getHoaDonWithDetails();
    }

    public HoaDon createHoaDon(HoaDon hoaDon) {
        String newMaHoaDon = generateMaHoaDon();
        hoaDon.setMaHoaDon(newMaHoaDon);
        TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
        trangThaiHoaDon.setId(1);
        hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
        hoaDon.setLoai("Tại quầy");
        /// my check
        hoaDon.setNgayThanhToan(new Date());
        return hoaDonRepository.save(hoaDon);
    }

    private String generateMaHoaDon() {
        List<String> lastMaHoaDonList = hoaDonRepository.findLastMaHoaDon();
        if (!lastMaHoaDonList.isEmpty()) {
            String lastMaHoaDon = lastMaHoaDonList.get(0);
            int newId = Integer.parseInt(lastMaHoaDon.substring(2)) + 1;
            return String.format("HD%03d", newId);
        } else {
            return "HD001";
        }
    }
    public HoaDon findById(Integer id) {
        return hoaDonRepository.findById(id).orElse(null);
    }
    public boolean updateInvoiceStatus(Integer id, boolean trangThai) {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(id);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            hoaDon.setTrangThai(trangThai);
            hoaDonRepository.save(hoaDon);
            return true;
        }
        return false;
    }

    @Transactional
    public HoaDonResponseDTO updateHoaDon(int idHoaDon, HoaDonUpdateDTO updateHoaDonDTO) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        hoaDon.setPhiShip(updateHoaDonDTO.getPhiShip());
        hoaDon.setNgayThanhToan(updateHoaDonDTO.getNgayThanhToan());
        hoaDon.setThanhTien(updateHoaDonDTO.getThanhTien());

        PtThanhToanHoaDon ptThanhToan = new PtThanhToanHoaDon();
        ptThanhToan.setId(updateHoaDonDTO.getIdPtThanhToanHoaDon());
        PtThanhToanHoaDon existingPtThanhToan = ptThanhToanHoaDonRepository.findById(ptThanhToan.getId())
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không tồn tại."));
        hoaDon.setPtThanhToanHoaDon(existingPtThanhToan);

        TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
        trangThaiHoaDon.setId(updateHoaDonDTO.getTrangThaiHoaDon());
        hoaDon.setTrangThaiHoaDon(trangThaiHoaDon);
        hoaDonRepository.save(hoaDon);

        // Trả về DTO với dữ liệu mong muốn
        return new HoaDonResponseDTO(hoaDon.getPhiShip(), hoaDon.getNgayThanhToan(), hoaDon.getThanhTien(), hoaDon.getPtThanhToanHoaDon().getId());
    }


    public void capNhatTrangThaiHoaDon(int idHoaDon, TrangThaiHoaDon trangThaiMoi) throws Exception {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();
            hoaDon.setTrangThaiHoaDon(trangThaiMoi);
            hoaDonRepository.save(hoaDon);
        } else {
            throw new Exception("Không tìm thấy hóa đơn với ID: " + idHoaDon);
        }
    }
    }

