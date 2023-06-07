package com.example.moneymoney.controller;

import com.example.moneymoney.entity.User;
import com.example.moneymoney.jwt.userprincipal.Principal;
import com.example.moneymoney.model.requestmodel.PurchasePremiumRequest;
import com.example.moneymoney.model.responsemodel.PurchaseResult;
import com.example.moneymoney.service.UserService;
import com.example.moneymoney.utils.ResponseObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/money-money/users/premiums")
@Slf4j
@RequiredArgsConstructor
public class PremiumController {
    private  final UserService userService;


    @PostMapping("/{userId}/premiums")
    public ResponseEntity<PurchaseResult> purchasePremium(@RequestBody PurchasePremiumRequest purchasePremiumRequest, Principal principal) {
        String userName = principal.getName();
        User  loggedInUser = userService.findUserByEmail(userName);

        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PurchaseResult("error", "User not found"));
        }

        PurchaseResult purchaseResult = userService.purchasePremium(purchasePremiumRequest, loggedInUser);

        if (purchaseResult.getStatus().equals("success")) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(purchaseResult);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(purchaseResult);
        }
    }

}
