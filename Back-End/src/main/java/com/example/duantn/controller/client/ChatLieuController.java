package com.example.duantn.controller.client;

import com.example.duantn.entity.ChatLieu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/nguoi_dung/chat_lieu")
@CrossOrigin(origins = "http://127.0.0.1:5502")
public class ChatLieuController {
    @Autowired
    private com.example.duantn.service.ChatLieuService chatLieuService;
    @GetMapping
    public ResponseEntity<List<ChatLieu>> getAllChatLieu() {
        List<ChatLieu> ChatLieuList = chatLieuService.getAllChatLieu();
        return new ResponseEntity<>(ChatLieuList, HttpStatus.OK);
    }
}
