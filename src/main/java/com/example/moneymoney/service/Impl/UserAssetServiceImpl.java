package com.example.moneymoney.service.Impl;

import com.example.moneymoney.entity.Asset;
import com.example.moneymoney.entity.User;
import com.example.moneymoney.entity.UserAsset;
import com.example.moneymoney.exception.NotFoundException;
import com.example.moneymoney.model.requestmodel.UserAssetRequest;
import com.example.moneymoney.repository.AssetRepository;
import com.example.moneymoney.repository.UserAssetRepository;
import com.example.moneymoney.repository.UserRepository;
import com.example.moneymoney.service.UserAssetService;
import com.example.moneymoney.utils.ResponseObject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAssetServiceImpl implements UserAssetService {

    private final UserAssetRepository userAssetRepository;

    private final UserRepository userRepository;
    private final AssetRepository assetRepository;

    @Override
    public ResponseEntity<ResponseObject> createUserAsset(UserAssetRequest userAssetRequest, User loggedInUser) {
        User user = userRepository.findById(loggedInUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Asset asset = assetRepository.findById(userAssetRequest.getAssetId())
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        UserAsset userAsset = UserAsset.builder()
                .user(user)
                .asset(asset)
                .value(userAssetRequest.getValue())
                .build();

        userAssetRepository.save(userAsset);

        ResponseObject responseObject = new ResponseObject(HttpStatus.CREATED.toString(), "User asset created successfully", null, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseObject);
    }

    @Override
    public void subtractValueFromUserAsset(User user, BigDecimal value) {
        UserAsset userAsset = userAssetRepository.findByUser(user);

        if (userAsset == null) {
            // Xử lý khi không tìm thấy UserAsset cho User
            throw new NotFoundException("UserAsset not found");
        }

        BigDecimal currentValue = userAsset.getValue();

        if (value.compareTo(currentValue) > 0) {
            // Xử lý khi giá trị gói Premium lớn hơn giá trị hiện tại trong UserAsset
            throw new IllegalArgumentException("Insufficient value in UserAsset");
        }

        BigDecimal newValue = currentValue.subtract(value);
        userAsset.setValue(newValue);

        userAssetRepository.save(userAsset);
    }


}