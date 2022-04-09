package com.jolles.restfulservice.repository;

import com.jolles.restfulservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderRepository extends JpaRepository<Order, Long> {
}
