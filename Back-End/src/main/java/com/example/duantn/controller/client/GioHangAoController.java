package com.example.duantn.controller.client;

import com.example.duantn.dto.ProductInCart;
import com.example.duantn.entity.GioHang;
import com.example.duantn.entity.NguoiDung;
import com.example.duantn.service.GioHangAoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/gio_hang_ao")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class GioHangAoController {
    @Autowired
    private GioHangAoService gioHangAoService;

    @PostMapping("/add_to_cart")
    public GioHang addProductsToCart(@RequestParam Integer idNguoiDung, @RequestBody List<ProductInCart> products) {
        return gioHangAoService.addProductsToCart(idNguoiDung, products);
    }
}
