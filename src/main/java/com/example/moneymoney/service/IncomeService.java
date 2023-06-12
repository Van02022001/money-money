package com.example.moneymoney.service;

import com.example.moneymoney.entity.Expense;
import com.example.moneymoney.entity.Income;
import com.example.moneymoney.entity.User;
import com.example.moneymoney.model.requestmodel.ExpenseRequestModel;
import com.example.moneymoney.model.requestmodel.IncomeRequestModel;
import com.example.moneymoney.model.responsemodel.ExpenseResponse;
import com.example.moneymoney.model.responsemodel.IncomeResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IncomeService {

    IncomeResponse createIncome(IncomeRequestModel incomeRequest, User loggedInUser);
    IncomeResponse getIncomeById(Long incomeId,  User loggedInUser);
    IncomeResponse updateIncome(Long incomeId, IncomeRequestModel incomeRequest,  User loggedInUser);

    void deleteIncome(Long incomeId, User loggedInUser);

    List<Income> getListIncome(User loggedInUser);

    List<Income> getListIncomeByMonthAndYear(User loggedInUser, int month, int year);
    BigDecimal getTotalAmountByDay(Date date, User loggedInUser);

    BigDecimal getTotalAmountByWeek(Date date, User loggedInUser);

    BigDecimal getTotalAmountByMonth(Date date, User loggedInUser);

    BigDecimal getTotalAmountByYear(int year, User loggedInUser);



    BigDecimal getTotalAmountByDays(User loggedInUser);
    BigDecimal getTotalAmountByWeeks(User loggedInUser);
    BigDecimal getTotalAmountByMonths(User loggedInUser);
    BigDecimal getTotalAmountByYears(User loggedInUser);


    BigDecimal getTotalIncomeByMonths(User loggedInUser, int month, int year);
}
