package com.example.duantn.dto;

import com.example.duantn.entity.Tinh;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
public class Response<T> {
    private String code;
    private String message;
    private T data; // Chỉ để `T` thay vì `List<T>` nếu `data` không phải danh sách
}
