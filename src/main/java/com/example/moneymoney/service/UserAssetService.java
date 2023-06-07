package com.example.moneymoney.service;

import com.example.moneymoney.entity.User;
import com.example.moneymoney.model.requestmodel.UserAssetRequest;
import com.example.moneymoney.utils.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface UserAssetService {

    ResponseEntity<ResponseObject> createUserAsset(UserAssetRequest userAssetRequest, User loggedInUser);

}
