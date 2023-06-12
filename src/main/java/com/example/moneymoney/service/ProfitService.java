package com.example.moneymoney.service;

import com.example.moneymoney.entity.User;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

public interface ProfitService {
    BigDecimal getProfitByDay(Date date, User loggedInUser );
    BigDecimal getProfitByWeek(Date date, User loggedInUser);
    BigDecimal getProfitByMonth(Date date, User loggedInUser);
    BigDecimal getProfitByYear(int year, User loggedInUser);



    BigDecimal getTotalAmountByDays(User loggedInUser);
    BigDecimal getTotalAmountByWeeks(User loggedInUser);
    BigDecimal getTotalAmountByMonths(User loggedInUser);
    BigDecimal getTotalAmountByYears(User loggedInUser);

    BigDecimal getStartingBalanceOfMonth(User loggedInUser, int month, int year);

    BigDecimal getTotalProfitByDateRange(LocalDate   startDate, LocalDate  endDate, User loggedInUser);

    BigDecimal getStartingBalanceOfYear(int year, User loggedInUser);

    BigDecimal getProfitOfMonth(User loggedInUser, int month, int year);

}
