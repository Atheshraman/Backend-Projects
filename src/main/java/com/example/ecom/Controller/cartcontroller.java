package com.example.ecom.Controller;

import com.example.ecom.DTO.Ecomdto;
import com.example.ecom.DTO.cartdto;
import com.example.ecom.DTO.cartitemDTO;
import com.example.ecom.Service.CartService;
import com.example.ecom.model.CartItems;
import com.example.ecom.model.Ecom;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/ecom")
@AllArgsConstructor
@Validated
public class cartcontroller {
    private final CartService cartService;

    @PostMapping("/cart/add")
    public ResponseEntity<cartitemDTO> addtocart(
                                               @RequestParam Long ecomId ,@RequestParam Integer quantity ){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String email=authentication.getName();
        CartItems item=cartService.Addtocart(email,ecomId,quantity);
        cartitemDTO dto=convertToCartItemDTO(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @GetMapping("/cart")
    public ResponseEntity<cartdto> getcart(){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        Integer userid=cartService.resolveUserId(username);
        return  ResponseEntity.ok(cartService.getCartDTO(userid));
    }
    @GetMapping("/cart/{id}/carttotal")
    public ResponseEntity<BigDecimal> getcarttotal(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Integer userId = cartService.resolveUserId(email);
        return ResponseEntity.ok(cartService.getCartTotal(userId));
    }
    @DeleteMapping("/cart/{cartItemId}")
    public ResponseEntity<Void> removefromcart(@PathVariable Long cartItemId){
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        cartService.removefromcart(email,cartItemId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/cart/{cartItemId}")
    public ResponseEntity<cartitemDTO> Updatequantity(@PathVariable Long cartItemId,
                                                    @RequestParam Integer quantity){
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        CartItems item=cartService.updatequantity(email,cartItemId,quantity);
        if (item == null) {
            return ResponseEntity.noContent().build();
        }
        cartitemDTO dto=convertToCartItemDTO(item);
        return  ResponseEntity.ok(dto);
    }
    private cartitemDTO convertToCartItemDTO(CartItems cartItem) {
        cartitemDTO dto = new cartitemDTO();
        dto.setCartItemId(cartItem.getCartItemId());
        dto.setUserId(cartItem.getUser().getId());
        dto.setEcom(convertToEcomDTO(cartItem.getEcom()));
        dto.setQuantity(cartItem.getQuantity());
        dto.setAddedAt(cartItem.getAddedAt());
        dto.setTotalPrice(cartItem.getTotalPrice());
        return dto;
    }

    private Ecomdto convertToEcomDTO(Ecom ecom) {
        Ecomdto dto = new Ecomdto();
        dto.setEcomId(ecom.getEcomId());
        dto.setName(ecom.getName());
        dto.setDescript(ecom.getDescript());
        dto.setPrice(ecom.getPrice());
        dto.setQuantity(ecom.getQuantity());
        dto.setAvailability(ecom.isAvailabity());
        dto.setReleaseDate(ecom.getReleasDate());
        dto.setImageName(ecom.getImagename());
        dto.setImageType(ecom.getImagetype());
        return dto;
    }
}
