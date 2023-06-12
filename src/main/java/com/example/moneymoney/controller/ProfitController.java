package com.example.moneymoney.controller;

import com.example.moneymoney.entity.User;
import com.example.moneymoney.service.ExpenseService;
import com.example.moneymoney.service.IncomeService;
import com.example.moneymoney.service.ProfitService;
import com.example.moneymoney.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

@RestController
@RequestMapping("api/v1/money-money/users/profits")
@Slf4j
@RequiredArgsConstructor
public class ProfitController {
    private final ProfitService profitService;

    private final UserService userService;

    private final IncomeService incomeService;
    private final ExpenseService    expenseService;

    @GetMapping("/total-by-day")
    @Operation(summary = "For getting total profit's amount by day - Must input 'year-month-day' ")
    public ResponseEntity<BigDecimal> getProfitByDay(@RequestParam("date") Date date, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal profit = profitService.getProfitByDay(date, loggedInUser);
        return ResponseEntity.ok(profit);
    }

    @GetMapping("/total-by-week")
    @Operation(summary = "For getting total profit's amount by week - Must input 'year-month-day' ")
    public ResponseEntity<BigDecimal> getProfitByWeek(@RequestParam("date") Date date, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal profit = profitService.getProfitByWeek(date, loggedInUser);
        return ResponseEntity.ok(profit);
    }

    @GetMapping("/total-by-month")
    @Operation(summary = "For getting total profit's amount by month - Must input 'year-month-day' ")
    public ResponseEntity<BigDecimal> getProfitByMonth(@RequestParam("date") Date date, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal profit = profitService.getProfitByMonth(date, loggedInUser);
        return ResponseEntity.ok(profit);
    }

    @GetMapping("/total-by-year")
    @Operation(summary = "For getting total profit's amount by year - Must input 'year' ")
    public ResponseEntity<BigDecimal> getProfitByYear(@RequestParam("year") int year, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal profit = profitService.getProfitByYear(year, loggedInUser);
        return ResponseEntity.ok(profit);
    }

    @GetMapping("/total-amount-by-day")
    @Operation(summary = "For getting total profit's amount by year ")
    public ResponseEntity<BigDecimal> getTotalProfitByDay(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalProfit = profitService.getTotalAmountByDays(loggedInUser);
        return ResponseEntity.ok(totalProfit);
    }

    @GetMapping("/total-amount-by-week")
    @Operation(summary = "For getting total profit's amount by week ")

    public ResponseEntity<BigDecimal> getTotalProfitByWeek(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalProfit = profitService.getTotalAmountByWeeks(loggedInUser);
        return ResponseEntity.ok(totalProfit);
    }

    @GetMapping("/total-amount-by-month")
    @Operation(summary = "For getting total profit's amount by month ")
    public ResponseEntity<BigDecimal> getTotalProfitByMonth(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalProfit = profitService.getTotalAmountByMonths(loggedInUser);
        return ResponseEntity.ok(totalProfit);
    }

    @GetMapping("/total-amount-by-year")
    @Operation(summary = "For getting total profit's amount by year ")
    public ResponseEntity<BigDecimal> getTotalProfitByYear(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalProfit = profitService.getTotalAmountByYears(loggedInUser);
        return ResponseEntity.ok(totalProfit);
    }

    @GetMapping("/starting-balance-of-month")
    @Operation(summary = "For getting the starting balance of a specific month")
    public ResponseEntity<BigDecimal> getStartingBalanceOfMonth(
            @RequestParam("month") int month,
            @RequestParam("year") int year,
            Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal startingBalance = profitService.getStartingBalanceOfMonth(loggedInUser, month, year);
        return ResponseEntity.ok(startingBalance);
    }

    @GetMapping("/starting-balance-of-year")
    @Operation(summary = "For getting the starting balance of a specific year")
    public ResponseEntity<BigDecimal> getStartingBalanceOfYear(
            @RequestParam("year") int year,
            Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);

        BigDecimal startingBalance = profitService.getStartingBalanceOfYear(year, loggedInUser);

        return ResponseEntity.ok(startingBalance);
    }
    @GetMapping("/ending-balance-of-month")
    @Operation(summary = "For getting the ending balance of a specific month")
    public ResponseEntity<BigDecimal> getEndingBalanceOfMonth(
            @RequestParam("month") int month,
            @RequestParam("year") int year,
            Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);

        BigDecimal startingBalance = profitService.getStartingBalanceOfMonth(loggedInUser, month, year);
        BigDecimal profit = profitService.getProfitOfMonth(loggedInUser, month, year);
        BigDecimal endingBalance = startingBalance.add(profit);

        return ResponseEntity.ok(endingBalance);
    }

    @GetMapping("/profit-of-month")
    @Operation(summary = "Get the profit of a specific month")
    public ResponseEntity<BigDecimal> getProfitOfMonth(
            @RequestParam("month") int month,
            @RequestParam("year") int year,
            Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal profitOfMonth = profitService.getProfitOfMonth(loggedInUser, month, year);
        return ResponseEntity.ok(profitOfMonth);
    }






}
