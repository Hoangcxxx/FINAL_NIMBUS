        package com.example.duantn.service;

        import com.example.duantn.Response.LoginResponse;
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

            public NguoiDung registerUser(NguoiDungDTO nguoiDungDTO) {
                if (nguoiDungRepository.findByEmail(nguoiDungDTO.getEmail()).isPresent()) {
                    throw new RuntimeException("Người dùng đã tồn tại với email: " + nguoiDungDTO.getEmail());
                }

                long soLuongNguoiDung = nguoiDungRepository.count();
                String maNguoiDungGenerated = "user" + (soLuongNguoiDung + 1);

                NguoiDung newUser = new NguoiDung();
                newUser.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());
                newUser.setEmail(nguoiDungDTO.getEmail());
                newUser.setMaNguoiDung(maNguoiDungGenerated);

                String encodedPassword = bCryptPasswordEncoder.encode(nguoiDungDTO.getMatKhau());
                newUser.setMatKhau(encodedPassword);
                newUser.setVaiTro(vaiTroService.getVaiTroById(2));
                newUser.setTrangThai(true);
                newUser.setSdtNguoiDung(nguoiDungDTO.getSdtNguoiDung());
                newUser.setDiaChi(nguoiDungDTO.getDiaChi());
                newUser.setGioiTinh(nguoiDungDTO.getGioiTinh());

                return nguoiDungRepository.save(newUser);
            }
            public LoginResponse signIn(NguoiDungDTO nguoiDung) throws Exception {
                try {
                    // Kiểm tra thông tin xác thực
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(nguoiDung.getEmail(), nguoiDung.getMatKhau()));
                } catch (BadCredentialsException e) {
                    throw new Exception("Tên người dùng hoặc mật khẩu không đúng", e);
                }

                // Lấy thông tin người dùng từ cơ sở dữ liệu
                NguoiDung nguoiDung1 = nguoiDungRepository.findByEmail(nguoiDung.getEmail()).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
                if (!nguoiDung1.getTrangThai()) throw new Exception("Chưa xác thực");

                UserDetails userDetails = ourUserDetailsService.loadUserByUsername(nguoiDung.getEmail());
                String accessToken = jwtTokenUtil.generateToken(userDetails);
                return new LoginResponse(nguoiDung1.getIdNguoiDung(), nguoiDung1.getTenNguoiDung(), accessToken);
            }


            public Token generateRefreshToken(RefreshToken token) {
                String username = jwtTokenUtil.extractUsernameToken(token.getToken());
                UserDetails userDetails = ourUserDetailsService.loadUserByUsername(username);
                String jwttoken = jwtTokenUtil.generateToken(userDetails);
                String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);
                return new Token(jwttoken, refreshToken);
            }

            public NguoiDung updateUser(Integer id, NguoiDungDTO nguoiDungDTO) {
                NguoiDung nguoiDung = nguoiDungRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại với ID: " + id));
                nguoiDung.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());
                nguoiDung.setEmail(nguoiDungDTO.getEmail());
                nguoiDung.setMatKhau(bCryptPasswordEncoder.encode(nguoiDungDTO.getMatKhau()));
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
        }
