package com.example.duantn.service;

import com.example.duantn.dto.*;

import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import org.apache.poi.ss.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private LoaiTrangThaiRepository loaiTrangThaiRepository;
    @Autowired
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;

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
            headerRow.createCell(6).setCellValue("Loại");

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
                row.createCell(6).setCellValue(hoaDon.getLoai());
            }

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    @Transactional
    public void deleteHoaDon(Integer id) {
        if (!hoaDonRepository.existsById(id)) {
            throw new EntityNotFoundException("Hóa đơn không tồn tại với ID: " + id);
        }
        trangThaiHoaDonRepository.deleteByHoaDonId(id);
        hoaDonRepository.deleteById(id);
    }

    public List<HoaDonDTO> getHoaDonWithDetails() {
        return hoaDonRepository.getHoaDonWithDetails();
    }

    public HoaDon createHoaDon(HoaDon hoaDon) {
        String newMaHoaDon = generateMaHoaDon();
        hoaDon.setMaHoaDon(newMaHoaDon);
        hoaDon.setLoai(0);
        HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);

        // Tạo trạng thái hóa đơn với loại trạng thái là 1
        TrangThaiHoaDon trangThaiHoaDon1 = new TrangThaiHoaDon();
        trangThaiHoaDon1.setMoTa("Tạo đơn hàng");
        trangThaiHoaDon1.setNgayTao(new Date());
        trangThaiHoaDon1.setNgayCapNhat(new Date());

        LoaiTrangThai loaiTrangThai1 = loaiTrangThaiRepository.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));

        trangThaiHoaDon1.setLoaiTrangThai(loaiTrangThai1);
        trangThaiHoaDon1.setHoaDon(savedHoaDon);
        trangThaiHoaDonRepository.save(trangThaiHoaDon1);

        // Tạo trạng thái hóa đơn với loại trạng thái là 4
        TrangThaiHoaDon trangThaiHoaDon2 = new TrangThaiHoaDon();
        trangThaiHoaDon2.setMoTa("Chờ Thanh Toán");
        trangThaiHoaDon2.setNgayTao(new Date());
        trangThaiHoaDon2.setNgayCapNhat(new Date());

        LoaiTrangThai loaiTrangThai2 = loaiTrangThaiRepository.findById(4)
                .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));

        trangThaiHoaDon2.setLoaiTrangThai(loaiTrangThai2);
        trangThaiHoaDon2.setHoaDon(savedHoaDon);
        trangThaiHoaDonRepository.save(trangThaiHoaDon2);

        return savedHoaDon;
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

    public HoaDon getInvoiceById(Integer id) {
        // Gọi repository để tìm hóa đơn theo ID
        return hoaDonRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy hóa đơn với ID: " + id));
    }

    @Transactional
    public HoaDonResponseDTO updateHoaDon(int idHoaDon, HoaDonUpdateDTO updateHoaDonDTO) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        hoaDon.setPhiShip(updateHoaDonDTO.getPhiShip());
        hoaDon.setNgayThanhToan(updateHoaDonDTO.getNgayThanhToan());
        hoaDon.setThanhTien(updateHoaDonDTO.getThanhTien());
        hoaDon.setSdtNguoiNhan(updateHoaDonDTO.getSetSdtNguoiNhan());

        PtThanhToanHoaDon ptThanhToan = new PtThanhToanHoaDon();
        ptThanhToan.setId(updateHoaDonDTO.getIdPtThanhToanHoaDon());

        PtThanhToanHoaDon existingPtThanhToan = ptThanhToanHoaDonRepository.findById(ptThanhToan.getId())
                .orElseThrow(() -> new RuntimeException("Phương thức thanh toán không tồn tại."));
        hoaDon.setPtThanhToanHoaDon(existingPtThanhToan);

        if (updateHoaDonDTO.getIdVoucher() > 0) {
            Voucher voucher = voucherRepository.findById(updateHoaDonDTO.getIdVoucher())
                    .orElseThrow(() -> new RuntimeException("Voucher không tồn tại"));
            hoaDon.setVoucher(voucher);
        }

        if (!existingPtThanhToan.getPhuongThucThanhToan().getId().equals(2) &&
                !existingPtThanhToan.getPhuongThucThanhToan().getId().equals(3)) {
            // Tạo trạng thái hóa đơn loại 5
            TrangThaiHoaDon trangThaiHoaDon1 = new TrangThaiHoaDon();
            trangThaiHoaDon1.setMoTa("Đã thanh toán thành công");
            trangThaiHoaDon1.setNgayTao(new Date());
            trangThaiHoaDon1.setNgayCapNhat(new Date());
            trangThaiHoaDon1.setHoaDon(hoaDon);

            LoaiTrangThai loaiTrangThai1 = loaiTrangThaiRepository.findById(5)
                    .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));
            trangThaiHoaDon1.setLoaiTrangThai(loaiTrangThai1);

            trangThaiHoaDonRepository.save(trangThaiHoaDon1);

            // Tạo trạng thái hóa đơn loại 8
            TrangThaiHoaDon trangThaiHoaDon2 = new TrangThaiHoaDon();
            trangThaiHoaDon2.setMoTa("Hoàn thành    ");
            trangThaiHoaDon2.setNgayTao(new Date());
            trangThaiHoaDon2.setNgayCapNhat(new Date());
            trangThaiHoaDon2.setHoaDon(hoaDon);

            LoaiTrangThai loaiTrangThai2 = loaiTrangThaiRepository.findById(8)
                    .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));
            trangThaiHoaDon2.setLoaiTrangThai(loaiTrangThai2);

            trangThaiHoaDonRepository.save(trangThaiHoaDon2);
        }

        hoaDonRepository.save(hoaDon);

        return new HoaDonResponseDTO(
                hoaDon.getPhiShip(),
                hoaDon.getNgayThanhToan(),
                hoaDon.getThanhTien(),
                hoaDon.getPtThanhToanHoaDon().getId()
        );
    }

}

