package com.example.duantn.service;

import com.example.duantn.entity.ChatLieu;
import com.example.duantn.entity.ChatLieu;
import com.example.duantn.entity.ChatLieuChiTiet;
import com.example.duantn.repository.ChatLieuChiTietRepository;
import com.example.duantn.repository.ChatLieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChatLieuService {
    @Autowired
    private ChatLieuRepository chatLieuRepository;
    @Autowired
    private ChatLieuChiTietRepository chatLieuChiTietRepository;
    public List<ChatLieu> getAllChatLieu() {
        // Lấy danh sách sắp xếp theo ngày cập nhật giảm dần
        return chatLieuRepository.findAllByOrderByNgayCapNhatDesc();
    }
    public List<ChatLieu> searchChatLieuByTen(String tenChatLieu) {
        return chatLieuRepository.findByTenChatLieuContaining(tenChatLieu);
    }
    public ChatLieu createChatLieu(ChatLieu chatLieu) {
        // Thiết lập ngày tạo và ngày cập nhật cho ChatLieu
        Date currentDate = new Date();
        chatLieu.setNgayTao(currentDate);
        chatLieu.setNgayCapNhat(currentDate);

        // Lưu ChatLieu vào database
        ChatLieu savedChatLieu = chatLieuRepository.save(chatLieu);

        // Tạo ChatLieuChiTiet liên kết với ChatLieu vừa lưu
        ChatLieuChiTiet chatLieuChiTiet = new ChatLieuChiTiet();
        chatLieuChiTiet.setChatLieu(savedChatLieu); // Gắn ChatLieu vào ChatLieuChiTiet
        chatLieuChiTiet.setNgayTao(currentDate);
        chatLieuChiTiet.setNgayCapNhat(currentDate);

        // Lưu ChatLieuChiTiet vào database
        chatLieuChiTietRepository.save(chatLieuChiTiet);

        return savedChatLieu;
    }

    public ChatLieu updateChatLieu(Integer id, ChatLieu chatLieuDetails) {
        ChatLieu chatLieu = chatLieuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found"));
        chatLieu.setTenChatLieu(chatLieuDetails.getTenChatLieu());
        chatLieu.setMoTa(chatLieuDetails.getMoTa());
        chatLieu.setNgayCapNhat(new Date()); // Cập nhật thời gian
        return chatLieuRepository.save(chatLieu);
    }

    public void deleteChatLieu(Integer id) {
        ChatLieu chatLieu = chatLieuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found"));
        chatLieuRepository.delete(chatLieu);
    }
}
