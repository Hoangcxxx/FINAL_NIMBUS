package com.example.duantn.repository;

import com.example.duantn.entity.ChatLieu;
import com.example.duantn.query.ChatLieuQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatLieuRepository extends JpaRepository<ChatLieu, Integer> {

    @Query(value = ChatLieuQuery.GET_ALL_CHAT_LIEU, nativeQuery = true)
    List<Object[]> getAllChatLieu();
}
