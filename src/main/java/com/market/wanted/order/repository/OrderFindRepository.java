package com.market.wanted.order.repository;

import com.market.wanted.order.dto.OrderDto;
import com.market.wanted.order.dto.TransactionDetail;

import java.util.List;

public interface OrderFindRepository {
    List<OrderDto> findAllBySellerName(String username);
    List<OrderDto> findAllByBuyerName(String username);

    List<TransactionDetail> findOrdersBySellerName(Long productId, String sellerName);
    List<TransactionDetail> findOrdersByBuyerName(Long productId, String sellerName);
}
