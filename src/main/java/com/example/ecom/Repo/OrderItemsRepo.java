package com.example.ecom.Repo;

import com.example.ecom.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemsRepo extends JpaRepository<OrderItems,Long> {
    List<OrderItems> findByOrder_OrderId(Long orderId);
}
