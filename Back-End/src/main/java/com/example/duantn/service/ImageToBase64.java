package com.example.duantn.service;

import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageToBase64 {
    public static void main(String[] args) throws Exception {
        byte[] imageBytes = Files.readAllBytes(Paths.get("D:\\upddate\\FINAL_NIMBUS-master\\FINAL_NIMBUS-master\\Fond-End\\admin\\view-admin\\assets\\image\\favicon1.png"));
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        System.out.println(base64Image);
    }
}
