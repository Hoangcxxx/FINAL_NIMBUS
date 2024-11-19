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

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class NguoiDungController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody NguoiDungDTO nguoiDungDTO) {
        try {
            NguoiDung registeredUser = nguoiDungService.registerUser(nguoiDungDTO);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi xảy ra trong quá trình đăng ký"));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody NguoiDungDTO nguoiDungDTO) {
        try {
            LoginResponse loginResponse = nguoiDungService.signIn(nguoiDungDTO);
            if (loginResponse == null || loginResponse.getTenNguoiDung() == null || loginResponse.getTenNguoiDung().isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Tên người dùng không tồn tại"));
            }
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Tên người dùng hoặc mật khẩu không đúng"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Người dùng không tồn tại hoặc chưa được xác thực"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi xảy ra trong quá trình đăng nhập"));
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
    public ResponseEntity<?> updateUser(@PathVariable Integer userid, @RequestBody NguoiDungDTO nguoiDungDTO) {
        try {
            NguoiDung nguoiDung = nguoiDungService.updateUser(userid, nguoiDungDTO);
            return ResponseEntity.ok(nguoiDung);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi xảy ra trong quá trình cập nhật thông tin người dùng"));
        }
    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Token tokenRequest) {
        try {
            nguoiDungService.logout(tokenRequest.getAccessToken());
            return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Có lỗi xảy ra trong quá trình đăng xuất"));
        }
    }


}

