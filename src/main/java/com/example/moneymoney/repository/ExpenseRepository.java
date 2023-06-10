package com.example.moneymoney.repository;

import com.example.moneymoney.entity.Expense;
import com.example.moneymoney.entity.ExpenseCategory;
import com.example.moneymoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {


    Optional<Expense> findByIdAndUser(Long expenseId, User user);

    List<Expense> findAllByUserOrderByDateDesc(User user);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE DATE(e.date) = :date AND e.user = :loggedInUser")
    BigDecimal getTotalAmountByDay(@Param("date") Date date, @Param("loggedInUser") User loggedInUser);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE WEEK(e.date) = WEEK(:date) AND YEAR(e.date) = YEAR(:date) AND e.user = :loggedInUser")
    BigDecimal getTotalAmountByWeek(@Param("date") Date date, @Param("loggedInUser") User loggedInUser);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE MONTH(e.date) = MONTH(:date) AND YEAR(e.date) = YEAR(:date) AND e.user = :loggedInUser")
    BigDecimal getTotalAmountByMonth(@Param("date") Date date, @Param("loggedInUser") User loggedInUser);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE YEAR(e.date) = :year AND e.user = :loggedInUser")
    BigDecimal getTotalAmountByYear(@Param("year") int year, @Param("loggedInUser") User loggedInUser);



}