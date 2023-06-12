package com.example.moneymoney.controller;

import com.example.moneymoney.entity.Expense;

import com.example.moneymoney.entity.User;
import com.example.moneymoney.model.requestmodel.ExpenseRequestModel;
import com.example.moneymoney.model.responsemodel.ExpenseResponse;
import com.example.moneymoney.model.responsemodel.IncomeResponse;
import com.example.moneymoney.service.ExpenseService;
import com.example.moneymoney.service.UserService;
import com.example.moneymoney.utils.ResponseObject;
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
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/money-money/users/expenses")
@Slf4j
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;
    private final UserService userService;

    @PostMapping
    @Operation(summary = "For creating new expense ")
    public ResponseEntity<ExpenseResponse> createExpense(@RequestBody ExpenseRequestModel expenseRequest, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);

        ExpenseResponse createdExpense = expenseService.createExpense(expenseRequest, loggedInUser);
        return ResponseEntity.ok(createdExpense);
    }

    @PutMapping("/{expenseId}")
    @Operation(summary = "For updating expense by expenseId")
    public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long expenseId, @RequestBody ExpenseRequestModel expenseRequest, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        ExpenseResponse updatedExpense = expenseService.updateExpense(expenseId, expenseRequest, loggedInUser);
        return ResponseEntity.ok(updatedExpense);
    }


    @GetMapping("/{expenseId}")
    @Operation(summary = "For getting  expense by id")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long expenseId,  Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        ExpenseResponse expenseResponse = expenseService.getExpenseById(expenseId, loggedInUser);
        return ResponseEntity.ok(expenseResponse);
    }


    @DeleteMapping("/{expenseId}")
    @Operation(summary = "For deleting one expense in list by id ")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long expenseId, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        expenseService.deleteExpense(expenseId, loggedInUser);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    @Operation(summary = "For getting list of expense ")
    public ResponseEntity<List<ExpenseResponse>> getListExpense(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        List<Expense> expenses = expenseService.getListExpense(loggedInUser);
        ModelMapper modelMapper = new ModelMapper();
        List<ExpenseResponse> expenseResponses = modelMapper.map(expenses, new TypeToken<List<ExpenseResponse>>() {
        }.getType());
//        List<ExpenseResponse> expenseResponses = expenses.stream()
//                .sorted(Comparator.comparing(Expense::getDate).reversed())
//                .map(this::mapToExpenseResponse)
//                .collect(Collectors.toList());
        return ResponseEntity.ok(expenseResponses);
    }
    @GetMapping("/total/{month}/{year}")
    @Operation(summary = "Get list of expenses by month and year")
    public ResponseEntity<List<ExpenseResponse>> getListExpenseByMonthAndYear(
            @PathVariable("month") int month,
            @PathVariable("year") int year,
            Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);

        List<Expense> expenses = expenseService.getListExpenseByMonthAndYear(loggedInUser, month, year);

        List<ExpenseResponse> expenseResponses = expenses.stream()
                .map(this::mapToExpenseResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(expenseResponses);
    }


    private ExpenseResponse mapToExpenseResponse(Expense expense) {
        ExpenseResponse expenseResponse = new ExpenseResponse();
        expenseResponse.setExpenseCategoryName(expense.getExpenseCategory().getExpenseCategoryName());
        expenseResponse.setDate(expense.getDate());
        expenseResponse.setAmount(expense.getAmount());
        expenseResponse.setDescription(expense.getDescription());
        expenseResponse.setAssetName(expense.getAsset().getAssetName());
        return expenseResponse;
    }


    @GetMapping("/total-by-day")

    @Operation(summary = "For getting total expense's amount by week - Must input 'year-month-day' ")
    public ResponseEntity<BigDecimal> getTotalAmountByDay(@RequestParam("date") Date date, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = expenseService.getTotalAmountByDay(date, loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-by-week")
    @Operation(summary = "For getting total expense's amount by week - Must input 'year-month-day' ")
    public ResponseEntity<BigDecimal> getTotalAmountByWeek(@RequestParam("date") Date date, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = expenseService.getTotalAmountByWeek(date, loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-by-month")
    @Operation(summary = "For getting total expense's amount by month - Must input 'year-month-day' ")
    public ResponseEntity<BigDecimal> getTotalAmountByMonth(@RequestParam("date") Date date, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = expenseService.getTotalAmountByMonth(date, loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-by-year")
    @Operation(summary = "For getting total expense's amount by year - Must input 'year-month-day' ")
    public ResponseEntity<BigDecimal> getTotalAmountByYear(@RequestParam("year") int year, Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = expenseService.getTotalAmountByYear(year, loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }
    @GetMapping("/total-amount-by-day")
    @Operation(summary = "For getting total expense's amount by day ")
    public ResponseEntity<BigDecimal> getTotalAmountByDay(Principal principal) {
    String username = principal.getName();
    User loggedInUser = userService.findUserByEmail(username);
    BigDecimal totalAmount = expenseService.getTotalAmountByDays(loggedInUser);
    return ResponseEntity.ok(totalAmount);
}

    @GetMapping("/total-amount-by-week")
    @Operation(summary = "For getting total expense's amount by week ")
    public ResponseEntity<BigDecimal> getTotalAmountByWeek(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = expenseService.getTotalAmountByWeeks(loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-amount-by-month")
    @Operation(summary = "For getting total expense's amount by month ")
    public ResponseEntity<BigDecimal> getTotalAmountByMonth(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = expenseService.getTotalAmountByMonths(loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }

    @GetMapping("/total-amount-by-year")
    @Operation(summary = "For getting total expense's amount by day ")
    public ResponseEntity<BigDecimal> getTotalAmountByYear(Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);
        BigDecimal totalAmount = expenseService.getTotalAmountByYears(loggedInUser);
        return ResponseEntity.ok(totalAmount);
    }


    @GetMapping("/total-expenses-by-month")
    @Operation(summary = "For getting total expense's amount by month")
    public ResponseEntity<BigDecimal> getTotalAmountByMonth(
            @RequestParam("month") int month,
            @RequestParam("year") int year,
            Principal principal) {
        String username = principal.getName();
        User loggedInUser = userService.findUserByEmail(username);

        BigDecimal totalAmount = expenseService.getTotalExpensesByMonths(loggedInUser, month, year);
        return ResponseEntity.ok(totalAmount);
    }


}
