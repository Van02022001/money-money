package com.example.moneymoney.repository;

import com.example.moneymoney.entity.Expense;
import com.example.moneymoney.entity.Income;

import com.example.moneymoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    Optional<Income> findByIdAndUser(Long expenseId, User user);

    List<Income> findAllByUserOrderByDateDesc(User user);


    @Query("SELECT SUM(i.amount) FROM Income i WHERE DATE(i.date) = :date AND i.user = :loggedInUser")
    BigDecimal getTotalAmountByDay(@Param("date") Date date, @Param("loggedInUser") User loggedInUser);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE WEEK(i.date) = WEEK(:date) AND YEAR(i.date) = YEAR(:date) AND i.user = :loggedInUser")
    BigDecimal getTotalAmountByWeek(@Param("date") Date date, @Param("loggedInUser") User loggedInUser);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE MONTH(i.date) = MONTH(:date) AND YEAR(i.date) = YEAR(:date) AND i.user = :loggedInUser")
    BigDecimal getTotalAmountByMonth(@Param("date") Date date, @Param("loggedInUser") User loggedInUser);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE YEAR(i.date) = :year AND i.user = :loggedInUser")
    BigDecimal getTotalAmountByYear(@Param("year") int year, @Param("loggedInUser") User loggedInUser);






}