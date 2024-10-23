package com.example.duantn.service;


import com.example.duantn.dto.NguoiDungDTO;
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
    private XacThucService xacThucService;
    @Autowired
    private OurUserDetailsService ourUserDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

//    public NguoiDung registerUser(NguoiDungDTO nguoiDungDTO) {
//        if (nguoiDungRepository.findByEmail(nguoiDungDTO.getEmail()).isPresent()) {
//            throw new RuntimeException("User already exists with email: " + nguoiDungDTO.getEmail());
//        }
//
//        // Lấy số lượng người dùng hiện có trong hệ thống
//        long soLuongNguoiDung = nguoiDungRepository.count();
//
//        // Tự động sinh maNguoiDung theo dạng "USER-1", "USER-2", "USER-3",...
//        String maNguoiDungGenerated = "USER-" + (soLuongNguoiDung + 1);
//
//        NguoiDung newUser = new NguoiDung();
//        newUser.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());
//        newUser.setEmail(nguoiDungDTO.getEmail());
//        newUser.setMaNguoiDung(maNguoiDungGenerated); // Gán mã người dùng mới tạo
//
//        String encodedPassword = bCryptPasswordEncoder.encode(nguoiDungDTO.getMatKhau());
//        newUser.setMatKhau(encodedPassword); // Mã hóa mật khẩu trước khi lưu
//        newUser.setVaiTro(vaiTroService.getVaiTroById(nguoiDungDTO.getVaiTro())); // Gán vai trò mặc định
//        newUser.setTrangThai(nguoiDungDTO.getTrangThai());
//        newUser.setSdtNguoiDung(nguoiDungDTO.getSdtNguoiDung());
//        newUser.setDiaChi(nguoiDungDTO.getDiaChi());
//
//        // Lưu người dùng mới vào cơ sở dữ liệu
//        NguoiDung nguoiDung = nguoiDungRepository.save(newUser);
//
//        // Xác thực tài khoản người dùng
//        XacThuc xacThuc = new XacThuc();
//        xacThuc.setMaXacThuc("xacthuuser" + nguoiDung.getIdNguoiDung());
//        xacThuc.setNguoiDung(nguoiDung);
//        xacThuc.setMoTa("Chưa xác thực");
//        xacThucService.createXacThuc(xacThuc);
//
//        return nguoiDung;
//    }

    public NguoiDung registerUser(NguoiDungDTO nguoiDungDTO) {
        if (nguoiDungRepository.findByEmail(nguoiDungDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Người dùng đã tồn tại với email: " + nguoiDungDTO.getEmail());
        }

        // Lấy số lượng người dùng hiện có trong hệ thống
        long soLuongNguoiDung = nguoiDungRepository.count();

        // Tạo mã người dùng tự động theo định dạng "USER-1", "USER-2", ...
        String maNguoiDungGenerated = "user" + (soLuongNguoiDung + 1);

        // Tạo một đối tượng người dùng mới
        NguoiDung newUser = new NguoiDung();
        newUser.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());
        newUser.setEmail(nguoiDungDTO.getEmail());
        newUser.setMaNguoiDung(maNguoiDungGenerated); // Gán mã người dùng mới

        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String encodedPassword = bCryptPasswordEncoder.encode(nguoiDungDTO.getMatKhau());
        newUser.setMatKhau(encodedPassword);

        // Gán vai trò mặc định là 2 (Khách hàng)
        newUser.setVaiTro(vaiTroService.getVaiTroById(2)); // Đảm bảo rằng vai trò 2 đã tồn tại
        newUser.setTrangThai(true); // Đánh dấu người dùng là đã xác thực
        newUser.setSdtNguoiDung(nguoiDungDTO.getSdtNguoiDung());
        newUser.setDiaChi(nguoiDungDTO.getDiaChi());
        newUser.setGioiTinh(nguoiDungDTO.getGioiTinh());

        // Lưu người dùng mới vào cơ sở dữ liệu
        return nguoiDungRepository.save(newUser);
    }


    public Token signIn(NguoiDungDTO nguoiDung) throws Exception {
        try {
            // Xác thực bằng email và mật khẩu
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nguoiDung.getEmail(), nguoiDung.getMatKhau()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        NguoiDung nguoiDung1 = nguoiDungRepository.findByEmail(nguoiDung.getEmail()).orElseThrow();
        if (!nguoiDung1.getTrangThai()) throw new Exception("Chưa xác thực");

        UserDetails userDetails = ourUserDetailsService.loadUserByUsername(nguoiDung.getEmail());

        // Tạo access token và refresh token
        String accessToken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        // Trả về các token dưới dạng đối tượng Token
        return new Token(accessToken, refreshToken);
    }

    public Token generateRefreshToken(RefreshToken token) {
        // Lấy thông tin từ token cũ
        String username = jwtTokenUtil.extractUsernameToken(token.getToken());

        // Tạo refresh token mới
        UserDetails userDetails = ourUserDetailsService.loadUserByUsername(username);
        String jwttoken = jwtTokenUtil.generateToken(userDetails);
        String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

        // Trả về đối tượng phản hồi chứa refresh token
        return new Token(jwttoken, refreshToken);
    }

    public NguoiDung updateUser(Integer id, NguoiDungDTO nguoiDungDTO) {
        NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại với ID: " + id));

        // Cập nhật thông tin người dùng
        nguoiDung.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());
        nguoiDung.setEmail(nguoiDungDTO.getEmail());
        nguoiDung.setSdtNguoiDung(nguoiDungDTO.getSdtNguoiDung());
        nguoiDung.setDiaChi(nguoiDungDTO.getDiaChi());

        // Chỉ cập nhật mật khẩu nếu nó được cung cấp
        if (nguoiDungDTO.getMatKhau() != null && !nguoiDungDTO.getMatKhau().isEmpty()) {
            String encodedPassword = bCryptPasswordEncoder.encode(nguoiDungDTO.getMatKhau());
            nguoiDung.setMatKhau(encodedPassword);
        }

        // Lưu thay đổi vào cơ sở dữ liệu
        return nguoiDungRepository.save(nguoiDung);
    }

    public NguoiDung admin(NguoiDungDTO nguoiDungDTO) {
        if (nguoiDungRepository.findByEmail(nguoiDungDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Người dùng đã tồn tại với email: " + nguoiDungDTO.getEmail());
        }

        // Lấy số lượng người dùng hiện có trong hệ thống
        long soLuongNguoiDung = nguoiDungRepository.count();

        // Tạo mã người dùng tự động theo định dạng "USER-1", "USER-2", ...
        String maNguoiDungGenerated = "user" + (soLuongNguoiDung + 1);

        // Tạo một đối tượng người dùng mới
        NguoiDung newUser = new NguoiDung();
        newUser.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());
        newUser.setEmail(nguoiDungDTO.getEmail());
        newUser.setMaNguoiDung(maNguoiDungGenerated); // Gán mã người dùng mới

        // Mã hóa mật khẩu trước khi lưu vào cơ sở dữ liệu
        String encodedPassword = bCryptPasswordEncoder.encode(nguoiDungDTO.getMatKhau());
        newUser.setMatKhau(encodedPassword);

        // Gán vai trò mặc định là 2 (Khách hàng)
        newUser.setVaiTro(vaiTroService.getVaiTroById(1)); // Đảm bảo rằng vai trò 2 đã tồn tại
        newUser.setTrangThai(true); // Đánh dấu người dùng là đã xác thực
        newUser.setSdtNguoiDung(nguoiDungDTO.getSdtNguoiDung());
        newUser.setDiaChi(nguoiDungDTO.getDiaChi());

        // Lưu người dùng mới vào cơ sở dữ liệu
        return nguoiDungRepository.save(newUser);
    }

    public NguoiDung getUserById(Integer id) {
        return nguoiDungRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại với ID: " + id));
    }

}
