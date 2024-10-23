//package com.example.duantn.controller.client;
//
//
//
//import com.example.duantn.dto.GioHangChiTietDTO;
//import com.example.duantn.service.GioHangChiTietService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//public class GioHangChiTietController {
//
//    @Autowired
//    private GioHangChiTietService gioHangChiTietService;
//
//    @GetMapping("/api/cart/details")
//    public List<GioHangChiTietDTO> getCartDetails(@RequestParam Integer idGioHang) {
//        return gioHangChiTietService.getGioHangChiTietWithSanPham(idGioHang);
//    }
//}
