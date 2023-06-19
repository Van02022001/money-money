package com.example.moneymoney.repository;

import com.example.moneymoney.entity.Asset;
import com.example.moneymoney.entity.User;
import com.example.moneymoney.entity.UserAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAssetRepository extends JpaRepository<UserAsset, Long> {
    UserAsset findUserAssetByUserAndAsset(User user, Asset asset);

    UserAsset save(UserAsset userAsset);
    UserAsset findByUser(User user);
}
