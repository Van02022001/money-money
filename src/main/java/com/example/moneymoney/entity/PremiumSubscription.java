package com.example.moneymoney.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Table(name = "premium_subscription")
public class PremiumSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subscription_id")
    private Long id;

    @Column(name = "package_type")
    private String packageName;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;


    @Column(name = "unit_stock")
    private int unitStock;

    @Column(name = "durian_in_months")
    private int durationInMonths;


    @Column(name = "status")
    private boolean status;



    @OneToMany(mappedBy = "premiumSubscription", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

}