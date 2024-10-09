    package com.example.duantn.controller;

    import com.example.duantn.DTO.NguoiDungDTO;
    import com.example.duantn.entity.NguoiDung;
    import com.example.duantn.repository.NguoiDungRepository;
    import com.example.duantn.service.NguoiDungService;
    import com.example.duantn.service.OurUserDetailsService;
    import com.example.duantn.TokenUser.RefreshToken;
    import com.example.duantn.TokenUser.Token;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/auth")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public class NguoiDungController {

        @Autowired
        private NguoiDungService nguoiDungService;

        @Autowired
        private NguoiDungRepository ndrp;
        @Autowired
        private OurUserDetailsService ourUserDetailsService;

        // 1. Endpoint cho đăng ký người dùng
        @PostMapping("/register")
        public ResponseEntity<NguoiDung> registerUser(@RequestBody NguoiDungDTO nguoiDungDTO) {
            try {
                NguoiDung registeredUser = nguoiDungService.registerUser(nguoiDungDTO);
                return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
            } catch (RuntimeException e) {
                return new ResponseEntity<>(null, HttpStatus.CONFLICT); // 409 nếu người dùng đã tồn tại
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500 nếu có lỗi khác
            }
        }

        // 2. Endpoint cho đăng nhập người dùng
        @PostMapping("/login")
        public ResponseEntity<Token> signIn(@RequestBody NguoiDungDTO nguoiDungDTO) {
            try {
                NguoiDung nguoiDung = new NguoiDung();
                nguoiDung.setEmail(nguoiDungDTO.getEmail());
                nguoiDung.setMatKhau(nguoiDungDTO.getMatKhau());
                nguoiDung.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());

                Token token = nguoiDungService.signIn(nguoiDungDTO);
                return new ResponseEntity<>(token, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED); // 401 nếu xác thực thất bại
            }
        }
    //
    //    // 3. Endpoint cho yêu cầu đặt lại mật khẩu
    //    @PostMapping("/forgot-password")
    //    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
    //        try {
    //            nguoiDungService.forgotPassword(forgotPasswordDTO.getEmail());
    //            return new ResponseEntity<>("Email đã được gửi để đặt lại mật khẩu", HttpStatus.OK);
    //        } catch (Exception e) {
    //            return new ResponseEntity<>("Có lỗi xảy ra, vui lòng thử lại", HttpStatus.INTERNAL_SERVER_ERROR);
    //        }
    //    }

        // 4. Endpoint để làm mới token
        @PostMapping("/refresh-token")
        public ResponseEntity<Token> refreshToken(@RequestBody RefreshToken refreshTokenRequest) {
            try {
                Token token = nguoiDungService.generateRefreshToken(refreshTokenRequest);
                return new ResponseEntity<>(token, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED); // 401 nếu refresh token không hợp lệ
            }
        }
        // 3. Endpoint để lấy thông tin người dùng theo ID
        @GetMapping("/user/{id}")
        public ResponseEntity<?> getUserById(@PathVariable Integer id) {
            try {
                NguoiDung nguoiDung = nguoiDungService.getUserById(id);
                NguoiDungDTO nguoiDungDTO = new NguoiDungDTO();

                // Chuyển đổi thông tin người dùng thành DTO
                nguoiDungDTO.setTenNguoiDung(nguoiDung.getTenNguoiDung());
                nguoiDungDTO.setEmail(nguoiDung.getEmail());
                nguoiDungDTO.setMatKhau(nguoiDung.getMatKhau()); // Mã hóa mật khẩu để hiển thị
                nguoiDungDTO.setSdtNguoiDung(nguoiDung.getSdtNguoiDung());
                nguoiDungDTO.setDiaChi(nguoiDung.getDiaChi());

                return new ResponseEntity<>(nguoiDungDTO, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Người dùng không tồn tại với ID: " + id, HttpStatus.NOT_FOUND);
            }
        }

    }
