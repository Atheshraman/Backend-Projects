package com.example.ecom.DTO;

import com.example.ecom.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class orderdto {
    private Long orderId;
    private Integer userId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private String deliveryAddress;
    private List<orderitemdto> orderItems;
    private Integer itemCount;
}
