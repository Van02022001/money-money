package com.example.moneymoney.controller;

import com.example.moneymoney.entity.*;
import com.example.moneymoney.model.requestmodel.IncomeRequestModel;
import com.example.moneymoney.model.responsemodel.ExpenseResponse;
import com.example.moneymoney.model.responsemodel.IncomeResponse;
import com.example.moneymoney.service.IncomeService;
import com.example.moneymoney.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/money-money/users/incomes")
@Slf4j
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    private final UserService userService;



    @PostMapping
    @Operation(summary = "For Creating new Income ")
    public ResponseEntity<IncomeResponse> createIncome(@RequestBody IncomeRequestModel incomeRequest, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        IncomeResponse createdIncome = incomeService.createIncome(incomeRequest, loggedInUser);
        return ResponseEntity.ok(createdIncome);
    }

    @GetMapping("/{incomeId}")
    @Operation(summary = "For getting Income by Id  ")
    public ResponseEntity<IncomeResponse> getIncomeById(@PathVariable Long incomeId, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        IncomeResponse incomeResponse = incomeService.getIncomeById(incomeId, loggedInUser);
        return ResponseEntity.ok(incomeResponse);
    }

    @PutMapping("/{incomeId}")
    @Operation(summary = "For Updating Income ")
    public ResponseEntity<IncomeResponse> updateIncome(@PathVariable Long incomeId, @RequestBody IncomeRequestModel incomeRequest, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        IncomeResponse updatedIncome = incomeService.updateIncome(incomeId, incomeRequest,loggedInUser);
        return ResponseEntity.ok(updatedIncome);
    }

    @DeleteMapping("/{incomeId}")
    @Operation(summary = "For Deleting Income ")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long incomeId, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        incomeService.deleteIncome(incomeId, loggedInUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "For getting list of income ")
    public ResponseEntity<List<IncomeResponse>> getListIncome(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        List<Income> incomes = incomeService.getListIncome(loggedInUser);
        ModelMapper modelMapper = new ModelMapper();
        List<IncomeResponse> expenseResponses = modelMapper.map(incomes, new TypeToken<List<IncomeResponse>>() {
        }.getType());
//        List<IncomeResponse> expenseResponses = incomes.stream()
//                .sorted(Comparator.comparing(Income::getDate).reversed())
//                .map(this::mapToIncomeResponse)
//                .collect(Collectors.toList());
        return ResponseEntity.ok(expenseResponses);
    }
    @GetMapping("/{month}/{year}")
    @Operation(summary = "For getting list of incomes by month and year")
    public ResponseEntity<List<IncomeResponse>> getListIncomeByMonthAndYear(
            @PathVariable("month") int month,
            @PathVariable("year") int year,
            Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        List<Income> incomes = incomeService.getListIncomeByMonthAndYear(loggedInUser, month, year);
        //ModelMapper modelMapper = new ModelMapper();
                List<IncomeResponse> incomeResponses = incomes.stream()
                .sorted(Comparator.comparing(Income::getDate).reversed())
                .map(this::mapToIncomeResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(incomeResponses);
    }


    private IncomeResponse mapToIncomeResponse(Income income) {
        IncomeResponse incomeResponse = new IncomeResponse();
        incomeResponse.setId(income.getId());
        incomeResponse.setIncomeCategoryName(income.getIncomeCategory().getIncomeCategoryName());
        incomeResponse.setDate(income.getDate());
        incomeResponse.setAmount(income.getAmount());
        incomeResponse.setDescription(income.getDescription());
        incomeResponse.setAssetName(income.getAsset().getAssetName());
        return incomeResponse;
    }

    @GetMapping("/total-by-day")
    @Operation(summary = "For getting total income's amount by date - Must input 'year-month-day' ")
    public ResponseEntity<BigDecimal> getTotalAmountByDay(@RequestParam("date") Date date, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = incomeService.getTotalAmountByDay(date, loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-by-week")
    @Operation(summary = "For getting total income's amount by week - Must input 'year-month-day' ")
    public ResponseEntity<BigDecimal> getTotalAmountByWeek(@RequestParam("date") Date date, Principal principal) {
        String userName = principal.getName();
        User loggedInUser = userService.findUserByEmail(userName);
        BigDecimal totalAmount = incomeService.getTotalAmountByWeek(date, loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-by-month")
    @Operation(summary = "For getting total income's amount by month - Must input 'year-month-day' ")
    public ResponseEntity<BigDecimal> getTotalAmountByMonth(@RequestParam("date") Date date, Principal principal) {
        String userName = principal.getName();
        User loggedInUser = userService.findUserByEmail(userName);
        BigDecimal totalAmount = incomeService.getTotalAmountByMonth(date, loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-by-year")
    @Operation(summary = "For getting total income's amount by year - Must input 'year' ")
    public ResponseEntity<BigDecimal> getTotalAmountByYear(@RequestParam("year") int year, Principal principal) {
        String userName = principal.getName();
        User loggedInUser = userService.findUserByEmail(userName);
        BigDecimal totalAmount = incomeService.getTotalAmountByYear(year, loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }


    @GetMapping("/total-amount-by-days")
    @Operation(summary = "For getting total income's amount by day ")
    public ResponseEntity<BigDecimal> getTotalAmountByDay(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = incomeService.getTotalAmountByDays(loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-amount-by-weeks")
    @Operation(summary = "For getting total income's amount by week ")
    public ResponseEntity<BigDecimal> getTotalAmountByWeek(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = incomeService.getTotalAmountByWeeks(loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-amount-by-months")
    @Operation(summary = "For getting total income's amount by month ")
    public ResponseEntity<BigDecimal> getTotalAmountByMonth(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = incomeService.getTotalAmountByMonths(loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-amount-by-years")
    @Operation(summary = "For getting total income's amount by year ")
    public ResponseEntity<BigDecimal> getTotalAmountByYear(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = incomeService.getTotalAmountByYears(loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }


    @GetMapping("/total-incomes-by-month/{month}/{year}")
    @Operation(summary = "Get total income's amount by month and year")
    public ResponseEntity<BigDecimal> getTotalIncomeByMonth(
            @PathVariable("month") int month,
            @PathVariable("year") int year,
            Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = incomeService.getTotalIncomeByMonths(loggedInUser, month, year);
        return ResponseEntity.ok(totalAmount);
    }




}