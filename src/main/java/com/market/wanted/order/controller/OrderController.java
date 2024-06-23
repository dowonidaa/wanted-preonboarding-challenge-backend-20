package com.market.wanted.order.controller;

import com.market.wanted.auth.dto.CustomUserDetails;
import com.market.wanted.common.dto.ApiResponse;
import com.market.wanted.order.dto.OrderRequest;
import com.market.wanted.order.dto.ResponseOrder;
import com.market.wanted.order.dto.ResponseOrderDetail;
import com.market.wanted.order.entity.Order;
import com.market.wanted.order.entity.OrderStatus;
import com.market.wanted.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    //주문생성
    @PostMapping("/add")
    public ResponseEntity<?> createOrder(@Validated @RequestBody OrderRequest order, @AuthenticationPrincipal CustomUserDetails user) {
        try {
            ApiResponse<ResponseOrderDetail> response = orderService.createOrder(user.getUsername(), order.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.builder().status("error").message(e.getMessage()));
        }
    }

    //구매자 주문조회
    @GetMapping("/my")
    public ResponseEntity<List<ResponseOrder>> myOrders(@AuthenticationPrincipal CustomUserDetails user) {
        List<ResponseOrder> orderDtos = orderService.findAllByBuyerEmail(user.getUsername());
        return ResponseEntity.ok(orderDtos);
    }

    // 판매자 주문조회
    @GetMapping("/seller")
    public ResponseEntity<List<ResponseOrder>> orders(@AuthenticationPrincipal CustomUserDetails user) {
        List<ResponseOrder> orderDtos = orderService.findAllBySellerEmail(user.getUsername());
        return ResponseEntity.ok(orderDtos);
    }


    // 주문 상세내역 - 구매자
    @GetMapping("/my/{orderId}")
    public ResponseEntity<ResponseOrderDetail> getMyOrder(@PathVariable("orderId") Long orderId, @AuthenticationPrincipal CustomUserDetails user) {
        ResponseOrderDetail responseOrder = orderService.findResponseById(orderId, user.getUsername());
        return ResponseEntity.ok(responseOrder);
    }

    // 주문 상세내역 - 판매자
    @GetMapping("/seller/{orderId}")
    public ResponseEntity<ResponseOrderDetail> getSalesOrder(@PathVariable("orderId") Long orderId, @AuthenticationPrincipal CustomUserDetails user) {
        ResponseOrderDetail responseOrder = orderService.findResponseById(orderId, user.getUsername());
        return ResponseEntity.ok(responseOrder);
    }


    //판매자 주문 승인
    @PatchMapping("/seller/{orderId}")
    public ResponseEntity<String> orderConfirm(@PathVariable("orderId") Long orderId, @AuthenticationPrincipal CustomUserDetails user) {
        Order order = orderService.findById(orderId);
        OrderStatus orderStatus = order.getOrderStatus();
        String seller = order.getSeller().getUsername();
        if (orderStatus == OrderStatus.RESERVATION && user.getUsername().equals(seller)) {
            orderService.orderConfirm(user.getUsername(), orderId);
            return ResponseEntity.ok("주문이 승인 되었습니다.");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 승인 완료된 주문입니다.");
        }
    }



}
