package com.example.duantn.dto;

public class PaymentRequest {
    private int amount;      // Số tiền thanh toán
    private int expireHours; // Số giờ hết hạn

    // Getters và Setters
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getExpireHours() {
        return expireHours;
    }

    public void setExpireHours(int expireHours) {
        this.expireHours = expireHours;
    }
}
