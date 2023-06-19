package com.example.moneymoney.service;

import com.example.moneymoney.entity.User;
import com.example.moneymoney.model.requestmodel.UserAssetRequest;
import com.example.moneymoney.utils.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface UserAssetService {

    ResponseEntity<ResponseObject> createUserAsset(UserAssetRequest userAssetRequest, User loggedInUser);

    void subtractValueFromUserAsset(User user, BigDecimal value);
}
