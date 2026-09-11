package com.example.ecom.model;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ecom {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ecom_id")
    private Long ecomId;
    private String name;
    private String descript;
    private BigDecimal price;
    @Version
    private Long version;
    private int quantity;
    private boolean availabity;
    private LocalDate releasDate;
    private String imagename;
    private String imagetype;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageData;
}
