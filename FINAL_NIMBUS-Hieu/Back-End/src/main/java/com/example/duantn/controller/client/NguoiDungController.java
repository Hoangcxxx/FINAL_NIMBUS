package com.example.duantn.controller.client;

import com.example.duantn.Response.LoginResponse;
import com.example.duantn.DTO.NguoiDungDTO;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.service.NguoiDungService;
import com.example.duantn.TokenUser.RefreshToken;
import com.example.duantn.TokenUser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class NguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @PostMapping("/register")
    public ResponseEntity<NguoiDung> registerUser(@RequestBody NguoiDungDTO nguoiDungDTO) {
        try {
            NguoiDung registeredUser = nguoiDungService.registerUser(nguoiDungDTO);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody NguoiDungDTO nguoiDungDTO) {
        try {
            LoginResponse loginResponse = nguoiDungService.signIn(nguoiDungDTO);


            if (loginResponse == null || loginResponse.getTenNguoiDung() == null || loginResponse.getTenNguoiDung().isEmpty()) {
                return new ResponseEntity<>("Tên người dùng không tồn tại", HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Tên người dùng hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Người dùng không tồn tại hoặc chưa được xác thực", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            e.printStackTrace(); // Log lỗi để dễ debug
            return new ResponseEntity<>("Có lỗi xảy ra trong quá trình đăng nhập", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<Token> refreshToken(@RequestBody RefreshToken refreshTokenRequest) {
        try {
            Token token = nguoiDungService.generateRefreshToken(refreshTokenRequest);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<NguoiDungDTO> getUserById(@PathVariable Integer id) {
        try {
            NguoiDung nguoiDung = nguoiDungService.getUserById(id);

            // Convert NguoiDung to NguoiDungDTO
            NguoiDungDTO nguoiDungDTO = new NguoiDungDTO();
            nguoiDungDTO.setId(nguoiDung.getIdNguoiDung());
            nguoiDungDTO.setTenNguoiDung(nguoiDung.getTenNguoiDung());
            nguoiDungDTO.setMaNguoiDung(nguoiDung.getMaNguoiDung());
            nguoiDungDTO.setEmail(nguoiDung.getEmail());
            nguoiDungDTO.setSdtNguoiDung(nguoiDung.getSdtNguoiDung());
            nguoiDungDTO.setDiaChi(nguoiDung.getDiaChi());
            nguoiDungDTO.setGioiTinh(nguoiDung.getGioiTinh());
            nguoiDungDTO.setTrangThai(nguoiDung.getTrangThai());
            nguoiDungDTO.setMatKhau(nguoiDung.getMatKhau());

            return new ResponseEntity<>(nguoiDungDTO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{userid}")
    public ResponseEntity<NguoiDung> updateUser(@PathVariable Integer userid, @RequestBody NguoiDungDTO nguoiDungDTO) {
        try {
            NguoiDung nguoiDung = nguoiDungService.updateUser(userid, nguoiDungDTO);
            return new ResponseEntity<>(nguoiDung, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

