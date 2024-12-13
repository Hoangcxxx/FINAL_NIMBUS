//package com.example.duantn.service;
//
//import com.example.duantn.dto.DiaChiVanChuyenDTO;
//import com.example.duantn.entity.*;
//import com.example.duantn.repository.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class DiaChiVanChuyenService {
//
//    @Autowired
//    private DiaChiVanChuyenRepository diaChiVanChuyenRepository;
//
//    @Autowired
//    private TinhRepository tinhRepository;
//
//    @Autowired
//    private HuyenRepository huyenRepository;
//
//    @Autowired
//    private XaRepository xaRepository;
//
//    @Autowired
//    private NguoiDungRepository nguoiDungRepository;
//
//    @Autowired
//    private LoaiThongBaoRepository loaiThongBaoRepository;
//
//    @Autowired
//    private ThongBaoRepository thongBaoRepository;
//
//    // Lấy tất cả địa chỉ vận chuyển
//    public List<DiaChiVanChuyenDTO> getAllDiaChiVanChuyen() {
//        List<DiaChiVanChuyen> addresses = diaChiVanChuyenRepository.findAll();
//        return convertToDTOs(addresses);
//    }
//
//
//    // Lấy địa chỉ theo id
//    public DiaChiVanChuyenDTO getDiaChiById(Integer idDiaChiVanChuyen) {
//        DiaChiVanChuyen address = diaChiVanChuyenRepository.findById(idDiaChiVanChuyen)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
//        return new DiaChiVanChuyenDTO(
//                address.getIdDiaChiVanChuyen(),
//                address.getTinh().getIdTinh(),  // Lấy idTinh từ đối tượng Tinh
//                address.getHuyen().getIdHuyen(),  // Lấy idHuyen từ đối tượng Huyen
//                address.getXa().getIdXa(),  // Lấy idXa từ đối tượng Xa
//                address.getNguoiDung().getIdNguoiDung(),  // Lấy idXa từ đối tượng Xa
//                address.getMoTa()
//        );
//    }
//
//    // Phương thức chuyển đổi danh sách DiaChiVanChuyen thành DiaChiVanChuyenDTO
//    private List<DiaChiVanChuyenDTO> convertToDTOs(List<DiaChiVanChuyen> addresses) {
//        return addresses.stream()
//                .map(address -> new DiaChiVanChuyenDTO(
//                        address.getIdDiaChiVanChuyen(),
//                        address.getTinh().getIdTinh(),  // Truyền idTinh thay vì đối tượng Tinh
//                        address.getHuyen().getIdHuyen(),  // Truyền idHuyen thay vì đối tượng Huyen
//                        address.getXa().getIdXa(),  // Truyền idXa thay vì đối tượng Xa
//                        address.getNguoiDung().getIdNguoiDung(),  // Lấy idXa từ đối tượng Xa
//                        address.getMoTa()
//                ))
//                .collect(Collectors.toList());
//    }
//
//    public DiaChiVanChuyen addDiaChiVanChuyen(Integer idTinh, Integer idHuyen, Integer idXa, String diaChiCuThe, String moTa, Integer idNguoiDung) {
//        // Lấy Tinh, Huyen, Xa từ cơ sở dữ liệu
//        Optional<Tinh> tinhOptional = tinhRepository.findById(idTinh);
//        Optional<Huyen> huyenOptional = huyenRepository.findById(idHuyen);
//        Optional<Xa> xaOptional = xaRepository.findById(idXa);
//
//        if (!tinhOptional.isPresent() || !huyenOptional.isPresent() || !xaOptional.isPresent()) {
//            throw new IllegalArgumentException("Thông tin địa lý không hợp lệ");
//        }
//
//        Tinh tinh = tinhOptional.get();
//        Huyen huyen = huyenOptional.get();
//        Xa xa = xaOptional.get();
//
//        // Tạo địa chỉ vận chuyển mới
//        DiaChiVanChuyen diaChiVanChuyen = new DiaChiVanChuyen();
//        diaChiVanChuyen.setTinh(tinh);
//        diaChiVanChuyen.setHuyen(huyen);
//        diaChiVanChuyen.setXa(xa);
//        diaChiVanChuyen.setDiaChiCuThe(diaChiCuThe);
//        diaChiVanChuyen.setMoTa(moTa);
//        diaChiVanChuyen.setNgayTao(new Date());
//        diaChiVanChuyen.setNgayCapNhat(new Date());
//        diaChiVanChuyen.setTrangThai(true);  // Mặc định trạng thái là true
//
//        // Thiết lập người dùng
//        NguoiDung nguoiDung = nguoiDungRepository.findById(idNguoiDung)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + idNguoiDung));
//        diaChiVanChuyen.setNguoiDung(nguoiDung);
//
//        // Lưu địa chỉ vận chuyển
//        diaChiVanChuyen = diaChiVanChuyenRepository.save(diaChiVanChuyen);
//
//        // Tạo thông báo
//        String message = "Bạn đã thêm địa chỉ vận chuyển mới tại: " + diaChiCuThe + " (" + tinh.getTenTinh() + " - " + huyen.getTenHuyen() + " - " + xa.getTenXa() + ")";
//
//        LoaiThongBao loaiThongBao = loaiThongBaoRepository.findById(21)
//                .orElseThrow(() -> new RuntimeException("Loại thông báo không tồn tại"));
//
//        ThongBao thongBao = new ThongBao();
//        thongBao.setNguoiDung(nguoiDung);
//        thongBao.setLoaiThongBao(loaiThongBao);
//        thongBao.setNoiDung(message);
//        thongBao.setTrangThai(true);
//        thongBao.setNgayGui(new Date());
//
//        thongBaoRepository.save(thongBao);
//
//        return diaChiVanChuyen;
//    }
//
//    // Lấy danh sách tất cả các tỉnh
//    public List<Tinh> getAllTinh() {
//        return tinhRepository.findAll();
//    }
//
//    // Lấy danh sách các huyện theo tỉnh
//    public List<Huyen> getHuyenByTinh(Integer idTinh) {
//        return huyenRepository.findByTinh_IdTinh(idTinh);
//    }
//
//    // Lấy danh sách các xã theo huyện
//    public List<Xa> getXaByHuyen(Integer idHuyen) {
//        return xaRepository.findByHuyen_IdHuyen(idHuyen);
//    }
//
//    public List<DiaChiVanChuyen> getDiaChiByNguoiDung(Integer idNguoiDung) {
//        return diaChiVanChuyenRepository.findDiaChiByNguoiDungId(idNguoiDung);
//    }
//}
