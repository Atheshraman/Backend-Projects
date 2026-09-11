package com.example.ecom.Service;

import com.example.ecom.DTO.Ecomdto;
import com.example.ecom.DTO.orderdto;
import com.example.ecom.DTO.orderitemdto;
import com.example.ecom.OrderStatus;
import com.example.ecom.Repo.OrderItemsRepo;
import com.example.ecom.Repo.Repository;
import com.example.ecom.Repo.orderRepo;
import com.example.ecom.Repo.userRepo;
import com.example.ecom.model.*;

import lombok.AllArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class orderService {
    private final orderRepo orderRepo;
    private final OrderItemsRepo orderItemsRepo;
    private final CartService cartService;
    private final Repository repo;
    private final userRepo userRepo;

    @Transactional
    public Orders placeorder(String email,String deliveryAddress) {
        try {
            users user = userRepo.findByEmail(email)
                    .orElseThrow(()->new UsernameNotFoundException("user not found"));
            Integer id = user.getId();
            List<CartItems> items = cartService.getCartitems(id);
            if (items.isEmpty()) {
                throw new RuntimeException("cart is empty");
            }
            Orders order = new Orders();
            order.setUser(user);
            order.setDeliveryAddress(deliveryAddress);
            order.setStatus(OrderStatus.PENDING);

            BigDecimal totalAmount = BigDecimal.ZERO;
            List<OrderItems> orderItem = new ArrayList<>();
            for (CartItems cartItem : items) {
                Long ecomId = cartItem.getEcom().getEcomId();
                Ecom product=repo.findIdForUpdate(ecomId)
                        .orElseThrow(()->new RuntimeException("product not found"));
                if (product.getQuantity() < cartItem.getQuantity()) {
                    throw new RuntimeException("product" + product.getName() + "is out of stock");
                }
                OrderItems orderitem = new OrderItems();
                orderitem.setEcom(product);
                orderitem.setQuantity(cartItem.getQuantity());
                orderitem.setPriceAtPurchase(product.getPrice());
                orderitem.setOrder(order);
                orderItem.add(orderitem);
                product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                repo.save(product);
                totalAmount = totalAmount.add(cartItem.getTotalPrice());
            }
            order.setTotalAmount(totalAmount);
            order.setOrderItems(orderItem);
            Orders savedorder = orderRepo.save(order);
            cartService.clearCart(id);
            return savedorder;
        }
        catch (ObjectOptimisticLockingFailureException e){
            throw new RuntimeException("one or more items sold out whileplacing your order");
        }

    }
    public List<orderdto> getAllOrdersDTO() {
        return orderRepo.findAllByOrderByOrderDateDesc().stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }
    private Orders getOrdersByIdInternal(Long orderId){
        return orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("order not found"));
    }
    public Orders getOrdersById(String email,Long orderId){
        Orders order=orderRepo.findById(orderId)
                .orElseThrow(()->new RuntimeException("order not found"));
        if(!order.getUser().getEmail().equals(email)){
            throw new AccessDeniedException("not your order");
        }
        return order;
    }
    public Orders updateOrderStatus(Long orderId,OrderStatus status){
        Orders order=getOrdersByIdInternal(orderId);
        order.setStatus(status);
        return orderRepo.save(order);
    }
    @Transactional
    public void cancelorder(String email,Long orderId){
        Orders order=getOrdersById(email,orderId);
        if(!order.getStatus().equals(OrderStatus.PENDING)){
            throw new RuntimeException("only pending orders can be cancelled");
        }
        Orders locked=orderRepo.findById(orderId)
                .orElseThrow(()->new RuntimeException("order not found"));
        for(OrderItems item:locked.getOrderItems()){
            Long ecomId=item.getEcom().getEcomId();
            Ecom product=repo.findIdForUpdate(ecomId)
                    .orElseThrow(()->new RuntimeException("product not found"));
            product.setQuantity(product.getQuantity()+item.getQuantity());
            repo.save(product);
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepo.save(order);
    }
    public orderdto getOrderDTOAdmin(Long orderId) {
        Orders order = getOrdersByIdInternal(orderId);
        return convertToOrderDTO(order);
    }
    public orderdto getOrderDTO(String email,Long orderId) {
        Orders order = getOrdersById(email,orderId);
        return convertToOrderDTO(order);
    }
    public List<orderdto> getUserOrdersDTO(String email) {
        users user=userRepo.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
        return orderRepo.findByUserIdWithItems(user.getId()).stream()
                .map(this::convertToOrderDTO)
                .collect(Collectors.toList());
    }

    private orderdto convertToOrderDTO(Orders order) {
        orderdto dto = new orderdto();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUser().getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setOrderDate(order.getOrderDate());
        dto.setDeliveryAddress(order.getDeliveryAddress());

        List<orderitemdto> orderItemDTOs = order.getOrderItems().stream()
                .map(this::convertToOrderItemDTO)
                .collect(Collectors.toList());
        dto.setOrderItems(orderItemDTOs);
        dto.setItemCount(orderItemDTOs.size());

        return dto;
    }

    private orderitemdto convertToOrderItemDTO(OrderItems orderItem) {
        orderitemdto dto = new orderitemdto();
        dto.setOrderItemId(orderItem.getOrderItemId());
        dto.setEcom(convertToEcomDTO(orderItem.getEcom()));
        dto.setQuantity(orderItem.getQuantity());
        dto.setPriceAtPurchase(orderItem.getPriceAtPurchase());
        dto.setSubtotal(orderItem.getPriceAtPurchase()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity())));
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
