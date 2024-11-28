package com.example.duantn.service;

import com.example.duantn.entity.HoaDon;
import com.example.duantn.entity.LoaiTrangThai;
import com.example.duantn.entity.TrangThaiHoaDon;
import com.example.duantn.repository.HoaDonRepository;
import com.example.duantn.repository.LoaiTrangThaiRepository;
import com.example.duantn.repository.TrangThaiHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private LoaiTrangThaiRepository loaiTrangThaiRepository;

    // Phương thức để kiểm tra xem trạng thái loại đã có chưa
    public boolean isTrangThaiHoaDonExist(Integer idHoaDon, Integer idLoaiTrangThai) {
        List<TrangThaiHoaDon> existingStatuses = trangThaiHoaDonRepository.findByHoaDon_IdHoaDonAndLoaiTrangThai_IdLoaiTrangThai(idHoaDon, idLoaiTrangThai);
        return !existingStatuses.isEmpty();  // Trả về true nếu trạng thái đã tồn tại
    }

    public List<TrangThaiHoaDon> saveTrangThaiHoaDon(Integer idHoaDon, Integer idLoaiTrangThai) {
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
                boolean isPaymentConfirmed = isTrangThaiHoaDonExist(idHoaDon, 5); // Kiểm tra xem trạng thái 'đã xác nhận thanh toán' có tồn tại không
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






}
