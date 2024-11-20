package com.example.duantn.service;

import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.security.SecureRandom;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private LoaiThongBaoRepository loaiThongBaoRepository;
    @Autowired
    private ThongBaoRepository thongBaoRepository;
    @Autowired
    private VoucherNguoiDungRepository voucherNguoiDungRepository;
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private TrangThaiGiamGiaRepository trangThaiGiamGiaRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int VOUCHER_CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> getVoucherById(Integer id) {
        return voucherRepository.findById(id);
    }

    public Voucher addVoucher(Voucher voucher) {
        if (voucher.getMaVoucher() == null || voucher.getMaVoucher().isEmpty()) {
            voucher.setMaVoucher(generateRandomVoucherCode());
        }
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
    public List<VoucherNguoiDung> addVoucherForUsers(List<Integer> idNguoiDungs, Voucher voucher) {
        // Kiểm tra thông tin voucher trước khi tạo
        System.out.println("Bắt đầu thêm voucher cho người dùng. Voucher: " + voucher);

        // Tạo voucher mới (có thể check, validate trước khi tạo)
        if (voucher.getMaVoucher() == null || voucher.getMaVoucher().isEmpty()) {
            voucher.setMaVoucher(generateRandomVoucherCode());
            System.out.println("Mã voucher được tạo ngẫu nhiên: " + voucher.getMaVoucher());
        }

        // Kiểm tra ngày bắt đầu và kết thúc voucher
        validateVoucherDates(voucher);
        setVoucherStatus(voucher);

        // Lưu voucher vào cơ sở dữ liệu
        Voucher savedVoucher = voucherRepository.save(voucher);
        System.out.println("Voucher đã lưu thành công. ID: " + savedVoucher.getIdVoucher() + ", Mã voucher: " + savedVoucher.getMaVoucher());

        // Tạo danh sách VoucherNguoiDung và lưu thông tin voucher cho nhiều người dùng
        for (Integer idNguoiDung : idNguoiDungs) {
            System.out.println("Đang xử lý người dùng ID: " + idNguoiDung);

            Optional<NguoiDung> optionalNguoiDung = nguoiDungRepository.findById(idNguoiDung);
            if (optionalNguoiDung.isPresent()) {
                NguoiDung nguoiDung = optionalNguoiDung.get();

                System.out.println("Người dùng tồn tại. Tên người dùng: " + nguoiDung.getTenNguoiDung());

                // Tạo đối tượng VoucherNguoiDung và lưu
                VoucherNguoiDung voucherNguoiDung = new VoucherNguoiDung();
                voucherNguoiDung.setVoucher(savedVoucher);
                voucherNguoiDung.setNguoiDung(nguoiDung);
                voucherNguoiDung.setNgayTao(new Date());

                // Lưu VoucherNguoiDung vào cơ sở dữ liệu
                voucherNguoiDungRepository.save(voucherNguoiDung);
                System.out.println("Voucher đã được thêm cho người dùng ID: " + idNguoiDung);

                // Gửi thông báo cho người dùng sau khi thêm voucher
                sendNotificationToUser(nguoiDung, savedVoucher);
            } else {
                // Nếu người dùng không tồn tại
                System.out.println("Người dùng với ID " + idNguoiDung + " không tồn tại!");
                throw new IllegalArgumentException("Người dùng với ID " + idNguoiDung + " không tồn tại!");
            }
        }

        // Trả về danh sách VoucherNguoiDung đã được tạo
        List<VoucherNguoiDung> voucherNguoiDungs = voucherNguoiDungRepository.findAllByVoucher(savedVoucher);
        System.out.println("Danh sách VoucherNguoiDung đã được thêm: " + voucherNguoiDungs);
        return voucherNguoiDungs;
    }

    // Phương thức gửi thông báo
    private void sendNotificationToUser(NguoiDung nguoiDung, Voucher voucher) {
        LoaiThongBao loaiThongBao = loaiThongBaoRepository.findById(6).orElseThrow(() -> new RuntimeException("LoaiThongBao không tồn tại"));
        ThongBao thongBao = new ThongBao();
        thongBao.setLoaiThongBao(loaiThongBao);
        thongBao.setNguoiDung(nguoiDung);
        thongBao.setNoiDung("Bạn đã nhận được voucher mới: " + voucher.getTenVoucher() + " với mã voucher: " + voucher.getMaVoucher());
        thongBao.setTrangThai(true); // Trạng thái thông báo là hiện thị
        thongBao.setNgayGui(new Date()); // Thời gian gửi thông báo

        // Lưu thông báo vào cơ sở dữ liệu
        thongBaoRepository.save(thongBao);
        System.out.println("Thông báo đã được gửi cho người dùng: " + nguoiDung.getTenNguoiDung());
    }

}
