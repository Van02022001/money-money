package com.example.moneymoney.controller;

import com.example.moneymoney.entity.User;
import com.example.moneymoney.model.requestmodel.PurchasePremiumRequest;
import com.example.moneymoney.model.responsemodel.PurchaseResult;
import com.example.moneymoney.service.UserService;
import com.example.moneymoney.utils.ResponseObject;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/money-money/users/premiums")
@Slf4j
@RequiredArgsConstructor
public class PremiumController {
    private final UserService userService;


@PostMapping("/purchase")
@Operation(summary = "Purchase Premium")
public ResponseEntity<PurchaseResult> purchasePremium(@RequestBody PurchasePremiumRequest purchasePremiumRequest, Principal principal) {
    if (purchasePremiumRequest.getPackageType() == null) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PurchaseResult(HttpStatus.BAD_REQUEST.toString(), "package_type_required"));
    }

    PurchaseResult purchaseResult = userService.purchasePremium(purchasePremiumRequest, principal);

    if (purchaseResult.getStatus().equals(HttpStatus.OK.toString())) {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseResult);
    } else if (purchaseResult.getStatus().equals(HttpStatus.BAD_REQUEST.toString())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(purchaseResult);
    } else {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(purchaseResult);
    }
}


}

