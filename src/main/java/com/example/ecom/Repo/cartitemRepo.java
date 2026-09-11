package com.example.ecom.Repo;

import com.example.ecom.model.CartItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface cartitemRepo extends JpaRepository<CartItems,Long> {
    List<CartItems> findByUserId(Integer id);
    Optional<CartItems> findByUser_IdAndEcom_EcomId(
            Integer userId,
            Long ecomId
    );
    void deleteByUserId(Integer id);
}
