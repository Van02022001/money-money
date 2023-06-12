package com.example.moneymoney.model.responsemodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncomeResponse {
    private Long id;
    private String incomeCategoryName;
    private Timestamp date;
    private BigDecimal amount;
    private String description;
    private String assetName;
}
