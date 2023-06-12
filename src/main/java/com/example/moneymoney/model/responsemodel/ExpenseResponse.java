package com.example.moneymoney.model.responsemodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
public class ExpenseResponse {
    private Long id;
    private String expenseCategoryName;
    private Timestamp date;
    private BigDecimal amount;
    private String description;
    private String assetName;

    // Constructors, getters, and setters
}
