package com.market.wanted.order.repository;

import com.market.wanted.order.dto.ResponseOrder;
import com.market.wanted.order.dto.TransactionDetail;

import java.util.List;

public interface OrderFindRepository {
    List<ResponseOrder> findAllBySellerName(String username);
    List<ResponseOrder> findAllByBuyerName(String username);

    List<TransactionDetail> findOrdersBySellerName(Long productId, String sellerName);
    List<TransactionDetail> findOrdersByBuyerName(Long productId, String sellerName);
}
