package com.example.ecom.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class cartdto {
    private Integer userId;
    private List<cartitemDTO> cartItems;
    private BigDecimal cartTotal;
    private Integer itemCount;
}