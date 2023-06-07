package com.example.moneymoney.model.requestmodel;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchasePremiumRequest {

    private Long assetId;

    private BigDecimal premiumPrice;
}
