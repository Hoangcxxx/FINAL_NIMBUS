package com.example.duantn.service;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.LoaiTrangThai;
import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.LoaiTrangThaiRepository;
import com.example.duantn.repository.TrangThaiHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TrangThaiHoaDonService {

    @Autowired
    private TrangThaiHoaDonRepository trangThaiHoaDonRepository;

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
        Optional<HoaDon> hoaDonOpt = hoaDonRepository.findById(idHoaDon);
        Optional<LoaiTrangThai> loaiTrangThaiOpt = loaiTrangThaiRepository.findById(idLoaiTrangThai);

        if (hoaDonOpt.isPresent() && loaiTrangThaiOpt.isPresent()) {
            HoaDon hoaDon = hoaDonOpt.get();
            LoaiTrangThai loaiTrangThai = loaiTrangThaiOpt.get();

            // Kiểm tra xem trạng thái loại đã tồn tại chưa
            if (isTrangThaiHoaDonExist(idHoaDon, idLoaiTrangThai)) {
                System.out.println("Trạng thái đã tồn tại cho hóa đơn này với loại trạng thái ID = " + idLoaiTrangThai);
                return null;  // Nếu trạng thái đã tồn tại, không tiếp tục thực hiện lưu và trả về null
            }

            // Kiểm tra nếu trạng thái là 'Hoàn thành' (idLoaiTrangThai == 8)
            if (idLoaiTrangThai == 8) {
                // Kiểm tra xem hóa đơn có đang ở trạng thái 'Chờ thanh toán' (idLoaiTrangThai == 4) hay không
                boolean isPaymentConfirmed = isTrangThaiHoaDonExist(idHoaDon, 7); // Kiểm tra xem trạng thái 'đã xác nhận thanh toán' có tồn tại không
                if (!isPaymentConfirmed) {
                    System.out.println("Không thể cập nhật trạng thái 'Hoàn thành' vì hóa đơn chưa xác nhận thanh toán.");
                    return null; // Nếu hóa đơn chưa thanh toán, không cho phép cập nhật trạng thái hoàn thành
                }
            }

            // Tạo đối tượng TrangThaiHoaDon mới
            TrangThaiHoaDon trangThaiHoaDon = new TrangThaiHoaDon();
            trangThaiHoaDon.setHoaDon(hoaDon);
            trangThaiHoaDon.setLoaiTrangThai(loaiTrangThai);
            trangThaiHoaDon.setNgayTao(new Date());
            trangThaiHoaDon.setNgayCapNhat(new Date());
            trangThaiHoaDon.setIdNhanVien(idNhanVien);
            trangThaiHoaDon.setMoTa(loaiTrangThai.getMoTa());

            // Lưu trạng thái hóa đơn vào database
            TrangThaiHoaDon savedTrangThai = trangThaiHoaDonRepository.save(trangThaiHoaDon);
            System.out.println("Trạng thái hóa đơn đã được lưu: " + savedTrangThai);

            // Kiểm tra idLoaiTrangThai và thêm trạng thái liên quan (4 & 6)
            if (idLoaiTrangThai == 3) {
                // Nếu trạng thái là 3, thêm trạng thái 4
                LoaiTrangThai loaiTrangThaiThanhToan = loaiTrangThaiRepository.findById(4).orElse(null); // idLoaiTrangThai == 4: Chờ thanh toán
                if (loaiTrangThaiThanhToan != null) {
                    TrangThaiHoaDon trangThaiThanhToan = new TrangThaiHoaDon();
                    trangThaiThanhToan.setHoaDon(hoaDon);
                    trangThaiThanhToan.setLoaiTrangThai(loaiTrangThaiThanhToan);
                    trangThaiThanhToan.setNgayTao(new Date());
                    trangThaiThanhToan.setNgayCapNhat(new Date());
                    trangThaiHoaDon.setIdNhanVien(idNhanVien);

                    trangThaiThanhToan.setMoTa(loaiTrangThaiThanhToan.getMoTa());  // Mô tả từ loại trạng thái
                    trangThaiHoaDonRepository.save(trangThaiThanhToan);
                    System.out.println("Trạng thái 'Chờ thanh toán' đã được tạo thêm.");
                }
            } else if (idLoaiTrangThai == 5) {
                // Nếu trạng thái là 5, thêm trạng thái 6
                LoaiTrangThai loaiTrangThaiHoanTat = loaiTrangThaiRepository.findById(6).orElse(null); // idLoaiTrangThai == 6: Hoàn tất
                if (loaiTrangThaiHoanTat != null) {
                    TrangThaiHoaDon trangThaiHoanTat = new TrangThaiHoaDon();
                    trangThaiHoanTat.setHoaDon(hoaDon);
                    trangThaiHoanTat.setLoaiTrangThai(loaiTrangThaiHoanTat);
                    trangThaiHoanTat.setNgayTao(new Date());
                    trangThaiHoanTat.setNgayCapNhat(new Date());
                    trangThaiHoaDon.setIdNhanVien(idNhanVien);
                    trangThaiHoanTat.setMoTa(loaiTrangThaiHoanTat.getMoTa());  // Mô tả từ loại trạng thái
                    trangThaiHoaDonRepository.save(trangThaiHoanTat);
                    System.out.println("Trạng thái 'Hoàn tất' đã được tạo thêm.");
                }
            }

            return List.of(savedTrangThai);  // Trả về danh sách chứa trạng thái đã lưu
        }

        System.out.println("Không tìm thấy hóa đơn hoặc loại trạng thái.");
        return null;
    }

    public void TrangThaiHoaDonKhiChonPTTT(Integer hoaDonId) {
        // Tiến hành tạo trạng thái hóa đơn mới nếu chưa tồn tại
        HoaDon savedHoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại!"));

        // Mặc định loại trạng thái là 4
        int loaiTrangThaiId = 4;

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
