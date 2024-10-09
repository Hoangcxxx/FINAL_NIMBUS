package com.example.duantn.service;

import com.example.duantn.entity.ChatLieu;
import com.example.duantn.repository.ChatLieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ChatLieuService {
    @Autowired
    private ChatLieuRepository chatLieuRepository;

    public List<ChatLieu> getAllChatLieu() {
        return chatLieuRepository.findAll();
    }

    public ChatLieu createChatLieu(ChatLieu chatLieu) {
        // Thiết lập ngày tạo là thời điểm hiện tại
        chatLieu.setNgayTao(new Date());
        chatLieu.setNgayCapNhat(new Date());
        return chatLieuRepository.save(chatLieu);
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
