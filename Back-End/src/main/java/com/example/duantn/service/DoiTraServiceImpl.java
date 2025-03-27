//package com.example.duantn.service;
//
//import com.example.duantn.dto.DoiTraDTO;
//import com.example.duantn.entity.*;
//import com.example.duantn.repository.*;
//
//import com.example.duantn.service.DoiTraSevice.DoiTraService;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class DoiTraServiceImpl implements DoiTraService {
//    private final DoiTraRepository doiTraRepository;
//    private final HoaDonRepository hoaDonRepository;
//    private final SanPhamChiTietRepository sanPhamChiTietRepository;
//    private final TrangThaiHoaDonRepository trangThaiHoaDonRepository;
//    private final LoaiTrangThaiRepository loaiTrangThaiRepository;
//
//    public DoiTraServiceImpl(DoiTraRepository doiTraRepository,
//                             HoaDonRepository hoaDonRepository,
//                             SanPhamChiTietRepository sanPhamChiTietRepository,
//                             TrangThaiHoaDonRepository trangThaiHoaDonRepository,
//                             LoaiTrangThaiRepository loaiTrangThaiRepository) {
//        this.doiTraRepository = doiTraRepository;
//        this.hoaDonRepository = hoaDonRepository;
//        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
//        this.trangThaiHoaDonRepository = trangThaiHoaDonRepository;
//        this.loaiTrangThaiRepository = loaiTrangThaiRepository;
//    }
//
//    @Override
//    public List<DoiTra> createDoiTra(List<DoiTraDTO> doiTraDTOList) {
//        // Duyệt qua danh sách các yêu cầu đổi trả
//        return doiTraDTOList.stream().map(doiTraDTO -> {
//            // Lấy hóa đơn từ database
//            HoaDon hoaDon = hoaDonRepository.findById(doiTraDTO.getIdHoaDon())
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: " + doiTraDTO.getIdHoaDon()));
//
//            // Lấy sản phẩm chi tiết từ database
//            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(doiTraDTO.getIdSanPhamChiTiet())
//                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết với ID: " + doiTraDTO.getIdSanPhamChiTiet()));
//
//            // Kiểm tra số lượng đổi trả hợp lệ
//            if (sanPhamChiTiet.getSoLuong() < doiTraDTO.getSoLuong()) {
//                throw new RuntimeException("Số lượng đổi trả vượt quá số lượng hiện có trong kho cho sản phẩm ID: "
//                        + doiTraDTO.getIdSanPhamChiTiet());
//            }
//
//            // Tạo yêu cầu đổi trả
//            DoiTra doiTra = new DoiTra();
//            doiTra.setHoaDon(hoaDon);
//            doiTra.setSanPhamChiTiet(sanPhamChiTiet);
//            doiTra.setSoLuong(doiTraDTO.getSoLuong());
//            doiTra.setLyDo(doiTraDTO.getLyDo());
//            doiTra.setTongTien(doiTraDTO.getTongTien());
//            doiTra.setTrangThai(true);
//            doiTra.setNgayTao(new Date());
//
//            // Thêm trạng thái "Tạo đơn hàng" và "Chờ xử lý"
//            createTrangThaiHoaDon(hoaDon, 9, "Yêu cầu hoàn trả");
//
//            // Lưu yêu cầu đổi trả và trả kết quả
//            return doiTraRepository.save(doiTra);
//        }).collect(Collectors.toList());
//    }
//
//    // Phương thức tạo trạng thái cho hóa đơn
//    private void createTrangThaiHoaDon(HoaDon hoaDon, int loaiTrangThaiId, String moTa) {
//        LoaiTrangThai loaiTrangThai = loaiTrangThaiRepository.findById(loaiTrangThaiId)
//                .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));
//
//        TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
//        trangThaiHoaDon.setMoTa(moTa);
//        trangThaiHoaDon.setNgayTao(new Date());
//        trangThaiHoaDon.setIdNhanVien(1);  // ID nhân viên giả định, có thể điều chỉnh
//        trangThaiHoaDon.setNgayCapNhat(new Date());
//        trangThaiHoaDon.setLoaiTrangThai(loaiTrangThai);
//        trangThaiHoaDon.setHoaDon(hoaDon);
//        trangThaiHoaDonRepository.save(trangThaiHoaDon);
//    }
//
//    @Override
//    public DoiTra createDoiTra(DoiTraDTO doiTraDTO) {
//        return null;
//    }
//
//    @Override
//    public List<DoiTra> getDoiTraByHoaDonId(Integer idHoaDon) {
//        return doiTraRepository.findByHoaDonIdHoaDon(idHoaDon);
//    }
//}
