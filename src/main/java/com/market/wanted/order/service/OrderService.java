package com.market.wanted.order.service;

import com.market.wanted.member.entity.Member;
import com.market.wanted.member.service.MemberService;
import com.market.wanted.common.dto.ApiResponse;
import com.market.wanted.order.dto.ResponseOrder;
import com.market.wanted.order.dto.ResponseOrderDetail;
import com.market.wanted.order.entity.Order;
import com.market.wanted.order.entity.OrderItem;
import com.market.wanted.order.entity.OrderStatus;
import com.market.wanted.order.repository.OrderFindRepository;
import com.market.wanted.order.repository.OrderRepository;
import com.market.wanted.product.entity.Product;
import com.market.wanted.product.entity.ProductStatus;
import com.market.wanted.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final ProductRepository productRepository;
    private final OrderFindRepository orderFindRepository;

    @Transactional
    public ApiResponse createOrder(String username, Long productId) {
        Member buyer = memberService.findByUsername(username);
        Product findProduct = productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException("상품을 못찾음"));

        if(findProduct.getStatus() == ProductStatus.SALE) {
            Order order = new Order(OrderStatus.RESERVATION, buyer, findProduct.getSeller());
            findProduct.changeStatus(ProductStatus.RESERVATION);
            orderRepository.save(order);
            buyer.addOrder(order);
            OrderItem orderItem = OrderItem.builder().price(findProduct.getPrice())
                    .product(findProduct)
                    .build();
           order.addOrderItem(orderItem);
            return ApiResponse.builder()
                    .data(
                            ResponseOrderDetail.builder()
                                    .orderStatus(order.getOrderStatus())
                                    .orderId(order.getId())
                                    .orderDateTime(LocalDateTime.now())
                                    .sellerId(findProduct.getSeller().getId())
                                    .sellerName(findProduct.getSeller().getUsername())
                                    .buyerId(buyer.getId())
                                    .buyerName(buyer.getUsername())
                                    .price(orderItem.getPrice())
                                    .productName(findProduct.getProductName())
                                    .productId(findProduct.getId())
                                    .isSeller(isSeller(order.getSeller().getUsername(), username))
                                    .build()
                    )
                    .status("success").build();
        }
        return null;

    }

    @Transactional
    public void orderConfirm(String username, Long oderId) {
        Order findOrder = orderRepository.findById(oderId).orElse(null);
        if (findOrder.getSeller().getUsername().equals(username)) {
            findOrder.confirm();
        }

    }

    public ResponseOrder findDtoById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        return new ResponseOrder(order.getId(),
                order.getSeller().getId(),
                order.getBuyer().getId(),
                order.getOrderItem().getProduct().getId(),
                order.getOrderItem().getPrice(),
                order.getOrderItem().getProduct().getProductName(),
                order.getOrderStatus());
    }


    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public List<ResponseOrder> findAllBySellerEmail(String email) {
        return orderFindRepository.findAllBySellerName(email);
    }
    public List<ResponseOrder> findAllByBuyerEmail(String email) {
        return orderFindRepository.findAllByBuyerName(email);
    }

    public ResponseOrderDetail findResponseById(Long orderId, String username) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        return ResponseOrderDetail.builder()
                .orderId(order.getId())
                .sellerId(order.getSeller().getId())
                .buyerId(order.getBuyer().getId())
                .orderStatus(order.getOrderStatus())
                .productName(order.getOrderItem().getProduct().getProductName())
                .productId(order.getOrderItem().getProduct().getId())
                .orderDateTime(order.getCreateDate())
                .buyerName(order.getBuyer().getUsername())
                .sellerName(order.getSeller().getUsername())
                .price(order.getOrderItem().getPrice())
                .isSeller(isSeller(order.getSeller().getUsername(), username))
                .build();
    }

    private boolean isSeller(String seller, String username) {
        return seller.equals(username);
    }

}
