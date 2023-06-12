package com.example.moneymoney.service;

import com.example.moneymoney.entity.*;
import com.example.moneymoney.model.requestmodel.ExpenseRequestModel;
import com.example.moneymoney.model.responsemodel.ExpenseResponse;
import com.example.moneymoney.utils.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ExpenseService {
    ExpenseResponse  createExpense(ExpenseRequestModel expenseRequest, User loggedInUser);

    ExpenseResponse getExpenseById(Long expenseId, User loggedInUser);

    void deleteExpense(Long expenseId, User loggedInUser);

    ExpenseResponse updateExpense(Long expenseId, ExpenseRequestModel expenseRequest, User loggedInUser);

    List<Expense> getListExpense(User loggedInUser);

    List<Expense> getListExpenseByMonthAndYear(User loggedInUser, int month, int year);


    BigDecimal getTotalAmountByDay(Date date, User loggedInUser);

    BigDecimal getTotalAmountByWeek(Date date, User loggedInUser);
    public BigDecimal getTotalAmountByMonth(Date date, User loggedInUser);

    public BigDecimal getTotalAmountByYear(int year, User loggedInUser);


    BigDecimal getTotalAmountByDays(User loggedInUser);
    BigDecimal getTotalAmountByWeeks(User loggedInUser);
    BigDecimal getTotalAmountByMonths(User loggedInUser);
    BigDecimal getTotalAmountByYears(User loggedInUser);


    BigDecimal getTotalExpensesByMonths(User loggedInUser, int month, int year);

}
