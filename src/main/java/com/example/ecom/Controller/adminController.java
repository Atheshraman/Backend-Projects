package com.example.ecom.Controller;

import com.example.ecom.DTO.UserDTO;
import com.example.ecom.Service.registrationservice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Validated
public class adminController {
    private final registrationservice reg;

    public adminController(registrationservice reg) {
        this.reg = reg;

    }

    @PostMapping("/create")
    public ResponseEntity<?> Adminregister(@RequestBody UserDTO userDTO){
        try{
            reg.Register(userDTO,userDTO.getPassword(),"ADMIN");
            return  ResponseEntity.status(HttpStatus.CREATED).body("ADMIN Created");
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("registration failed");
        }
    }


}
