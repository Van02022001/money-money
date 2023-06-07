package com.example.moneymoney.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "asset")

public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private Long assetId;


    @Column(name = "asset_name", nullable = false)
    private String assetName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_type_id")
    private AssetType assetType;

    @Column(name = "description")
    private String description;



    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    private List<Expense> expenses;



    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    private List<Income> incomes;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL)
    private List<UserAsset> userAssets;

}
