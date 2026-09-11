package com.example.ecom.Service;

import com.example.ecom.DTO.Ecomdto;
import com.example.ecom.DTO.cartdto;
import com.example.ecom.DTO.cartitemDTO;
import com.example.ecom.Repo.Repository;
import com.example.ecom.Repo.cartitemRepo;
import com.example.ecom.Repo.userRepo;
import com.example.ecom.model.CartItems;
import com.example.ecom.model.Ecom;
import com.example.ecom.model.users;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CartService {
    private final cartitemRepo cartRepo;
    private final Repository repo;
    private final userRepo userRepo;

    @Transactional
    public CartItems Addtocart(
            String email,Long ecomId,Integer quantity){
        users user=userRepo.findByEmail(email)
                .orElseThrow(()->new RuntimeException("user not found"));
        Ecom product=repo.findById(ecomId)
                .orElseThrow(()->new RuntimeException("product not found"));
        if(quantity==null || quantity<=0){
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
        if(product.getQuantity()<quantity){
            throw new RuntimeException("insufficient stock");
        }
        Optional<CartItems> existingitem=cartRepo.findByUser_IdAndEcom_EcomId(user.getId(),ecomId);
        if(existingitem.isPresent()){
            CartItems item=existingitem.get();
            item.setQuantity(item.getQuantity()+quantity);
            return cartRepo.save(item);
        }
        CartItems item=new CartItems();
        item.setUser(user);
        item.setEcom(product);
        item.setQuantity(quantity);
        return cartRepo.save(item);
    }
    public List<CartItems> getCartitems(Integer id){

        return cartRepo.findByUserId(id);
    }
    public CartItems updatequantity(String email, Long CartItemId, Integer quantity){
        CartItems item=cartRepo.findById(CartItemId)
                .orElseThrow(()->new RuntimeException("item not found in cart"));
        if (!item.getUser().getEmail().equals(email)) {
            throw new AccessDeniedException("not your cart item");
        }
        if(quantity <=0){
            cartRepo.delete(item);
            return null;
        }
        else{
            if (item.getEcom().getQuantity() < quantity) {
                throw new RuntimeException("insufficient stock");
            }
            item.setQuantity(quantity);
             return cartRepo.save(item);
        }
    }
    public BigDecimal getCartTotal(Integer id){
        List<CartItems> item=getCartitems(id);
        return item.stream()
                .map(CartItems::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }
    public void removefromcart(String email, Long cartItemId){
        CartItems item = cartRepo.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("item not found"));
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        if (!item.getUser().getEmail().equals(username)) {
            throw new AccessDeniedException("not your cart item");
        }
        cartRepo.deleteById(cartItemId);
    }
    @Transactional
    public void clearCart(Integer id){
        cartRepo.deleteByUserId(id);
    }
    public Integer resolveUserId(String email) {
        users user = userRepo.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("email not exists"));
        return user.getId();
    }
    public cartdto getCartDTO(Integer userId) {
        List<CartItems> items = getCartitems(userId);
        List<cartitemDTO> cartItemDTOs = items.stream()
                .map(this::convertToCartItemDTO)
                .collect(Collectors.toList());

        BigDecimal total = getCartTotal(userId);

        cartdto cartDTO = new cartdto();
        cartDTO.setUserId(userId);
        cartDTO.setCartItems(cartItemDTOs);
        cartDTO.setCartTotal(total);
        cartDTO.setItemCount(items.size());

        return cartDTO;
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

