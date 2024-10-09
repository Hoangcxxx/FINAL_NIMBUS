package com.example.duantn.service;

import com.example.duantn.entity.GioHang;
import com.example.duantn.repository.GioHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GioHangService {
    @Autowired
    private GioHangRepository ghrp;


    public List<GioHang> loading(){
        return ghrp.findAll();
    }

    public GioHang create(GioHang gioHang){
        return ghrp.save(gioHang);
    }

    public GioHang update(Integer id,GioHang gioHang){
        gioHang.setIdGioHang(id);
        return ghrp.save(gioHang);
    }

    public GioHang delete(Integer id){
        ghrp.deleteById(id);
        return ghrp.findById(id).get();
    }
}
