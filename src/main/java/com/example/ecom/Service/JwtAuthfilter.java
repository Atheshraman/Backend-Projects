package com.example.ecom.Service;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthfilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final myuserdetailsservice myuserdetailsservice;

    public JwtAuthfilter(JwtService jwtService, myuserdetailsservice myuserdetailsservice) {
        this.jwtService = jwtService;
        this.myuserdetailsservice = myuserdetailsservice;
    }
    private static final Logger log = LoggerFactory.getLogger(JwtAuthfilter.class);
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String header=request.getHeader("Authorization");
            if(header==null || !header.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
            String token=header.substring(7);
            String username=jwtService.extractusername(token);
            try {

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = myuserdetailsservice.loadUserByUsername(username);
                    if (jwtService.istokenvalid(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authtoken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities()
                                );
                        SecurityContextHolder.getContext().setAuthentication(authtoken);
                    }
                }
            }
            catch(JwtException | IllegalArgumentException e){
                log.debug("JWT validation failed for request {}: {}", request.getRequestURI(), e.getClass().getSimpleName());
            }
            filterChain.doFilter(request,response);
    }
}
