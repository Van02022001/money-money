package com.example.moneymoney.controller;

import com.example.moneymoney.entity.User;
import com.example.moneymoney.jwt.userprincipal.Principal;
import com.example.moneymoney.model.requestmodel.UserAssetRequest;
import com.example.moneymoney.service.UserAssetService;
import com.example.moneymoney.service.UserService;
import com.example.moneymoney.utils.ResponseObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/money-money/users/users-asset")
@Slf4j
@RequiredArgsConstructor
public class UserAssetController {

    private final UserAssetService userAssetService;

    private final UserService userService;
    @PostMapping("")
    public ResponseEntity<ResponseObject> createUserAsset(@RequestBody UserAssetRequest userAssetRequest, @AuthenticationPrincipal Principal principal) {
        String username = principal.getUsername();
        User loggedInUser = userService.findUserByEmail(username);

        return userAssetService.createUserAsset(userAssetRequest, loggedInUser);
    }


}
