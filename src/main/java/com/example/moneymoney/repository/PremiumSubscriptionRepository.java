package com.example.moneymoney.repository;

import com.example.moneymoney.entity.PremiumSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremiumSubscriptionRepository extends JpaRepository< PremiumSubscription, Long> {

    PremiumSubscription save(PremiumSubscription premiumSubscription);
}
