package com.example.duantn.service;

import com.example.duantn.dto.NguoiDungDTO;
import com.example.duantn.dto.TimKiemNguoiDungDTO;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<NguoiDung> getAllNhanVienBanHang() {
        return nguoiDungRepository.findByVaiTro_IdVaiTro(3);
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
    public ResponseEntity<HttpStatus> deleteNguoiDung(Integer idNguoiDung) {
        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findById(idNguoiDung);
        if (nguoiDung.isPresent()) {
            nguoiDungRepository.deleteById(idNguoiDung);
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
    public NguoiDung addNguoiDung1(NguoiDung nguoiDung) {
        nguoiDung.setMaNguoiDung("user" + System.currentTimeMillis());
        return nguoiDungRepository.save(nguoiDung);
    }
    public NguoiDung findById(Integer idNguoiDung) {
        return nguoiDungRepository.findById(idNguoiDung).orElse(null);
    }


    public List<TimKiemNguoiDungDTO> searchKhachHangByPhone(String phonePrefix) {
        String searchPattern = phonePrefix + "%"; // Tạo pattern tìm kiếm
        List<Object[]> results = nguoiDungRepository.getSearchKhachHang(searchPattern);

        // Ánh xạ Object[] thành TimKiemNguoiDungDTO
        List<TimKiemNguoiDungDTO> nguoiDungDTOList = new ArrayList<>();
        for (Object[] result : results) {
            Integer idNguoiDung = (Integer) result[0];  // Lấy giá trị id_nguoi_dung
            String tenNguoiDung = (String) result[1];   // Lấy giá trị ten_nguoi_dung
            String sdt = (String) result[2];           // Lấy giá trị sdt

            TimKiemNguoiDungDTO dto = new TimKiemNguoiDungDTO(idNguoiDung, tenNguoiDung, sdt);
            nguoiDungDTOList.add(dto);
        }

        return nguoiDungDTOList;
    }
    public boolean isPhoneDuplicate(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) {
            return false; // Số điện thoại rỗng không được tính là trùng
        }
        return nguoiDungRepository.existsBySdt(sdt.trim());
    }
    public List<NguoiDung> searchNguoiDung(String tenNguoiDung, String email, String sdt) {
        return nguoiDungRepository.searchNguoiDung(tenNguoiDung, email, sdt);
    }


    public List<NguoiDungDTO> getAllNguoiDung() {
        List<NguoiDung> nguoiDungList = nguoiDungRepository.findAll();

        return nguoiDungList.stream()
                .filter(nguoiDung -> {
                    // Kiểm tra null cho vaiTro trước khi sử dụng
                    if (nguoiDung.getVaiTro() == null) {
                        return false; // Loại bỏ người dùng nếu vaiTrò là null
                    }
                    Integer vaiTroId = nguoiDung.getVaiTro().getIdVaiTro();
                    return vaiTroId != null && vaiTroId == 2; // Điều kiện kiểm tra vai trò
                })
                .map(nguoiDung -> {
                    NguoiDungDTO dto = new NguoiDungDTO();
                    dto.setIdNguoiDung(nguoiDung.getIdNguoiDung());
                    dto.setTenNguoiDung(nguoiDung.getTenNguoiDung());
                    dto.setEmail(nguoiDung.getEmail());
                    dto.setSdt(nguoiDung.getSdt());
                    dto.setNgayTao(nguoiDung.getNgayTao());
                    dto.setTrangThai(nguoiDung.getTrangThai());
                    dto.setGioiTinh(nguoiDung.getGioiTinh());
                    // Kiểm tra null trước khi lấy tên vai trò
                    String tenVaiTro = (nguoiDung.getVaiTro() != null) ? nguoiDung.getVaiTro().getTen() : "Không xác định";
                    dto.setTenVaiTro(tenVaiTro);
                    return dto;
                })
                .collect(Collectors.toList());
    }



    // Lấy danh sách tất cả nhân viên và chuyển thành DTO
    public List<NguoiDungDTO> getAllNhanvien() {
        List<NguoiDung> nguoiDungList = nguoiDungRepository.findAll();

        return nguoiDungList.stream()
                .filter(nguoiDung -> {
                    // Kiểm tra null trước khi truy cập vaiTro và idVaiTro
                    if (nguoiDung.getVaiTro() == null) {
                        return false; // Loại bỏ nếu vaiTro là null
                    }
                    Integer vaiTroId = nguoiDung.getVaiTro().getIdVaiTro();
                    return vaiTroId != null && vaiTroId == 4; // Lọc vai trò là 4 (nhân viên)
                })
                .map(nguoiDung -> {
                    NguoiDungDTO dto = new NguoiDungDTO();
                    dto.setIdNguoiDung(nguoiDung.getIdNguoiDung());
                    dto.setTenNguoiDung(nguoiDung.getTenNguoiDung());
                    dto.setEmail(nguoiDung.getEmail());
                    dto.setSdt(nguoiDung.getSdt());
                    dto.setNgayTao(nguoiDung.getNgayTao());
                    dto.setTrangThai(nguoiDung.getTrangThai());
                    dto.setGioiTinh(nguoiDung.getGioiTinh());
                    // Kiểm tra null trước khi lấy tên vai trò
                    String tenVaiTro = (nguoiDung.getVaiTro() != null) ? nguoiDung.getVaiTro().getTen() : "Không xác định";
                    dto.setTenVaiTro(tenVaiTro);
                    return dto;
                })
                .collect(Collectors.toList());
    }
    // Phương thức khóa người dùng
    public void khoaNguoiDung(Integer idNguoiDung) {
        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findById(idNguoiDung);
        if (nguoiDung.isPresent()) {
            NguoiDung user = nguoiDung.get();
            user.setTrangThai(false);  // Thiết lập trạng thái khóa (false)
            nguoiDungRepository.save(user);  // Lưu lại thay đổi
        } else {
            throw new RuntimeException("Người dùng không tồn tại.");
        }
    }

    // Phương thức mở khóa người dùng
    public void moKhoaNguoiDung(Integer idNguoiDung) {
        Optional<NguoiDung> nguoiDung = nguoiDungRepository.findById(idNguoiDung);
        if (nguoiDung.isPresent()) {
            NguoiDung user = nguoiDung.get();
            user.setTrangThai(true);  // Thiết lập trạng thái mở khóa (true)
            nguoiDungRepository.save(user);  // Lưu lại thay đổi
        } else {
            throw new RuntimeException("Người dùng không tồn tại.");
        }
    }
    public List<NguoiDungDTO> getAllkhachhangle() {
        List<NguoiDung> nguoiDungList = nguoiDungRepository.findAll();

        return nguoiDungList.stream()
                .filter(nguoiDung -> {
                    // Kiểm tra null trước khi truy cập vaiTro và idVaiTro
                    if (nguoiDung.getVaiTro() == null) {
                        return false; // Loại bỏ nếu vaiTro là null
                    }
                    Integer vaiTroId = nguoiDung.getVaiTro().getIdVaiTro();
                    return vaiTroId != null && vaiTroId == 3; // Lọc vai trò là 4 (nhân viên)
                })
                .map(nguoiDung -> {
                    NguoiDungDTO dto = new NguoiDungDTO();
                    dto.setIdNguoiDung(nguoiDung.getIdNguoiDung());
                    dto.setTenNguoiDung(nguoiDung.getTenNguoiDung());
                    dto.setSdt(nguoiDung.getSdt());
                    dto.setNgayTao(nguoiDung.getNgayTao());
                    // Kiểm tra null trước khi lấy tên vai trò
                    String tenVaiTro = (nguoiDung.getVaiTro() != null) ? nguoiDung.getVaiTro().getTen() : "Không xác định";
                    dto.setTenVaiTro(tenVaiTro);
                    return dto;
                })
                .collect(Collectors.toList());
    }


}
