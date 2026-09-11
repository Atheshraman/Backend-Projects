package com.example.ecom.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ecomdto{
    private Long ecomId;
    private String name;
    private String descript;
    private BigDecimal price;
    private Integer quantity;
    private Boolean availability;
    private LocalDate releaseDate;
    private String imageName;
    private String imageType;
}