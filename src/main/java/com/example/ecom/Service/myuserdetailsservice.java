package com.example.ecom.Service;


import com.example.ecom.Repo.userRepo;
import com.example.ecom.model.userprincipal;
import com.example.ecom.model.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class myuserdetailsservice implements UserDetailsService {

    @Autowired
    private userRepo repo;
    @Override
    public UserDetails loadUserByUsername( String email) throws UsernameNotFoundException {
        users user=repo.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
        return new userprincipal(user);
    }

}
