package com.example.moneymoney.controller;

import com.example.moneymoney.entity.User;
import com.example.moneymoney.jwt.userprincipal.Principal;
import com.example.moneymoney.model.requestmodel.CalculateInterestRequest;
import com.example.moneymoney.service.InterestService;
import com.example.moneymoney.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/v1/money-money/users/interests")
@Slf4j
@RequiredArgsConstructor
public class InterestController {

    private final InterestService interestService;

    private final UserService userService;

    @GetMapping("/interest-rates")
    public ResponseEntity<List<BigDecimal>> getInterestRates() {
        List<BigDecimal> interestRates = interestService.getAllInterestRates();
        return ResponseEntity.ok(interestRates);
    }

    @GetMapping("/term-months")
    public ResponseEntity<List<Integer>> getTermMonths() {
        List<Integer> termMonths = interestService.getAllTermMonths();
        return ResponseEntity.ok(termMonths);
    }

    @PostMapping("/total-amount-received")
    public ResponseEntity<BigDecimal> calculateTotalReceived(@RequestBody CalculateInterestRequest request) {
        BigDecimal currentAmount = request.getCurrentAmount();
        List<BigDecimal> interestRates = request.getInterestRate();
        List<Integer> termMonths = request.getTermMonths();

        BigDecimal expectedInterest = interestService.calculateExpectedInterest(currentAmount, interestRates, termMonths);
        BigDecimal totalAmountAndInterest = currentAmount.add(expectedInterest);
        return ResponseEntity.ok(totalAmountAndInterest);
    }

    @PostMapping("/total-interest")
    public ResponseEntity<BigDecimal> calculateInterest(@RequestBody CalculateInterestRequest request) {
        BigDecimal currentAmount = request.getCurrentAmount();
        List<BigDecimal> interestRates = request.getInterestRate();
        List<Integer> termMonths = request.getTermMonths();

        BigDecimal expectedInterest = interestService.calculateExpectedInterest(currentAmount, interestRates, termMonths);

        return ResponseEntity.ok(expectedInterest);
    }



}
