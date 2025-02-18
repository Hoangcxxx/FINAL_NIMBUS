package com.example.duantn.service;

import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TrangThaiHoaDonService {

    @Autowired
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;



    // Lấy tất cả trạng thái hóa đơn
    public List<TrangThaiHoaDon> getAllTrangThaiHoaDon() {
        return trangThaiHoaDonRepository.findAll();
    }

    public List<Object[]> getAllTrangThaiHoaDonByidHoaDon(Integer idHoaDon) {
        return trangThaiHoaDonRepository.findAllByidHoaDon(idHoaDon);
    }

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private LoaiTrangThaiRepository loaiTrangThaiRepository;

    // Phương thức để kiểm tra xem trạng thái loại đã có chưa
    public boolean isTrangThaiHoaDonExist(Integer idHoaDon, Integer idLoaiTrangThai) {
        List<TrangThaiHoaDon> existingStatuses = trangThaiHoaDonRepository.findByHoaDon_IdHoaDonAndLoaiTrangThai_IdLoaiTrangThai(idHoaDon, idLoaiTrangThai);
        return !existingStatuses.isEmpty();  // Trả về true nếu trạng thái đã tồn tại
    }

    public List<TrangThaiHoaDon> saveTrangThaiHoaDon(Integer idHoaDon, Integer idLoaiTrangThai, Integer idNhanVien) {
        // Kiểm tra sự tồn tại của hóa đơn và loại trạng thái
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findById(idHoaDon);
        Optional<LoaiTrangThai> loaiTrangThaiOpt = loaiTrangThaiRepository.findById(idLoaiTrangThai);

        // Nếu hóa đơn và loại trạng thái tồn tại
        if (hoaDonOpt.isPresent() && loaiTrangThaiOpt.isPresent()) {
            HoaDon hoaDon = hoaDonOpt.get();
            LoaiTrangThai loaiTrangThai = loaiTrangThaiOpt.get();

            // Kiểm tra trạng thái đã tồn tại hay chưa
            if (isTrangThaiHoaDonExist(idHoaDon, idLoaiTrangThai)) {
                String errorMessage = "Trạng thái đã tồn tại cho hóa đơn này với loại trạng thái ID = " + idLoaiTrangThai;
                System.out.println(errorMessage);

                // Khởi tạo TrangThaiHoaDon và gán thông báo lỗi vào MoTa
                TrangThaiHoaDon errorTrangThai = new TrangThaiHoaDon();
                errorTrangThai.setMoTa(errorMessage);  // Gán thông báo lỗi vào MoTa
                return List.of(errorTrangThai);  // Trả về danh sách chứa thông báo lỗi
            }

            // Kiểm tra trạng thái 'Xác nhận đơn hàng' (idLoaiTrangThai == 3)
            if (idLoaiTrangThai == 3) {
                // Nếu trạng thái 'Chờ xác nhận' (idLoaiTrangThai == 2) không tồn tại, cho phép cập nhật
                boolean isPaymentConfirmed = isTrangThaiHoaDonExist(idHoaDon, 2);
                if (!isPaymentConfirmed) {
                    String errorMessage = "Không thể cập nhật trạng thái 'Xác nhận đơn hàng' vì hóa đơn chưa được 'Chờ xác nhận'.";
                    System.out.println(errorMessage);

                    // Trả về thông báo lỗi
                    TrangThaiHoaDon errorTrangThai = new TrangThaiHoaDon();
                    errorTrangThai.setMoTa(errorMessage);  // Gán thông báo lỗi vào MoTa
                    return List.of(errorTrangThai);  // Trả về danh sách chứa thông báo lỗi
                }

                // Cập nhật số lượng sản phẩm chi tiết
                List<HoaDonChiTiet> hoaDonChiTietList = hoaDon.getHoaDonChiTietList();
                for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
                    SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();
                    Integer soLuongBan = hoaDonChiTiet.getSoLuong();
                    Integer soLuongTon = sanPhamChiTiet.getSoLuong();

                    // Kiểm tra nếu còn đủ số lượng trong kho để trừ đi
                    if (soLuongTon >= soLuongBan) {
                        sanPhamChiTiet.setSoLuong(soLuongTon - soLuongBan); // Trừ số lượng tồn kho
                        sanPhamChiTietRepository.save(sanPhamChiTiet);  // Lưu lại thông tin sản phẩm chi tiết với số lượng mới
                        System.out.println("Đã trừ đi " + soLuongBan + " sản phẩm từ kho.");
                    } else {
                        // Nếu số lượng trong kho không đủ, trả về thông báo lỗi
                        String errorMessage = "Không đủ số lượng trong kho để xác nhận đơn hàng.";
                        TrangThaiHoaDon errorTrangThai = new TrangThaiHoaDon();
                        errorTrangThai.setMoTa(errorMessage);
                        return List.of(errorTrangThai);  // Trả về thông báo lỗi
                    }
                }
            }

            // Kiểm tra các trạng thái khác (4, 5, 6, 7, 8) tương tự
            if (idLoaiTrangThai == 4) {
                boolean isPaymentConfirmed = isTrangThaiHoaDonExist(idHoaDon, 3);
                if (!isPaymentConfirmed) {
                    String errorMessage = "Không thể cập nhật trạng thái 'Chờ giao hàng' vì hóa đơn chưa được 'Xác nhận đơn hàng'.";
                    System.out.println(errorMessage);

                    // Khởi tạo TrangThaiHoaDon và gán thông báo lỗi vào MoTa
                    TrangThaiHoaDon errorTrangThai = new TrangThaiHoaDon();
                    errorTrangThai.setMoTa(errorMessage);
                    return List.of(errorTrangThai);
                }
            }

            if (idLoaiTrangThai == 5) {
                boolean isPaymentConfirmed = isTrangThaiHoaDonExist(idHoaDon, 4);
                if (!isPaymentConfirmed) {
                    String errorMessage = "Không thể cập nhật trạng thái 'Đang giao hàng' vì hóa đơn chưa được 'Chờ giao hàng'.";
                    System.out.println(errorMessage);

                    // Khởi tạo TrangThaiHoaDon và gán thông báo lỗi vào MoTa
                    TrangThaiHoaDon errorTrangThai = new TrangThaiHoaDon();
                    errorTrangThai.setMoTa(errorMessage);
                    return List.of(errorTrangThai);
                }
            }

            if (idLoaiTrangThai == 6) {
                boolean isPaymentConfirmed = isTrangThaiHoaDonExist(idHoaDon, 5);
                if (!isPaymentConfirmed) {
                    String errorMessage = "Không thể cập nhật trạng thái 'Chờ thanh toán' vì hóa đơn chưa được 'Đang giao hàng'.";
                    System.out.println(errorMessage);

                    // Khởi tạo TrangThaiHoaDon và gán thông báo lỗi vào MoTa
                    TrangThaiHoaDon errorTrangThai = new TrangThaiHoaDon();
                    errorTrangThai.setMoTa(errorMessage);
                    return List.of(errorTrangThai);
                }
            }

            if (idLoaiTrangThai == 7) {
                boolean isPaymentConfirmed = isTrangThaiHoaDonExist(idHoaDon, 6);
                if (!isPaymentConfirmed) {
                    String errorMessage = "Không thể cập nhật trạng thái 'Đã thanh toán thành công' vì hóa đơn chưa được 'Chờ thanh toán'.";
                    System.out.println(errorMessage);

                    // Khởi tạo TrangThaiHoaDon và gán thông báo lỗi vào MoTa
                    TrangThaiHoaDon errorTrangThai = new TrangThaiHoaDon();
                    errorTrangThai.setMoTa(errorMessage);
                    return List.of(errorTrangThai);
                }
            }

            if (idLoaiTrangThai == 8) {
                boolean isPaymentConfirmed = isTrangThaiHoaDonExist(idHoaDon, 7);
                if (!isPaymentConfirmed) {
                    String errorMessage = "Không thể cập nhật trạng thái 'Hoàn thành' vì hóa đơn chưa được xác nhận thanh toán.";
                    System.out.println(errorMessage);

                    // Khởi tạo TrangThaiHoaDon và gán thông báo lỗi vào MoTa
                    TrangThaiHoaDon errorTrangThai = new TrangThaiHoaDon();
                    errorTrangThai.setMoTa(errorMessage);
                    return List.of(errorTrangThai);
                }
            }

            // Sau khi các kiểm tra khác đều thành công:
            TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
            trangThaiHoaDon.setHoaDon(hoaDon);
            trangThaiHoaDon.setLoaiTrangThai(loaiTrangThai);
            trangThaiHoaDon.setNgayTao(new Date());
            trangThaiHoaDon.setNgayCapNhat(new Date());
            trangThaiHoaDon.setIdNhanVien(idNhanVien); // Gán idNhanVien cho trạng thái hiện tại
            trangThaiHoaDon.setMoTa(loaiTrangThai.getMoTa());
            // Lưu trạng thái 'Xác nhận đơn hàng' (idLoaiTrangThai == 3)
            if (idLoaiTrangThai == 3) {
                TrangThaiHoaDon savedTrangThai = trangThaiHoaDonRepository.save(trangThaiHoaDon);
                System.out.println("Trạng thái 'Xác nhận đơn hàng' đã được lưu: " + savedTrangThai);

                // Sau khi trạng thái 'Xác nhận đơn hàng' lưu thành công, lưu trạng thái 'Chờ thanh toán' (idLoaiTrangThai == 4)
                LoaiTrangThai loaiTrangThaiThanhToan = loaiTrangThaiRepository.findById(4).orElse(null); // idLoaiTrangThai == 4: Chờ thanh toán
                if (loaiTrangThaiThanhToan != null) {
                    TrangThaiHoaDon trangThaiThanhToan = new TrangThaiHoaDon();
                    trangThaiThanhToan.setHoaDon(hoaDon);
                    trangThaiThanhToan.setLoaiTrangThai(loaiTrangThaiThanhToan);
                    trangThaiThanhToan.setNgayTao(new Date());
                    trangThaiThanhToan.setNgayCapNhat(new Date());
                    trangThaiThanhToan.setIdNhanVien(idNhanVien); // Gán idNhanVien cho trạng thái 'Chờ thanh toán'
                    trangThaiThanhToan.setMoTa(loaiTrangThaiThanhToan.getMoTa());  // Mô tả từ loại trạng thái
                    trangThaiHoaDonRepository.save(trangThaiThanhToan);
                    System.out.println("Trạng thái 'Chờ thanh toán' đã được tạo thêm.");
                }

                return List.of(savedTrangThai);
            }
            // Lưu trạng thái hóa đơn vào database
            TrangThaiHoaDon savedTrangThai = trangThaiHoaDonRepository.save(trangThaiHoaDon);
            System.out.println("Trạng thái hóa đơn đã được lưu: " + savedTrangThai);

            // Kiểm tra nếu trạng thái đã được lưu thành công
            if (savedTrangThai != null) {
                // Trả về kết quả thành công
                return List.of(savedTrangThai);  // Trả về danh sách chứa trạng thái đã lưu
            } else {
                // Trạng thái không lưu được, trả về lỗi
                String errorMessage = "Có lỗi xảy ra khi lưu trạng thái hóa đơn.";
                TrangThaiHoaDon errorTrangThai = new TrangThaiHoaDon();
                errorTrangThai.setMoTa(errorMessage);
                return List.of(errorTrangThai);  // Trả về thông báo lỗi
            }
        }

        // Trường hợp không tìm thấy hóa đơn hoặc loại trạng thái
        String errorMessage = "Không tìm thấy hóa đơn hoặc loại trạng thái.";
        System.out.println(errorMessage);

        // Khởi tạo đối tượng TrangThaiHoaDon để trả về thông báo lỗi
        TrangThaiHoaDon errorTrangThai = new TrangThaiHoaDon();
        errorTrangThai.setMoTa(errorMessage);  // Gán thông báo lỗi vào MoTa
        return List.of(errorTrangThai);  // Trả về danh sách chứa thông báo lỗi
    }


    public void TrangThaiHoaDonKhiChonPTTT(Integer hoaDonId, Integer idNhanVien) {
        // Tiến hành tạo trạng thái hóa đơn mới nếu chưa tồn tại
        HoaDon savedHoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại!"));

        int loaiTrangThaiId = 6;

        // Tạo trạng thái hóa đơn mới
        TrangThaiHoaDon trangThaiHoaDon2 = new TrangThaiHoaDon();
        trangThaiHoaDon2.setMoTa("Chờ Thanh Toán");
        trangThaiHoaDon2.setNgayTao(new Date());
        trangThaiHoaDon2.setNgayCapNhat(new Date());

        // Tìm loại trạng thái theo ID
        LoaiTrangThai loaiTrangThai2 = loaiTrangThaiRepository.findById(loaiTrangThaiId)
                .orElseThrow(() -> new IllegalArgumentException("Loại trạng thái không tồn tại!"));

        trangThaiHoaDon2.setLoaiTrangThai(loaiTrangThai2);
        trangThaiHoaDon2.setHoaDon(savedHoaDon);

        // Lưu idNhanVien vào trạng thái hóa đơn (Giả sử TrangThaiHoaDon có một trường để lưu idNhanVien)
        trangThaiHoaDon2.setIdNhanVien(idNhanVien);

        // Lưu trạng thái hóa đơn vào cơ sở dữ liệu
        trangThaiHoaDonRepository.save(trangThaiHoaDon2);
    }

    public TrangThaiHoaDon createTrangThaiHoaDon(Integer idHoaDon) {
        // Lấy hóa đơn từ cơ sở dữ liệu
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new RuntimeException("Hóa đơn không tồn tại"));

        // Lấy loại trạng thái 5
        LoaiTrangThai loaiTrangThai1 = loaiTrangThaiRepository.findById(5)
                .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));

        // Tạo trạng thái hóa đơn loại 5
        TrangThaiHoaDon trangThaiHoaDon1 = new TrangThaiHoaDon();
        trangThaiHoaDon1.setMoTa("Đã thanh toán thành công");
        trangThaiHoaDon1.setNgayTao(new Date());
        trangThaiHoaDon1.setNgayCapNhat(new Date());
        trangThaiHoaDon1.setLoaiTrangThai(loaiTrangThai1);
        trangThaiHoaDon1.setHoaDon(hoaDon);

        // Lưu trạng thái hóa đơn loại 5
        trangThaiHoaDonRepository.save(trangThaiHoaDon1);

        // Lấy loại trạng thái 8
        LoaiTrangThai loaiTrangThai2 = loaiTrangThaiRepository.findById(8)
                .orElseThrow(() -> new RuntimeException("Loại trạng thái không tồn tại"));

        // Tạo trạng thái hóa đơn loại 8
        TrangThaiHoaDon trangThaiHoaDon2 = new TrangThaiHoaDon();
        trangThaiHoaDon2.setMoTa("Hoàn thành ");
        trangThaiHoaDon2.setNgayTao(new Date());
        trangThaiHoaDon2.setNgayCapNhat(new Date());
        trangThaiHoaDon2.setLoaiTrangThai(loaiTrangThai2);
        trangThaiHoaDon2.setHoaDon(hoaDon);

        // Lưu trạng thái hóa đơn loại 8
        trangThaiHoaDonRepository.save(trangThaiHoaDon2);

        // Trả về trạng thái loại 5 (hoặc bất kỳ trạng thái nào bạn muốn phản hồi)
        return trangThaiHoaDon1;
    }


}