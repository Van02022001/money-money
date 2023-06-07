package com.example.moneymoney.repository;

import com.example.moneymoney.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    Asset findByAssetName(String assetName);

    Asset findAssetByAssetId(Long id);
    List<Asset> findAll();
}