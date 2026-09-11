package com.example.ecom.Controller;

import com.example.ecom.DTO.UserDTO;
import com.example.ecom.Service.registrationservice;
import com.example.ecom.model.users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/ecom")
public class registrationcontroller {
    private final registrationservice reg;

    public registrationcontroller(registrationservice reg) {
        this.reg = reg;
    }

    @GetMapping("/register")
    public String registerpage(){
        return "register";
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO, Model model){

        try{
            users dto=reg.Register(userDTO, userDTO.getPassword(),"USER");
            return ResponseEntity.status(HttpStatus.CREATED).body(toUserDTO(dto));
        }
        catch(RuntimeException e){
            model.addAttribute("error",e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }

    }
    @PutMapping("/profile/{id}")
    public ResponseEntity<UserDTO> Updateuserprofile(@PathVariable Integer id,@RequestBody UserDTO userDTO){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        users caller = reg.getUserByEmail(username);
        if (!caller.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        users user=reg.updateUserProfile(id,userDTO);
        return ResponseEntity.ok(toUserDTO(user));
    }
    private UserDTO toUserDTO(users user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}
