package com.example.moneymoney.model.responsemodel;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDTO {
    private Long Id;
    private BigDecimal totalPrice;
    private List<OrderDetailResponseDTO> list;

}
