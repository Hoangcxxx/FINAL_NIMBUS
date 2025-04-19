package com.example.duantn.service;

import com.example.duantn.dto.DoiTraDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import com.example.duantn.service.DoiTraSevice.DoiTraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoiTraServiceImpl implements DoiTraService {
    private final DoiTraRepository doiTraRepository;
    private final HoaDonRepository hoaDonRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final TrangThaiHoaDonRepository trangThaiHoaDonRepository;
    private final LoaiTrangThaiRepository loaiTrangThaiRepository;

    public DoiTraServiceImpl(DoiTraRepository doiTraRepository,
                             HoaDonRepository hoaDonRepository,
                             SanPhamChiTietRepository sanPhamChiTietRepository,
                             TrangThaiHoaDonRepository trangThaiHoaDonRepository,
                             LoaiTrangThaiRepository loaiTrangThaiRepository) {
        this.doiTraRepository = doiTraRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.trangThaiHoaDonRepository = trangThaiHoaDonRepository;
        this.loaiTrangThaiRepository = loaiTrangThaiRepository;
    }

    @Override
    public List<DoiTra> createDoiTra(List<DoiTraDTO> doiTraDTOList) {
        return doiTraDTOList.stream().map(doiTraDTO -> {
            // Lấy hóa đơn từ database
            HoaDon hoaDon = hoaDonRepository.findById(doiTraDTO.getIdHoaDon())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + doiTraDTO.getIdHoaDon()));

            // Lấy sản phẩm chi tiết từ database
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(doiTraDTO.getIdSanPhamChiTiet())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với ID: " + doiTraDTO.getIdSanPhamChiTiet()));

            // Lấy thông tin chi tiết hóa đơn để kiểm tra số lượng đặt hàng ban đầu
            HoaDonChiTiet hoaDonChiTiet = hoaDon.getHoaDonChiTietList().stream()
                    .filter(hdct -> hdct.getSanPhamChiTiet().getIdSanPhamChiTiet().equals(doiTraDTO.getIdSanPhamChiTiet()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy chi tiết hóa đơn cho sản phẩm ID: " + doiTraDTO.getIdSanPhamChiTiet()));

            // Kiểm tra số lượng hoàn trả không vượt quá số lượng đã đặt
            if (doiTraDTO.getSoLuong() > hoaDonChiTiet.getSoLuong()) {
                throw new RuntimeException("Số lượng hoàn trả không hợp lệ. Số lượng hoàn trả không được vượt quá số lượng đã đặt ban đầu cho sản phẩm ID: "
                        + doiTraDTO.getIdSanPhamChiTiet());
            }

            // Tạo yêu cầu đổi trả
            DoiTra doiTra = new DoiTra();
            doiTra.setHoaDon(hoaDon);
            doiTra.setSanPhamChiTiet(sanPhamChiTiet);
            doiTra.setSoLuong(doiTraDTO.getSoLuong());
            doiTra.setLyDo(doiTraDTO.getLyDo());
            doiTra.setTongTien(doiTraDTO.getTongTien());
            doiTra.setTrangThai(false);
            doiTra.setNgayTao(new Date());
            doiTra.setNgayCapNhat(new Date());

            // Thêm trạng thái "Yêu cầu hoàn trả"
            createTrangThaiHoaDon(hoaDon, 9, "Yêu cầu hoàn trả");

            return doiTraRepository.save(doiTra); // Lưu yêu cầu đổi trả vào DB
        }).collect(Collectors.toList());
    }


    @Override
    public DoiTra createDoiTra(DoiTraDTO doiTraDTO) {
        return null;
    }

    // Phương thức tạo trạng thái cho hóa đơn
    private void createTrangThaiHoaDon(HoaDon hoaDon, int loaiTrangThaiId, String moTa) {
        LoaiTrangThai loaiTrangThai = loaiTrangThaiRepository.findById(loaiTrangThaiId)
                .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));

        TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
        trangThaiHoaDon.setMoTa(moTa);
        trangThaiHoaDon.setNgayTao(new Date());
        trangThaiHoaDon.setIdNhanVien(1);  // ID nhân viên giả định, tùy chỉnh theo nhu cầu thực tế
        trangThaiHoaDon.setNgayCapNhat(new Date());
        trangThaiHoaDon.setLoaiTrangThai(loaiTrangThai);
        trangThaiHoaDon.setHoaDon(hoaDon);

        trangThaiHoaDonRepository.save(trangThaiHoaDon);
    }

    @Override
    public List<DoiTra> getDoiTraByHoaDonId(Integer idHoaDon) {
        return doiTraRepository.findByHoaDonIdHoaDon(idHoaDon);
    }

    @Override
    public List<Object[]> getAllSanPhamDoiTraByIdHoaDon(Integer idHoaDon) {
        return doiTraRepository.getAllSanPhamDoiTraByIdHoaDon(idHoaDon); // Trả về danh sách từ repository
    }

    @Override
    @Transactional
    public boolean capNhatTrangThaiHoanTra(Integer idHoaDon, Integer idLoaiTrangThai, Integer idNhanVien) {
        List<DoiTra> doiTraList = doiTraRepository.findByHoaDon_IdHoaDon(idHoaDon);
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findById(idHoaDon);
        Optional<LoaiTrangThai> loaiTrangThaiOpt = loaiTrangThaiRepository.findById(idLoaiTrangThai);
        Optional<LoaiTrangThai> loaiTrangThaiHoanTraOpt = loaiTrangThaiRepository.findById(8); // Trạng thái mặc định "Hoàn trả"

        if (hoaDonOpt.isEmpty() || loaiTrangThaiOpt.isEmpty() || loaiTrangThaiHoanTraOpt.isEmpty()) {
            return false;
        }

        HoaDon hoaDon = hoaDonOpt.get();
        LoaiTrangThai loaiTrangThai = loaiTrangThaiOpt.get();
        LoaiTrangThai loaiTrangThaiHoanTra = loaiTrangThaiHoanTraOpt.get();

        // Lưu trạng thái "Hoàn trả" (Mặc định - ID = 8)
        TrangThaiHoaDon trangThaiHoanTra = new TrangThaiHoaDon();
        trangThaiHoanTra.setIdNhanVien(idNhanVien); // Gán idNhanVien cho trạng thái hiện tại
        trangThaiHoanTra.setHoaDon(hoaDon);
        trangThaiHoanTra.setLoaiTrangThai(loaiTrangThaiHoanTra);
        trangThaiHoanTra.setNgayTao(new Date());
        trangThaiHoanTra.setNgayCapNhat(new Date());
        trangThaiHoanTra.setMoTa(loaiTrangThaiHoanTra.getMoTa());
        trangThaiHoaDonRepository.save(trangThaiHoanTra);

        // Lưu trạng thái nhập vào (10 hoặc 11)
        TrangThaiHoaDon trangThaiNhapVao = new TrangThaiHoaDon();
        trangThaiNhapVao.setIdNhanVien(idNhanVien); // Gán idNhanVien cho trạng thái hiện tại
        trangThaiNhapVao.setHoaDon(hoaDon);
        trangThaiNhapVao.setLoaiTrangThai(loaiTrangThai);
        trangThaiNhapVao.setNgayTao(new Date());
        trangThaiNhapVao.setNgayCapNhat(new Date());
        trangThaiNhapVao.setMoTa(loaiTrangThai.getMoTa());
        trangThaiHoanTra.setIdNhanVien(idNhanVien); // Gán idNhanVien cho trạng thái hiện tại
        trangThaiHoaDonRepository.save(trangThaiNhapVao);

        if (doiTraList.isEmpty()) {
            return false; // Không tìm thấy đơn đổi trả nào
        }

        for (DoiTra doiTra : doiTraList) {
            doiTra.setNgayCapNhat(new Date());

            if (idLoaiTrangThai == 10) { // Hủy bỏ hoàn trả
                doiTra.setTrangThai(false);
            } else if (idLoaiTrangThai == 11) { // Xác nhận hoàn trả
                doiTra.setTrangThai(true);

                // Cập nhật số lượng sản phẩm
                SanPhamChiTiet sanPhamChiTiet = doiTra.getSanPhamChiTiet();
                if (sanPhamChiTiet != null) {
                    sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + doiTra.getSoLuong());
                    sanPhamChiTiet.setNgayCapNhat(new Date());
                    sanPhamChiTietRepository.save(sanPhamChiTiet);
                }
            }
        }

        doiTraRepository.saveAll(doiTraList);
        return true;
    }

}
