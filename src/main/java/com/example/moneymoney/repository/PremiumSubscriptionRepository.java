package com.example.moneymoney.repository;

import com.example.moneymoney.entity.PremiumSubscription;
import com.example.moneymoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PremiumSubscriptionRepository extends JpaRepository< PremiumSubscription, Long> {

    PremiumSubscription save(PremiumSubscription premiumSubscription);

    List<PremiumSubscription> findByUser(User user);
}
