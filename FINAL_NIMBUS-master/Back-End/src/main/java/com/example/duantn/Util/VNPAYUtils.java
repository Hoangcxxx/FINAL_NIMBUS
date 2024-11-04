package com.example.duantn.Util;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.StringJoiner;

public class VNPAYUtils {

    public static String hmacSHA512(String key, String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(key.getBytes(StandardCharsets.UTF_8));
        byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    public static String buildQueryString(Map<String, String> params) {
        StringJoiner sj = new StringJoiner("&");
        params.forEach((k, v) -> sj.add(k + "=" + v));
        return sj.toString();
    }
}
