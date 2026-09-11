package com.example.ecom.Repo;

import com.example.ecom.model.Orders;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface orderRepo extends JpaRepository<Orders,Long> {
    List<Orders> findByUserId(Integer id);
    @EntityGraph(attributePaths = {"orderItems", "orderItems.ecom"})
    @Query("SELECT o FROM Orders o WHERE o.user.id = :userId")
    List<Orders> findByUserIdWithItems(@Param("userId") Integer userId);
    @EntityGraph(attributePaths = {"orderItems", "orderItems.ecom", "user"})
    List<Orders> findAllByOrderByOrderDateDesc();
}
