package com.example.ecom.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private users user;

    @ManyToOne
    @JoinColumn(name = "ecom_id")
    private Ecom ecom;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "added_at")
    private LocalDateTime addedAt = LocalDateTime.now();

    public BigDecimal getTotalPrice() {
        return ecom.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
