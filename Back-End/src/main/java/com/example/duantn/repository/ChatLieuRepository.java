package com.example.duantn.repository;

import com.example.duantn.entity.ChatLieu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatLieuRepository extends JpaRepository<ChatLieu, Integer> {
    List<ChatLieu> findByTenChatLieuContaining(String tenChatLieu);

}
