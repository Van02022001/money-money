package com.example.moneymoney.model.responsemodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PremiumHistory {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String packageType;
}
