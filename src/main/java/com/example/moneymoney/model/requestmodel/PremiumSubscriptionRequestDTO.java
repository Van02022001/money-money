package com.example.moneymoney.model.requestmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PremiumSubscriptionRequestDTO {
    private Long subscriptionId;
    private int quantity;
}
