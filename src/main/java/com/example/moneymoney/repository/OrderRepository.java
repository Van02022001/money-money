package com.example.moneymoney.repository;

import com.example.moneymoney.entity.Order;
import com.example.moneymoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user = ?1")
    List<Order> findOrdersByUser(User user);
}
