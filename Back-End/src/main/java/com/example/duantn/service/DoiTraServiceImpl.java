package com.example.duantn.service;

import com.example.duantn.dto.DoiTraDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import com.example.duantn.service.DoiTraSevice.DoiTraService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    
                // Thêm kiểm tra và gán lý do
                String lyDo = doiTraDTO.getLyDo() != null && !doiTraDTO.getLyDo().isEmpty()
                        ? doiTraDTO.getLyDo()
                        : "Không có lý do cụ thể";  // Gán lý do mặc định nếu lý do bị bỏ trống
                doiTra.setLyDo(lyDo);
    
                doiTra.setTongTien(doiTraDTO.getTongTien());
                doiTra.setTrangThai(false);
                doiTra.setNgayTao(new Date());
    
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
}
