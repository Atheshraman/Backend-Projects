package com.example.ecom.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(name="password",nullable = false)
    @JsonIgnore
    private String password;
    @Column(nullable=false)
    private String username;
    @Column(nullable = false)
    private String role;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable=false)
    private String lastname;
    @Column(nullable = false)
    private String phone;
    @Column(nullable=false)
    private String address;
    private String city;
    private String state;
    private String zipcode;

    @Column(name="CreatedAt")
    private LocalDateTime createdAt=LocalDateTime.now();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Orders> orders=new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<CartItems> cartitems=new ArrayList<>();
}
