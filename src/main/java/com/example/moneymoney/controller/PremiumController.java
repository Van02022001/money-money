package com.example.moneymoney.controller;

import com.example.moneymoney.entity.PremiumSubscription;
import com.example.moneymoney.entity.User;
import com.example.moneymoney.model.requestmodel.PurchasePremiumRequest;
import com.example.moneymoney.model.responsemodel.PremiumHistory;
import com.example.moneymoney.model.responsemodel.PurchaseResult;
import com.example.moneymoney.repository.PremiumSubscriptionRepository;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/money-money/users/premiums")
@Slf4j
@RequiredArgsConstructor
public class PremiumController {
    private final UserService userService;

    private final PremiumSubscriptionRepository premiumSubscriptionRepository;

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

    @GetMapping("/premium-history")
    public ResponseEntity<List<PremiumHistory>> getPremiumHistory(Principal principal) {
        // Lấy thông tin người dùng từ Principal hoặc thông tin xác thực khác
        String username = principal.getName();
        User user = userService.findUserByEmail(username);

        // Kiểm tra người dùng và lấy lịch sử mua premium
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<PremiumSubscription> premiumHistory = premiumSubscriptionRepository.findByUser(user);

        // Chuyển đổi lịch sử mua premium thành định dạng JSON response
        List<PremiumHistory> response = new ArrayList<>();
        for (PremiumSubscription subscription : premiumHistory) {
            PremiumHistory history = new PremiumHistory(
                    subscription.getStartDate(),
                    subscription.getEndDate(),
                    subscription.getPackageType()
            );
            response.add(history);
        }

        return ResponseEntity.ok(response);
    }


}

