package com.example.ecom.Service;

import com.example.ecom.Repo.userRepo;
import com.example.ecom.model.users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretkey;
    private final userRepo repo;

    public JwtService(userRepo repo) {
        this.repo = repo;
    }

    public Key getkey(){
        byte[] keybytes= Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keybytes);
    }
    public String generateToken(String username) {
        users user = repo.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("user not found"));
        Map<String,Object> claims=new HashMap<>();
        claims.put("role",user.getRole());
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+60*60*1000))
                .and()
                .signWith(getkey())
                .compact();
    }
    public String extractusername(String token){
        return Jwts.parser().verifyWith((SecretKey) getkey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }
    public boolean istokenvalid(String token, UserDetails user){
        final String username=extractusername(token);
        return username.equals(user.getUsername()) && !istokenexpired(token);
    }

    private boolean istokenexpired(String token) {
        Date exp=Jwts.parser().verifyWith((SecretKey) getkey()).build()
                .parseSignedClaims(token).getPayload().getExpiration();
        return exp.before(new Date());
    }
}

