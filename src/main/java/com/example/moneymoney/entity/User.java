package com.example.moneymoney.entity;


import com.example.moneymoney.enums.AuthProvider;
import com.example.moneymoney.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder


@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", length = 60)
    private String password;

    private boolean enable =false;


    private Role role;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserAsset> userAssets;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PremiumSubscription premiumSubscription;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;


    private String providerId;
}
