package com.example.ecom.Controller;

import com.example.ecom.DTO.orderdto;
import com.example.ecom.OrderStatus;
import com.example.ecom.Service.orderService;
import com.example.ecom.model.Orders;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ecom")
@AllArgsConstructor
@Validated
public class ordercontroller {
    private final orderService orderService;

    @PostMapping("/order/placeorder")
    public ResponseEntity<orderdto> placeorder( @RequestParam String deliveryAddress){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        Orders order=orderService.placeorder(username,deliveryAddress);
        orderdto dto=orderService.getOrderDTO(username,order.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Orders> getorders(@PathVariable Long orderId){
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        Orders order=orderService.getOrdersById(email,orderId);
        return ResponseEntity.ok(order);
    }
    @GetMapping("/order/admin/all")
    public ResponseEntity<List<orderdto>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrdersDTO());
    }

    @GetMapping("/order/myorders")
    public ResponseEntity<List<orderdto>> getuserOrders(){
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        List<orderdto> order=orderService.getUserOrdersDTO(email);
        return ResponseEntity.ok(order);
    }
    @PutMapping("/order/{orderId}/status")
    public ResponseEntity<orderdto> updateOrderStatus(@PathVariable Long orderId,
                                                      @RequestParam OrderStatus status){
        orderService.updateOrderStatus(orderId, status);
        orderdto dto = orderService.getOrderDTOAdmin(orderId);
        return ResponseEntity.ok(dto);
    }
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Void> cancelorder(@PathVariable Long orderId){
        String email=SecurityContextHolder.getContext().getAuthentication().getName();
        orderService.cancelorder(email,orderId);
        return ResponseEntity.noContent().build();
    }
}
