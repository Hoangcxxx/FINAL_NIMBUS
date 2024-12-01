package com.example.duantn.service;

import com.example.duantn.entity.NguoiDung;
import com.example.duantn.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class NguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    // Lấy tất cả người dùng có vai trò id = 2 (ví dụ)
    public List<NguoiDung> getAllNguoiDungsByRoleId() {
        return nguoiDungRepository.getSanPhamById();
    }

    // Lấy người dùng theo ID
    public Optional<NguoiDung> getNguoiDungById(int id) {
        return nguoiDungRepository.findById(id);
    }

    // Thêm mới người dùng
    public ResponseEntity<?> addNguoiDung(NguoiDung nguoiDung) {
        // Kiểm tra nếu email hoặc số điện thoại đã tồn tại trong cơ sở dữ liệu
        if (isEmailExist(nguoiDung.getEmail())) {
            return new ResponseEntity<>("Email đã tồn tại!", HttpStatus.BAD_REQUEST);
        }
        if (isSdtExist(nguoiDung.getSdt())) {
            return new ResponseEntity<>("Số điện thoại đã tồn tại!", HttpStatus.BAD_REQUEST);
        }

        nguoiDung.setNgayTao(new Date());
        nguoiDung.setNgayCapNhat(new Date());

        // Tạo mã người dùng (ma_nguoi_dung) nếu chưa có
        if (nguoiDung.getMaNguoiDung() == null || nguoiDung.getMaNguoiDung().isEmpty()) {
            String generatedMaNguoiDung = generateMaNguoiDung(nguoiDung.getTenNguoiDung()); // Hàm tự tạo mã
            nguoiDung.setMaNguoiDung(generatedMaNguoiDung);
        }

        NguoiDung createdNguoiDung = nguoiDungRepository.save(nguoiDung);
        return new ResponseEntity<>(createdNguoiDung, HttpStatus.CREATED);
    }

    // Kiểm tra xem email đã tồn tại hay chưa
    private boolean isEmailExist(String email) {
        return nguoiDungRepository.findByEmail(email).isPresent();
    }

    // Kiểm tra xem số điện thoại đã tồn tại hay chưa
    private boolean isSdtExist(String sdt) {
        return nguoiDungRepository.findBySdt(sdt).isPresent();
    }

    // Cập nhật người dùng
    public ResponseEntity<?> updateNguoiDung(int id, NguoiDung nguoiDung) {
        // Đảm bảo người dùng tồn tại trước khi cập nhật
        Optional<NguoiDung> existingNguoiDung = nguoiDungRepository.findById(id);
        if (existingNguoiDung.isPresent()) {
            // Kiểm tra nếu email hoặc số điện thoại đã tồn tại
            if (isEmailExist(nguoiDung.getEmail()) && !nguoiDung.getEmail().equals(existingNguoiDung.get().getEmail())) {
                return new ResponseEntity<>("Email đã tồn tại!", HttpStatus.BAD_REQUEST);
            }
            if (isSdtExist(nguoiDung.getSdt()) && !nguoiDung.getSdt().equals(existingNguoiDung.get().getSdt())) {
                return new ResponseEntity<>("Số điện thoại đã tồn tại!", HttpStatus.BAD_REQUEST);
            }

            nguoiDung.setIdNguoiDung(id); // Đảm bảo ID không bị thay đổi

            // Cập nhật ngày giờ của người dùng
            nguoiDung.setNgayCapNhat(new Date()); // Chỉ cần cập nhật ngàyCapNhat

            NguoiDung updatedNguoiDung = nguoiDungRepository.save(nguoiDung);
            return new ResponseEntity<>(updatedNguoiDung, HttpStatus.OK);
        } else {
            // Người dùng không tồn tại
            return new ResponseEntity<>("Người dùng không tồn tại!", HttpStatus.NOT_FOUND);
        }
    }

    // Xóa người dùng
    public ResponseEntity<HttpStatus> deleteNguoiDung(int id) {
        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findById(id);
        if (nguoiDung.isPresent()) {
            nguoiDungRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Hàm tạo mã người dùng tự động
    private String generateMaNguoiDung(String tenNguoiDung) {
        // Tạo mã người dùng đơn giản từ tên (hoặc phương thức tạo mã khác)
        return tenNguoiDung.substring(0, 3).toUpperCase() + System.currentTimeMillis();
    }



    // Lấy thông tin người dùng theo ID
    public NguoiDung getNguoiDungById(Integer id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }

    // Cập nhật thông tin người dùng
    public NguoiDung updateNguoiDung(Integer id, NguoiDung nguoiDungDetails) {
        NguoiDung existingNguoiDung = nguoiDungRepository.findById(id).orElse(null);

        if (existingNguoiDung != null) {
            existingNguoiDung.setTenNguoiDung(nguoiDungDetails.getTenNguoiDung());
            existingNguoiDung.setEmail(nguoiDungDetails.getEmail());
            existingNguoiDung.setSdt(nguoiDungDetails.getSdt());
            existingNguoiDung.setNgaySinh(nguoiDungDetails.getNgaySinh());
            existingNguoiDung.setDiaChi(nguoiDungDetails.getDiaChi());
            existingNguoiDung.setGioiTinh(nguoiDungDetails.getGioiTinh());
            existingNguoiDung.setAnhDaiDien(nguoiDungDetails.getAnhDaiDien());
            existingNguoiDung.setNgayCapNhat(new Date());

            return nguoiDungRepository.save(existingNguoiDung);
        } else {
            return null;
        }
    }
}
