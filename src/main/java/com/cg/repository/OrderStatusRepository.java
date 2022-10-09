package com.cg.repository;

import com.cg.model.Category;
import com.cg.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

}
