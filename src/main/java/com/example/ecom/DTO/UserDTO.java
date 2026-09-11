package com.example.ecom.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer user_id;
    private String email;
    private String password;
    private String username;
    private String role;
    private String firstname;
    private String lastname;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private LocalDateTime createdAt;

}
