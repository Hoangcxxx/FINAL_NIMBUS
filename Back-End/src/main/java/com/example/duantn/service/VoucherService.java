package com.example.duantn.service;

import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.security.SecureRandom;
import java.util.stream.Collectors;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherNguoiDungRepository voucherNguoiDungRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private TrangThaiGiamGiaRepository trangThaiGiamGiaRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int VOUCHER_CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    // Phương thức để lấy danh sách voucher sắp xếp theo ngày tạo
    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll(Sort.by(Sort.Order.desc("ngayTao")));
    }
    // Tìm kiếm voucher theo mã voucher
    // Tìm kiếm voucher theo mã voucher
    public List<Voucher> searchByMaVoucher(String maVoucher) {
        return voucherRepository.findByMaVoucherContaining(maVoucher);
    }

    // Tìm kiếm voucher theo trạng thái giảm giá
    public List<Voucher> searchByTrangThaiGiamGia(TrangThaiGiamGia trangThaiGiamGia) {
        return voucherRepository.findByTrangThaiGiamGia(trangThaiGiamGia);
    }

    // Tìm kiếm voucher theo kiểu giảm giá
    public List<Voucher> findByKieuGiamGia(Boolean kieuGiamGia) {
        return voucherRepository.findByKieuGiamGia(kieuGiamGia);
    }

    // Tìm kiếm theo các điều kiện mã voucher, trạng thái và kiểu giảm giá
    public List<Voucher> searchVouchers(String maVoucher, Integer trangThaiId, Boolean kieuGiamGia) {
        // Nếu không có điều kiện nào, trả về tất cả voucher
        if (maVoucher == null && trangThaiId == null && kieuGiamGia == null) {
            return voucherRepository.findAll();
        }

        // Lấy đối tượng TrangThaiGiamGia nếu có
        TrangThaiGiamGia trangThaiGiamGia = null;
        if (trangThaiId != null) {
            trangThaiGiamGia = new TrangThaiGiamGia();
            trangThaiGiamGia.setIdTrangThaiGiamGia(trangThaiId);
        }

        // Thực hiện tìm kiếm kết hợp điều kiện
        if (maVoucher != null && trangThaiGiamGia != null && kieuGiamGia != null) {
            return voucherRepository.findByMaVoucherContainingAndTrangThaiGiamGiaAndKieuGiamGia(
                    maVoucher, trangThaiGiamGia, kieuGiamGia);
        } else if (maVoucher != null && trangThaiGiamGia != null) {
            return voucherRepository.findByMaVoucherContainingAndTrangThaiGiamGia(maVoucher, trangThaiGiamGia);
        } else if (maVoucher != null && kieuGiamGia != null) {
            return voucherRepository.findByMaVoucherContainingAndKieuGiamGia(maVoucher, kieuGiamGia);
        } else if (trangThaiGiamGia != null && kieuGiamGia != null) {
            return voucherRepository.findByTrangThaiGiamGiaAndKieuGiamGia(trangThaiGiamGia, kieuGiamGia);
        } else if (maVoucher != null) {
            return voucherRepository.findByMaVoucherContaining(maVoucher);
        } else if (trangThaiGiamGia != null) {
            return voucherRepository.findByTrangThaiGiamGia(trangThaiGiamGia);
        } else {
            return voucherRepository.findByKieuGiamGia(kieuGiamGia);
        }
    }
    public List<Voucher> searchByTenVoucher(String tenVoucher) {
        return voucherRepository.findByTenVoucherContaining(tenVoucher);
    }

    public Optional<Voucher> getVoucherById(Integer id) {
        return voucherRepository.findById(id);
    }

    public Voucher addVoucher(Voucher voucher) {
        if (voucher.getMaVoucher() == null || voucher.getMaVoucher().isEmpty()) {
            voucher.setMaVoucher(generateRandomVoucherCode());
        }
        voucher.setSoTienToiThieu(voucher.getSoTienToiThieu());
        voucher.setGiaTriToiDa(voucher.getGiaTriToiDa());
        validateVoucherDates(voucher);
        setVoucherStatus(voucher);
        try {
            return voucherRepository.save(voucher);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lưu voucher: " + e.getMessage());
        }
    }


    public Voucher updateVoucher(Integer id, Voucher voucherDetails) {
        Optional<Voucher> optionalVoucher = voucherRepository.findById(id);
        if (optionalVoucher.isPresent()) {
            Voucher voucher = optionalVoucher.get();
            voucher.setMaVoucher(voucherDetails.getMaVoucher());
            voucher.setTenVoucher(voucherDetails.getTenVoucher());
            voucher.setGiaTriGiamGia(voucherDetails.getGiaTriGiamGia());
            voucher.setKieuGiamGia(voucherDetails.getKieuGiamGia());
            voucher.setSoLuong(voucherDetails.getSoLuong());
            voucher.setGiaTriToiDa(voucherDetails.getGiaTriToiDa());
            voucher.setSoTienToiThieu(voucherDetails.getSoTienToiThieu());
            voucher.setMoTa(voucherDetails.getMoTa());
            voucher.setNgayBatDau(voucherDetails.getNgayBatDau());
            voucher.setNgayKetThuc(voucherDetails.getNgayKetThuc());

            validateVoucherDates(voucher);
            setVoucherStatus(voucher);
            return voucherRepository.save(voucher);
        }
        return null;
    }

    private void validateVoucherDates(Voucher voucher) {
        if (voucher.getNgayBatDau() != null && voucher.getNgayKetThuc() != null) {
            if (voucher.getNgayBatDau().after(voucher.getNgayKetThuc())) {
                throw new IllegalArgumentException("Ngày bắt đầu không thể sau ngày kết thúc!");
            }
        }
    }

    public void deleteVoucher(Integer idVoucher) {
        Optional<Voucher> optionalVoucher = voucherRepository.findById(idVoucher);
        if (optionalVoucher.isPresent()) {
            Optional<TrangThaiGiamGia> optionalDeletedStatus = trangThaiGiamGiaRepository.findById(5);
            if (optionalDeletedStatus.isPresent()) {
                TrangThaiGiamGia deletedStatus = optionalDeletedStatus.get();
                Voucher voucher = optionalVoucher.get();
                voucher.setTrangThaiGiamGia(deletedStatus);
                voucherRepository.save(voucher);
            } else {
                throw new IllegalArgumentException("Trạng thái giảm giá không tồn tại!");
            }
        } else {
            throw new IllegalArgumentException("Voucher không tồn tại!");
        }
    }

    private void setVoucherStatus(Voucher voucher) {
        Date currentDate = new Date();
        if (voucher.getNgayBatDau().after(currentDate)) {
            TrangThaiGiamGia status = trangThaiGiamGiaRepository.findById(4).orElseThrow(() ->
                    new IllegalArgumentException("Trạng thái giảm giá không tồn tại!"));
            voucher.setTrangThaiGiamGia(status);
        } else if (voucher.getNgayKetThuc().before(currentDate)) {
            TrangThaiGiamGia status = trangThaiGiamGiaRepository.findById(3).orElseThrow(() ->
                    new IllegalArgumentException("Trạng thái giảm giá không tồn tại!"));
            voucher.setTrangThaiGiamGia(status);
        } else {
            TrangThaiGiamGia status = trangThaiGiamGiaRepository.findById(1).orElseThrow(() ->
                    new IllegalArgumentException("Trạng thái giảm giá không tồn tại!"));
            voucher.setTrangThaiGiamGia(status);
        }
    }

    public Voucher cancelVoucher(Integer id) {
        Optional<Voucher> optionalVoucher = voucherRepository.findById(id);
        if (optionalVoucher.isPresent()) {
            TrangThaiGiamGia cancelledStatus = trangThaiGiamGiaRepository.findById(5).orElseThrow(() ->
                    new IllegalArgumentException("Trạng thái giảm giá không tồn tại!"));
            Voucher voucher = optionalVoucher.get();
            voucher.setTrangThaiGiamGia(cancelledStatus);
            return voucherRepository.save(voucher);
        }
        throw new IllegalArgumentException("Voucher không tồn tại!");
    }

    private String generateRandomVoucherCode() {
        StringBuilder voucherCode = new StringBuilder(VOUCHER_CODE_LENGTH);
        for (int i = 0; i < VOUCHER_CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            voucherCode.append(CHARACTERS.charAt(index));
        }
        return voucherCode.toString();
    }

    public List<Voucher> getAllVouchersWithStatus(BigDecimal tongTien, Integer idNguoiDung) {
        List<Voucher> allVouchers = voucherRepository.findAll();
        Date currentDate = new Date();

        return allVouchers.stream()
                .peek(voucher -> {
                    // Kiểm tra trạng thái giảm giá
                    boolean isUsable = false;
                    if (voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() == 1) {
                        isUsable = true;
                    } else {
                        if (voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() == 3) {
                            isUsable = false;
                        } else {
                            boolean trongThoiGianSuDung = !voucher.getNgayBatDau().after(currentDate) &&
                                    !voucher.getNgayKetThuc().before(currentDate);
                            boolean conSoLuong = voucher.getSoLuong() > 0;
                            boolean tongTienDatMin = voucher.getSoTienToiThieu().compareTo(tongTien) <= 0;

                            if (Boolean.TRUE.equals(voucher.getKieuGiamGia())) {
                                isUsable = trongThoiGianSuDung && conSoLuong && tongTienDatMin;
                            } else {
                                boolean tongTienTrongToiDa = voucher.getGiaTriToiDa() == null ||
                                        voucher.getGiaTriToiDa().compareTo(tongTien) >= 0;
                                isUsable = trongThoiGianSuDung && conSoLuong && tongTienDatMin && tongTienTrongToiDa;
                            }
                        }
                    }
                    voucher.setIsUsable(isUsable);

                    // Kiểm tra xem voucher đã được sử dụng bởi người dùng hay chưa
                    boolean isVoucherUsed = voucherNguoiDungRepository.existsByIdVoucherAndIdNguoiDung(
                            voucher.getIdVoucher(), idNguoiDung);

                    // Gắn trạng thái kiểm tra sử dụng
                    voucher.setKiemtravoucher(isVoucherUsed);
                })
                .filter(voucher -> voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() >= 1 &&
                        voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() <= 4 &&
                        voucher.getSoLuong() > 0) // Lọc các voucher có số lượng > 0
                .sorted((voucher1, voucher2) -> {
                    // Sắp xếp theo trạng thái giảm giá (voucher1 là ưu tiên trước)
                    int compareTrangThai = Integer.compare(
                            voucher1.getTrangThaiGiamGia().getIdTrangThaiGiamGia(),
                            voucher2.getTrangThaiGiamGia().getIdTrangThaiGiamGia());

                    if (compareTrangThai == 0) {
                        // Nếu cùng trạng thái giảm giá, sắp xếp theo giá trị giảm giá
                        BigDecimal giamGia1 = Boolean.TRUE.equals(voucher1.getKieuGiamGia()) ?
                                BigDecimal.ZERO : voucher1.getGiaTriGiamGia();
                        BigDecimal giamGia2 = Boolean.TRUE.equals(voucher2.getKieuGiamGia()) ?
                                BigDecimal.ZERO : voucher2.getGiaTriGiamGia();
                        return giamGia2.compareTo(giamGia1); // Sắp xếp từ cao đến thấp
                    }

                    // Nếu voucher có trạng thái giảm giá là 1 thì sẽ ưu tiên trước
                    if (voucher1.getTrangThaiGiamGia().getIdTrangThaiGiamGia() == 1 &&
                            voucher2.getTrangThaiGiamGia().getIdTrangThaiGiamGia() != 1) {
                        return 1; // voucher1 ưu tiên
                    }
                    if (voucher1.getTrangThaiGiamGia().getIdTrangThaiGiamGia() != 1 &&
                            voucher2.getTrangThaiGiamGia().getIdTrangThaiGiamGia() == 1) {
                        return 1; // voucher2 ưu tiên
                    }

                    return compareTrangThai;
                })
                .collect(Collectors.toList());
    }
    public Voucher apdungvoucher(String maVoucher, BigDecimal tongTien,Integer idNguoiDung) {
        // Lấy voucher từ repository
        Voucher voucher = voucherRepository.findByMaVoucher(maVoucher);

        // Kiểm tra nếu voucher không tồn tại
        if (voucher == null) {
            throw new IllegalArgumentException("Voucher không tồn tại.");
        }
        if (voucher.getTrangThaiGiamGia() == null || voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() != 1) {
            throw new IllegalArgumentException("Voucher không thể sử dụng vì không đang phát hành.");
        }
        // Kiểm tra số lượng
        if (voucher.getSoLuong() <= 0) {
            throw new IllegalStateException("Voucher đã hết số lượng.");
        }

        // Kiểm tra điều kiện ngày (voucher chưa hết hạn và chưa bắt đầu)
        if (voucher.getNgayKetThuc() != null && voucher.getNgayKetThuc().before(new java.util.Date())) {
            throw new IllegalArgumentException("Voucher đã hết hạn.");
        }

        if (voucher.getNgayBatDau() != null && voucher.getNgayBatDau().after(new java.util.Date())) {
            throw new IllegalArgumentException("Voucher chưa bắt đầu.");
        }

        System.out.println("Tổng tiền: " + tongTien);
        System.out.println("Số tiền tối thiểu của voucher: " + voucher.getSoTienToiThieu());
        System.out.println("Số tiền tối đa của voucher: " + voucher.getGiaTriToiDa());

        if (tongTien.compareTo(voucher.getSoTienToiThieu()) < 0) {
            throw new IllegalArgumentException("Tổng tiền không đạt mức tối thiểu để áp dụng voucher.");
        }

        if (voucher.getGiaTriToiDa() != null && tongTien.compareTo(voucher.getGiaTriToiDa()) > 0) {
            throw new IllegalArgumentException("Tổng tiền vượt quá mức tối đa để áp dụng voucher.");
        }
        NguoiDung nguoiDung = nguoiDungRepository.findById(idNguoiDung)
                .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));
        VoucherNguoiDung voucherNguoiDung = new VoucherNguoiDung();
        voucherNguoiDung.setVoucher(voucher);
        voucherNguoiDung.setNguoiDung(nguoiDung);

        voucherNguoiDungRepository.save(voucherNguoiDung);

        // Cập nhật số lượng voucher
        voucher.setSoLuong(voucher.getSoLuong() - 1);
        voucherRepository.save(voucher);

        return voucher;
    }


    public Voucher useVoucher(String maVoucher, BigDecimal tongTien) {
        // Lấy voucher từ repository
        Voucher voucher = voucherRepository.findByMaVoucher(maVoucher);

        // Kiểm tra nếu voucher không tồn tại
        if (voucher == null) {
            throw new IllegalArgumentException("Voucher không tồn tại.");
        }
        if (voucher.getTrangThaiGiamGia() == null || voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() != 1) {
            throw new IllegalArgumentException("Voucher không thể sử dụng vì không đang phát hành.");
        }
        // Kiểm tra số lượng
        if (voucher.getSoLuong() <= 0) {
            throw new IllegalStateException("Voucher đã hết số lượng.");
        }

        // Kiểm tra loại voucher, chỉ áp dụng loại 1 hoặc loại 2
        if (voucher.getKieuGiamGia() == null ||
                (voucher.getKieuGiamGia() != true && voucher.getKieuGiamGia() != false)) {
            throw new IllegalArgumentException("Voucher không hợp lệ. Chỉ áp dụng voucher kiểu giảm giá 0 hoặc 1");
        }

        // Kiểm tra điều kiện ngày (voucher chưa hết hạn và chưa bắt đầu)
        if (voucher.getNgayKetThuc() != null && voucher.getNgayKetThuc().before(new java.util.Date())) {
            throw new IllegalArgumentException("Voucher đã hết hạn.");
        }

        if (voucher.getNgayBatDau() != null && voucher.getNgayBatDau().after(new java.util.Date())) {
            throw new IllegalArgumentException("Voucher chưa bắt đầu.");
        }

        System.out.println("Tổng tiền: " + tongTien);
        System.out.println("Số tiền tối thiểu của voucher: " + voucher.getSoTienToiThieu());
        System.out.println("Số tiền tối đa của voucher: " + voucher.getGiaTriToiDa());

        if (tongTien.compareTo(voucher.getSoTienToiThieu()) < 0) {
            throw new IllegalArgumentException("Tổng tiền không đạt mức tối thiểu để áp dụng voucher.");
        }

        if (voucher.getGiaTriToiDa() != null && tongTien.compareTo(voucher.getGiaTriToiDa()) > 0) {
            throw new IllegalArgumentException("Tổng tiền vượt quá mức tối đa để áp dụng voucher.");
        }

        // Nếu tất cả điều kiện đều thỏa mãn, giảm số lượng voucher
        voucher.setSoLuong(voucher.getSoLuong() - 1);
        voucherRepository.save(voucher);

        return voucher;
    }
    public List<Voucher> getValidVouchers(BigDecimal tongTien) {
        return voucherRepository.findAll()
                .stream()
                .filter(voucher ->
                        voucher.getSoLuong() > 0 &&
                                (voucher.getKieuGiamGia() == true ||
                                        voucher.getKieuGiamGia() == false) &&
                                voucher.getSoTienToiThieu().compareTo(tongTien) <= 0 &&

                                (voucher.getGiaTriToiDa() == null || voucher.getGiaTriToiDa().compareTo(tongTien) >= 0) &&
                                // Kiểm tra voucher vẫn còn hiệu lực (chưa hết hạn)
                                !voucher.getNgayKetThuc().before(new java.util.Date()) &&
                                !voucher.getNgayBatDau().after(new java.util.Date()) &&
                                voucher.getTrangThaiGiamGia().getIdTrangThaiGiamGia() == 1 // Đang phát hành
                )
                .collect(Collectors.toList());
    }
}
