package com.example.moneymoney.service.Impl;

import com.example.moneymoney.entity.User;
import com.example.moneymoney.repository.ExpenseRepository;
import com.example.moneymoney.repository.IncomeRepository;

import com.example.moneymoney.service.ProfitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ProfitServiceImpl implements ProfitService {
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository  expenseRepository;



    @Override
    public BigDecimal getProfitByDay(Date date, User loggedInUser) {
        BigDecimal totalIncome = incomeRepository.getTotalAmountByDay(date, loggedInUser);
        BigDecimal totalExpense = expenseRepository.getTotalAmountByDay(date, loggedInUser);
        totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;
        totalExpense = (totalExpense != null) ? totalExpense : BigDecimal.ZERO;
        return totalIncome.subtract(totalExpense);
    }

    @Override
    public BigDecimal getProfitByWeek(Date date, User loggedInUser) {
        BigDecimal totalIncome = incomeRepository.getTotalAmountByWeek(date, loggedInUser);
        BigDecimal totalExpense = expenseRepository.getTotalAmountByWeek(date, loggedInUser);
        totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;
        totalExpense = (totalExpense != null) ? totalExpense : BigDecimal.ZERO;
        return totalIncome.subtract(totalExpense);
    }

    @Override
    public BigDecimal getProfitByMonth(Date date, User loggedInUser) {
        BigDecimal totalIncome = incomeRepository.getTotalAmountByMonth(date, loggedInUser);
        BigDecimal totalExpense = expenseRepository.getTotalAmountByMonth(date, loggedInUser);
        totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;
        totalExpense = (totalExpense != null) ? totalExpense : BigDecimal.ZERO;
        return totalIncome.subtract(totalExpense);
    }

    @Override
    public BigDecimal getProfitByYear(int year, User loggedInUser) {
        BigDecimal totalIncome = incomeRepository.getTotalAmountByYear(year, loggedInUser);
        BigDecimal totalExpense = expenseRepository.getTotalAmountByYear(year, loggedInUser);

        totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;
        totalExpense = (totalExpense != null) ? totalExpense : BigDecimal.ZERO;
        return totalIncome.subtract(totalExpense);
    }

    @Override
    public BigDecimal getTotalAmountByDays(User loggedInUser) {
        Date currentDate = new Date();
        BigDecimal totalIncome = incomeRepository.getTotalAmountByDay(currentDate, loggedInUser);
        BigDecimal totalExpense = expenseRepository.getTotalAmountByDay(currentDate,loggedInUser);
        totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;
        totalExpense = (totalExpense != null) ? totalExpense : BigDecimal.ZERO;
        return totalIncome.subtract(totalExpense);
    }

    @Override
    public BigDecimal getTotalAmountByWeeks(User loggedInUser) {
        Date currentDate = new Date();
        BigDecimal totalIncome = incomeRepository.getTotalAmountByWeek(currentDate, loggedInUser);
        BigDecimal totalExpense = expenseRepository.getTotalAmountByWeek(currentDate,loggedInUser);
        totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;
        totalExpense = (totalExpense != null) ? totalExpense : BigDecimal.ZERO;
        return totalIncome.subtract(totalExpense);
    }

    @Override
    public BigDecimal getTotalAmountByMonths(User loggedInUser) {
        Date currentDate = new Date();
        BigDecimal totalIncome = incomeRepository.getTotalAmountByMonth(currentDate, loggedInUser);
        BigDecimal totalExpense = expenseRepository.getTotalAmountByMonth(currentDate,loggedInUser);
        totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;
        totalExpense = (totalExpense != null) ? totalExpense : BigDecimal.ZERO;
        return totalIncome.subtract(totalExpense);
    }

    @Override
    public BigDecimal getTotalAmountByYears(User loggedInUser) {
        Date currentYear = new Date();
        BigDecimal totalIncome = incomeRepository.getTotalAmountByMonth(currentYear, loggedInUser);
        BigDecimal totalExpense = expenseRepository.getTotalAmountByMonth(currentYear,loggedInUser);
        totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;
        totalExpense = (totalExpense != null) ? totalExpense : BigDecimal.ZERO;
        return totalIncome.subtract(totalExpense);
    }

    @Override
    public BigDecimal getStartingBalanceOfMonth(User loggedInUser , int month, int year) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth()).minusMonths(1);

        BigDecimal totalProfit = BigDecimal.ZERO;
        for (int m = 1; m < month; m++) {
            LocalDate monthStart = LocalDate.of(year, m, 1);
            LocalDate monthEnd = monthStart.with(TemporalAdjusters.lastDayOfMonth());

            BigDecimal profit = getTotalProfitByDateRange(monthStart, monthEnd, loggedInUser);
            totalProfit = totalProfit.add(profit);
        }

        return totalProfit;
    }

    @Override
    public BigDecimal getTotalProfitByDateRange(LocalDate  startDate, LocalDate   endDate, User loggedInUser) {
        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay().plusDays(1));

        BigDecimal totalIncome = incomeRepository.getTotalIncomeByDateRange(startTimestamp, endTimestamp, loggedInUser);
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByDateRange(startTimestamp, endTimestamp, loggedInUser);

        totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;
        totalExpense = (totalExpense != null) ? totalExpense : BigDecimal.ZERO;

        return totalIncome.subtract(totalExpense);
    }


    @Override
    public BigDecimal getStartingBalanceOfYear(int year, User loggedInUser) {
        BigDecimal totalProfit = BigDecimal.ZERO;

        for (int y = 1900; y < year; y++) {
            BigDecimal profit = getProfitByYears(y , loggedInUser);
            totalProfit = totalProfit.add(profit);
        }

        return totalProfit;
    }

    private BigDecimal getProfitByYears(int year, User loggedInUser) {
        LocalDate startDate = LocalDate.of(year, 1, 1);
        LocalDate endDate = LocalDate.of(year, 12, 31);

        return getTotalProfitByDateRange(startDate, endDate, loggedInUser);
    }


    @Override
    public BigDecimal getProfitOfMonth(User loggedInUser, int month, int year) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());

        Timestamp startTimestamp = Timestamp.valueOf(startDate.atStartOfDay());
        Timestamp endTimestamp = Timestamp.valueOf(endDate.atStartOfDay().plusDays(1));

        BigDecimal totalIncome = incomeRepository.getTotalIncomeByDateRange(startTimestamp, endTimestamp, loggedInUser);
        BigDecimal totalExpense = expenseRepository.getTotalExpenseByDateRange(startTimestamp, endTimestamp, loggedInUser);

        totalIncome = (totalIncome != null) ? totalIncome : BigDecimal.ZERO;
        totalExpense = (totalExpense != null) ? totalExpense : BigDecimal.ZERO;

        return totalIncome.subtract(totalExpense);
    }


}
