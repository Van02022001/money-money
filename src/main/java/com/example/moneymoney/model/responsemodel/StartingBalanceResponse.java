package com.example.moneymoney.model.responsemodel;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StartingBalanceResponse {
    private BigDecimal startingBalance;
}
