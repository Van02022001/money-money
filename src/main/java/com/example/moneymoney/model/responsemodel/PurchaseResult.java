package com.example.moneymoney.model.responsemodel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseResult {
    private String status;
    private String message;
    private String paymentQRCodeUrl;

    public PurchaseResult(String status, String message) {
        this(status, message, null);
    }
}
