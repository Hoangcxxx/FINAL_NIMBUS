package com.example.duantn.controller.admin;

import com.example.duantn.entity.ChatLieu;
import com.example.duantn.service.ChatLieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/chat_lieu")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class ADChatLieuController {
    @Autowired
    private ChatLieuService chatLieuService;

    @GetMapping
    public ResponseEntity<List<ChatLieu>> getAllChatLieu() {
        List<ChatLieu> ChatLieucList = chatLieuService.getAllChatLieu();
        return new ResponseEntity<>(ChatLieucList, HttpStatus.OK);
    }
    @GetMapping("/{tenChatLieu}")
    public ResponseEntity<List<ChatLieu>> searchChatLieu(@PathVariable String tenChatLieu) {
        List<ChatLieu> chatLieuList = chatLieuService.searchChatLieuByTen(tenChatLieu);
        return new ResponseEntity<>(chatLieuList, HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<ChatLieu> createChatLieuc(@RequestBody ChatLieu ChatLieuc) {
        ChatLieu newChatLieu = chatLieuService.createChatLieu(ChatLieuc);
        return new ResponseEntity<>(newChatLieu, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChatLieu> updateChatLieuc(@PathVariable Integer id, @RequestBody ChatLieu ChatLieucDetails) {
        ChatLieu updatedChatLieu = chatLieuService.updateChatLieu(id, ChatLieucDetails);
        return new ResponseEntity<>(updatedChatLieu, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatLieuc(@PathVariable Integer id) {
        chatLieuService.deleteChatLieu(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
