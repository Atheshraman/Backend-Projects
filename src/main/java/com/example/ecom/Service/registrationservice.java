package com.example.ecom.Service;

import com.example.ecom.DTO.UserDTO;
import com.example.ecom.Repo.userRepo;
import com.example.ecom.model.users;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class registrationservice {

    private final userRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public registrationservice(userRepo repo, PasswordEncoder passwordEncoder) {
        this.userRepo = repo;
        this.passwordEncoder=passwordEncoder;
    }
    @Transactional
    public users Register(UserDTO userDTO,String password,String role){
        if(userRepo.existsByEmail(userDTO.getEmail())){
            throw new RuntimeException("email already exists");
        }

        users user =new users();

        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setUsername(userDTO.getUsername());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setState(userDTO.getState());
        user.setZipcode(userDTO.getZipcode());
        user.setCreatedAt(userDTO.getCreatedAt());
        return userRepo.save(user);
    }
    public users getUserById(Integer id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Transactional
    public users updateUserProfile(Integer id, UserDTO userDTO) {
        users user = getUserById(id);
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setState(userDTO.getState());
        user.setZipcode(userDTO.getZipcode());
        return userRepo.save(user);
    }

    public users getUserByEmail(String username) {
        users user=userRepo.findByEmail(username)
                .orElseThrow(()->new RuntimeException("user not found"));
        return user;
    }
}

