package com.example.duantn.controller;


import com.example.duantn.DTO.NguoiDungDTO;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.TokenUser.JwtTokenUtil;
import com.example.duantn.service.NguoiDungService;
import com.example.duantn.service.OurUserDetailsService;
import com.example.duantn.TokenUser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class AdminLoginController {
    @Autowired
    private NguoiDungService nguoiDungService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    // 1. Endpoint cho đăng ký người dùng
    @PostMapping("/dang_ky")
    public ResponseEntity<NguoiDung> registerUser(@RequestBody NguoiDungDTO nguoiDungDTO) {
        try {
            NguoiDung registeredUser = nguoiDungService.admin(nguoiDungDTO);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT); // 409 nếu người dùng đã tồn tại
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500 nếu có lỗi khác
        }
    }

    // 2. Endpoint cho đăng nhập người dùng
//    @PostMapping("/login/admin")
//    public ResponseEntity<Token> signIn(@RequestBody NguoiDungDTO nguoiDungDTO) {
//        try {
//            NguoiDung nguoiDung = new NguoiDung();
//            nguoiDung.setEmail(nguoiDungDTO.getEmail());
//            nguoiDung.setMatKhau(nguoiDungDTO.getMatKhau());
//            nguoiDung.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());
//
//            Token token = nguoiDungService.signIn(nguoiDungDTO);
//            return new ResponseEntity<>(token, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED); // 401 nếu xác thực thất bại
//        }
//    }

//    @GetMapping("/hien-thi/{id}")
//    public ResponseEntity<Token> hienthi(@RequestBody NguoiDungDTO nguoiDungDTO, Integer id) {
//        try {
//            NguoiDung nguoiDung = new NguoiDung();
//            nguoiDung.setEmail(nguoiDungDTO.getEmail());
//            nguoiDung.setMatKhau(nguoiDungDTO.getMatKhau());
//            nguoiDung.setTenNguoiDung(nguoiDungDTO.getTenNguoiDung());
//
//            Token nguoiDung1 = nguoiDungService.updateUser(id);
//            return new ResponseEntity<>(nguoiDung1, HttpStatus.OK);
//        } catch (Exception e){
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
