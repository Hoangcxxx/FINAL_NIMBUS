package com.example.duantn.service;

import com.example.duantn.DTO.DiaChiVanChuyenDTO;
import com.example.duantn.DTO.HoaDonDTO;
import com.example.duantn.DTO.SanphamchiTietDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;



import com.example.duantn.DTO.HoaDonDTO;
import com.example.duantn.entity.*;
import com.example.duantn.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class HoaDonService {
	@Autowired
	private HoaDonRepository hoaDonRepository;

	@Autowired
	private HoaDonChiTietRepository hoaDonChiTietRepository;

	@Autowired
	private PhuongThucThanhToanHoaDonRepository phuongThucThanhToanHoaDonRepository;

	@Autowired
	private GioHangRepository gioHangRepository;

	@Autowired
	private PaymentService service;

	@Autowired
	private GioHangChiTietRepository gioHangChiTietRepository;

	@Autowired
	private DiaChiVanChuyenRepository diaChiVanChuyenRepository;

	@Autowired
	private NguoiDungRepository nguoiDungRepository;

	@Autowired
	private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

	@Autowired
	private SanPhamChiTietRepository sanPhamChiTietRepository;

	@Autowired
	private TrangThaiHoaDonRepository trangThaiHoaDonRepository;



	public List<HoaDonDTO> hienthi(String maHoaDon) {
		List<HoaDon> hoaDonList;

		if (maHoaDon != null && !maHoaDon.isEmpty()) {
			hoaDonList = hoaDonRepository.findByMaHoaDon(maHoaDon)
					.map(Collections::singletonList)
					.orElse(Collections.emptyList());
		} else {
			hoaDonList = hoaDonRepository.findAll();
		}

		return hoaDonList.stream()
				.map(this::convertToDTO)
				.collect(Collectors.toList());
	}

	private HoaDonDTO convertToDTO(HoaDon hoaDon) {
		HoaDonDTO dto = new HoaDonDTO();

		dto.setIdHoaDon(hoaDon.getIdHoaDon());
		dto.setMaHoaDon(hoaDon.getMaHoaDon());
		dto.setTenNguoiNhan(hoaDon.getTenNguoiNhan());
		dto.setSdtNguoiNhan(hoaDon.getSdtNguoiNhan());
		dto.setDiaChi(hoaDon.getDiaChi());
		dto.setPhiShip(hoaDon.getPhiShip().doubleValue());
		dto.setThanhTien(hoaDon.getThanhTien());
		dto.setGhiChu(hoaDon.getMoTa());


//
//		// Thông tin voucher
//		if (hoaDon.getVoucher() != null) {
//			dto.setIdvoucher(hoaDon.getVoucher().getIdVoucher());
//		}

		// Thông tin người dùng
		if (hoaDon.getNguoiDung() != null) {
			dto.setIdNguoiDung(hoaDon.getNguoiDung().getIdNguoiDung());
			dto.setEmail(hoaDon.getNguoiDung().getEmail());
		}

		// Thông tin địa chỉ vận chuyển
		if (hoaDon.getDiaChiVanChuyen() != null) {
			dto.setIdDiaChiVanChuyen(hoaDon.getDiaChiVanChuyen().getIdDiaChiVanChuyen());
			dto.setTinh(hoaDon.getDiaChiVanChuyen().getTinh());
			dto.setHuyen(hoaDon.getDiaChiVanChuyen().getHuyen());
			dto.setXa(hoaDon.getDiaChiVanChuyen().getXa());
		}

		// Thông tin trạng thái hóa đơn
		if (hoaDon.getTrangThaiHoaDon() != null) {
			dto.setIdtrangthaihoadon(hoaDon.getTrangThaiHoaDon().getIdTrangThaiHoaDon());
		}

		// Thông tin phương thức thanh toán
		if (hoaDon.getPhuongThucThanhToanHoaDon() != null) {
			dto.setIdphuongthucthanhtoanhoadon(hoaDon.getPhuongThucThanhToanHoaDon().getIdThanhToanHoaDon());
			dto.setTenPhuongThucThanhToan(hoaDon.getPhuongThucThanhToanHoaDon()
					.getPhuongThucThanhToan()
					.getTenPhuongThuc());
		}

		// Danh sách sản phẩm chi tiết
		List<HoaDonChiTiet> chiTietList = hoaDonChiTietRepository.findByHoaDon_IdHoaDon(hoaDon.getIdHoaDon());
		dto.setListSanPhamChiTiet(chiTietList.stream()
				.map(chiTiet -> {
					SanphamchiTietDTO spDTO = new SanphamchiTietDTO();
					spDTO.setIdspct(chiTiet.getSanPhamChiTiet().getIdSanPhamChiTiet());
					spDTO.setSoLuong(chiTiet.getSoLuong());
					spDTO.setGiaTien(chiTiet.getTongTien());
					spDTO.setTenkichthuoc(chiTiet.getSanPhamChiTiet().getKichThuocChiTiet().getKichThuoc().getTenKichThuoc());
					spDTO.setTenmausac(chiTiet.getSanPhamChiTiet().getMauSacChiTiet().getMauSac().getTenMauSac());
					spDTO.setTenchatlieu(chiTiet.getSanPhamChiTiet().getChatLieuChiTiet().getChatLieu().getTenChatLieu());
					spDTO.setTenSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getTenSanPham());
					spDTO.setMoTa(chiTiet.getSanPhamChiTiet().getSanPham().getMoTa());
					spDTO.setIdSanPham(chiTiet.getSanPhamChiTiet().getSanPham().getIdSanPham());
					return spDTO;
				})
				.collect(Collectors.toList()));

		return dto;
	}


	public void createOrder(HoaDonDTO hoaDonDTO,HttpServletRequest res) {
		long currentCount = hoaDonRepository.count();
		String generatedMaHoaDon = "HD00" + (currentCount + 1);

		HoaDon hoaDon = new HoaDon();
		hoaDon.setMaHoaDon(generatedMaHoaDon);

		NguoiDung nguoiDung = nguoiDungRepository.findById(hoaDonDTO.getIdNguoiDung())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy Người dùng"));
		hoaDon.setNguoiDung(nguoiDung);

		DiaChiVanChuyen diaChiVanChuyen = diaChiVanChuyenRepository
				.findByTinhAndHuyenAndXa(hoaDonDTO.getTinh(), hoaDonDTO.getHuyen(), hoaDonDTO.getXa())
				.orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ vận chuyển"));
		hoaDon.setDiaChiVanChuyen(diaChiVanChuyen);
		hoaDon.setPhiShip(diaChiVanChuyen.getSoTienVanChuyen());
		hoaDon.setNgayTao(new Date());

		hoaDon.setTrangThaiHoaDon(trangThaiHoaDonRepository.findById(1)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy trạng thái hóa đơn")));
		hoaDon.setTenNguoiNhan(hoaDonDTO.getTenNguoiNhan());
		hoaDon.setDiaChi(hoaDonDTO.getDiaChi());
		hoaDon.setSdtNguoiNhan(hoaDonDTO.getSdtNguoiNhan());
		hoaDon.setThanhTien(hoaDonDTO.getThanhTien());
		hoaDon.setMoTa(hoaDonDTO.getGhiChu());
		hoaDon.setTrangThai(true);

		switch (hoaDonDTO.getTenPhuongThucThanhToan()) {
			case "cod":
				saveCODPayment(hoaDon, generatedMaHoaDon, hoaDonDTO);
				break;
			case "vnpay":
				saveVNPayPayment(hoaDon,generatedMaHoaDon,hoaDonDTO,res);
				break;
			case "zalopay":
				throw new RuntimeException("Chức năng ZaloPay đang phát triển");
		}
	}

	private void saveCODPayment(HoaDon hoaDon, String generatedMaHoaDon, HoaDonDTO hoaDonDTO) {
		hoaDonRepository.save(hoaDon);
		PhuongThucThanhToanHoaDon phuongThucThanhToanHoaDon = new PhuongThucThanhToanHoaDon();
		phuongThucThanhToanHoaDon.setPhuongThucThanhToan(phuongThucThanhToanRepository.findById(4).get()); // COD
		phuongThucThanhToanHoaDon.setNgayGiaoDich(new Date());
		phuongThucThanhToanHoaDon.setMoTa("Thanh toán COD cho đơn hàng " + generatedMaHoaDon);
		phuongThucThanhToanHoaDon.setHoaDon(hoaDon);
		phuongThucThanhToanHoaDonRepository.save(phuongThucThanhToanHoaDon);
		hoaDon.setPhuongThucThanhToanHoaDon(phuongThucThanhToanHoaDon);
		hoaDonRepository.save(hoaDon);

		processCartItems(hoaDonDTO, hoaDon);
	}

	private void saveVNPayPayment(HoaDon hoaDon, String generatedMaHoaDon, HoaDonDTO hoaDonDTO, HttpServletRequest req) {
		try {
			// Gửi yêu cầu thanh toán VNPay và lấy URL thanh toán
			String paymentUrl = service.createPayment(hoaDonDTO.getThanhTien().longValue(), "vnpay", req);
			System.out.println("URL thanh toán VNPay: " + paymentUrl);

			// Lưu hóa đơn
			hoaDonRepository.save(hoaDon);
			PhuongThucThanhToanHoaDon phuongThucThanhToanHoaDon = new PhuongThucThanhToanHoaDon();
			phuongThucThanhToanHoaDon.setPhuongThucThanhToan(phuongThucThanhToanRepository.findById(2).orElseThrow(() -> new RuntimeException("Phương thức thanh toán không hợp lệ"))); // 2 là mã phương thức thanh toán cho VNPay
			phuongThucThanhToanHoaDon.setNgayGiaoDich(new Date());
			phuongThucThanhToanHoaDon.setMoTa("Thanh toán VNPay cho đơn hàng " + generatedMaHoaDon);
			phuongThucThanhToanHoaDon.setHoaDon(hoaDon);
			phuongThucThanhToanHoaDonRepository.save(phuongThucThanhToanHoaDon);
			hoaDon.setPhuongThucThanhToanHoaDon(phuongThucThanhToanHoaDon);
			hoaDonRepository.save(hoaDon);

			processCartItems(hoaDonDTO, hoaDon);
			updateHoaDonAfterPaymentSuccess(hoaDon, paymentUrl);

		} catch (Exception e) {
			throw new RuntimeException("Lỗi khi tạo URL thanh toán VNPay", e);
		}
	}

	private void updateHoaDonAfterPaymentSuccess(HoaDon hoaDon, String paymentUrl) {
		hoaDon.setTrangThaiHoaDon(hoaDon.getTrangThaiHoaDon());
		hoaDon.setThanhTien(hoaDon.getThanhTien());
		hoaDonRepository.save(hoaDon);
	}


	private void processCartItems(HoaDonDTO hoaDonDTO, HoaDon hoaDon) {
		hoaDonDTO.getListSanPhamChiTiet().forEach(sanPham -> {
			SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(sanPham.getIdspct())
					.orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm chi tiết"));

			if (sanPhamChiTiet.getSoLuong() <= 0 || sanPham.getSoLuong() > sanPhamChiTiet.getSoLuong()) {
				throw new RuntimeException("Số lượng tồn kho không đủ");
			}

			sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - sanPham.getSoLuong());
			sanPhamChiTietRepository.save(sanPhamChiTiet);

			HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
			hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
			hoaDonChiTiet.setHoaDon(hoaDon);
			hoaDonChiTiet.setSoLuong(sanPham.getSoLuong());
			hoaDonChiTiet.setTongTien(sanPham.getGiaTien().multiply(BigDecimal.valueOf(sanPham.getSoLuong())));
			hoaDonChiTiet.setNgayTao(new Date());
			hoaDonChiTiet.setTrangThai(true);
			hoaDonChiTietRepository.save(hoaDonChiTiet);

			clearGioHang(hoaDonDTO.getCartId());
		});
	}

	public void clearGioHang(Integer idGioHang) {
		GioHang gioHang = gioHangRepository.findById(idGioHang)
				.orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

		List<GioHangChiTiet> chiTietList = gioHangChiTietRepository.findByGioHang_IdGioHang(idGioHang);
		gioHangChiTietRepository.deleteAll(chiTietList);

		gioHang.setNgayCapNhat(new Date());
		gioHangRepository.save(gioHang);
	}
}