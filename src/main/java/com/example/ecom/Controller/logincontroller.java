package com.example.ecom.Controller;

import com.example.ecom.DTO.LoginRequest;
import com.example.ecom.Service.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ecom")
@Validated
public class logincontroller {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtservice;

    public logincontroller(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtservice = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            String token = jwtservice.generateToken(request.getEmail());
            return ResponseEntity.ok(
                    Map.of(
                            "token", token,
                            "username", request.getEmail()
                    )
            );
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("LOGIN FAILED");
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(){
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        String username=auth.getName();
        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("USER");
        return ResponseEntity.ok(Map.of("username", username, "role", role));
    }
}