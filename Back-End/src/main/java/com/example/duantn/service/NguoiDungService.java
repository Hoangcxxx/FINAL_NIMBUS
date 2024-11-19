package com.example.duantn.service;

import com.example.duantn.Response.LoginResponse;
import com.example.duantn.DTO.NguoiDungDTO;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.TokenUser.*;
import com.example.duantn.repository.NguoiDungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class NguoiDungService {

	@Autowired
	private NguoiDungRepository nguoiDungRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private VaiTroService vaiTroService;
	@Autowired
	private OurUserDetailsService ourUserDetailsService;
	@Autowired
	private AuthenticationManager authenticationManager;

	public NguoiDung registerUser(NguoiDungDTO nguoiDungDTO) {
		if (nguoiDungRepository.findByEmail(nguoiDungDTO.getEmail()).isPresent()) {
			throw new RuntimeException("Email đã tồn tại: " + nguoiDungDTO.getEmail());
		}

		String maNguoiDungGenerated = generateUserCode();

		NguoiDung newUser = new NguoiDung();
		newUser.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());
		newUser.setEmail(nguoiDungDTO.getEmail());
		newUser.setMaNguoiDung(maNguoiDungGenerated);
		newUser.setMatKhau(passwordEncoder.encode(nguoiDungDTO.getMatKhau()));
		newUser.setVaiTro(vaiTroService.getVaiTroById(2)); // 2: Vai trò người dùng
		newUser.setTrangThai(true);
		newUser.setSdtNguoiDung(nguoiDungDTO.getSdtNguoiDung());
		newUser.setDiaChi(nguoiDungDTO.getDiaChi());
		newUser.setGioiTinh(nguoiDungDTO.getGioiTinh());

		return nguoiDungRepository.save(newUser);
	}
	private String generateUserCode() {
		long userCount = nguoiDungRepository.count();
		return "user" + (userCount + 1);
	}

	public LoginResponse signIn(NguoiDungDTO nguoiDung) throws Exception {
		authenticateUser(nguoiDung.getEmail(), nguoiDung.getMatKhau());

		NguoiDung nguoiDungEntity = nguoiDungRepository.findByEmail(nguoiDung.getEmail())
				.orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

		if (!nguoiDungEntity.getTrangThai()) {
			throw new Exception("Tài khoản chưa được xác thực");
		}

		UserDetails userDetails = ourUserDetailsService.loadUserByUsername(nguoiDung.getEmail());
		String accessToken = jwtTokenUtil.generateToken(userDetails);

		return new LoginResponse(nguoiDungEntity.getIdNguoiDung(), nguoiDungEntity.getTenNguoiDung(), accessToken);
	}

	public Token generateRefreshToken(RefreshToken token) {
		String username = jwtTokenUtil.extractUsernameToken(token.getToken());
		UserDetails userDetails = ourUserDetailsService.loadUserByUsername(username);

		return new Token(jwtTokenUtil.generateToken(userDetails), jwtTokenUtil.generateRefreshToken(userDetails));
	}

	public NguoiDung updateUser(Integer id, NguoiDungDTO nguoiDungDTO) {
		NguoiDung nguoiDung = nguoiDungRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Người dùng không tồn tại với ID: " + id));

		nguoiDung.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());
		nguoiDung.setEmail(nguoiDungDTO.getEmail());

		// Kiểm tra mật khẩu có được thay đổi không
		if (nguoiDungDTO.getMatKhau() != null && !nguoiDungDTO.getMatKhau().isEmpty()) {
			// Nếu có mật khẩu mới, mã hóa và lưu mật khẩu
			nguoiDung.setMatKhau(passwordEncoder.encode(nguoiDungDTO.getMatKhau()));
		}

		nguoiDung.setSdtNguoiDung(nguoiDungDTO.getSdtNguoiDung());
		nguoiDung.setDiaChi(nguoiDungDTO.getDiaChi());
		nguoiDung.setGioiTinh(nguoiDungDTO.getGioiTinh());
		nguoiDung.setTrangThai(nguoiDungDTO.getTrangThai());

		return nguoiDungRepository.save(nguoiDung);
	}

	public NguoiDung getUserById(Integer id) {
		return nguoiDungRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Người dùng không tồn tại với ID: " + id));
	}

	public void logout(String token) {
		jwtTokenUtil.invalidateToken(token);
	}

	private void authenticateUser(String email, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException e) {
			throw new RuntimeException("Tên người dùng hoặc mật khẩu không đúng");
		}
	}


}
