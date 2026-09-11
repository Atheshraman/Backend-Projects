package com.example.ecom.config;

import com.example.ecom.Service.JwtAuthfilter;
import com.example.ecom.Service.myuserdetailsservice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class config {


    private final myuserdetailsservice userdetailsservice;
    private final JwtAuthfilter jwtAuthfilter;

    public config(JwtAuthfilter jwtAuthfilter, myuserdetailsservice userdetailsservice) {
        this.userdetailsservice=userdetailsservice;
        this.jwtAuthfilter = jwtAuthfilter;
    }

    @Bean
    public SecurityFilterChain Securityfilterchain(HttpSecurity http) throws Exception{
        http.cors(cors->{});
        http.csrf(Customizer-> Customizer.disable());
        http.addFilterBefore(jwtAuthfilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(request->
                request.requestMatchers("/ecom/login").permitAll()
                        .requestMatchers("/ecom/register").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/ecom/cart/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/ecom/order/*/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/ecom/order/admin/all").hasRole("ADMIN")
                        .requestMatchers("/ecom/order/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST,"/ecom/products").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/ecom/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/ecom/products/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/ecom/products","/ecom/products/**").permitAll()
                        .anyRequest().authenticated()

        );
        http.formLogin(form->form.disable());
        http.httpBasic(basic->basic.disable());
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider=new DaoAuthenticationProvider(userdetailsservice);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration conf=new CorsConfiguration();

        conf.setAllowedOrigins(
                List.of("http://localhost:5173"));
        conf.setAllowedMethods(
                List.of("GET","PUT","POST","DELETE","OPTIONS")
        );
        conf.setAllowedHeaders(
                List.of("*")
        );
        conf.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",conf);
        return source;
    }

}
