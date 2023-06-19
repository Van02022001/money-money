package com.example.moneymoney.model.responsemodel;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponseDTO {

    private Long Id;
    private BigDecimal totalPrice;
    private int quantity;

}
