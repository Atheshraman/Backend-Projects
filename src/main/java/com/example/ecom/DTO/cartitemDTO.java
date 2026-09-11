package com.example.ecom.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class cartitemDTO {
    private Long cartItemId;
    private Integer userId;
    private Ecomdto ecom;
    private Integer quantity;
    private LocalDateTime addedAt;
    private BigDecimal totalPrice;
}