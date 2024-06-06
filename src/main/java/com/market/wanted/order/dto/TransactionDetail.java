package com.market.wanted.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionDetail {
    private Long orderId;
    private String username; //거래 상대방
    private LocalDateTime transactionDate;
    private String productName;


    @QueryProjection
    public TransactionDetail(Long orderId, String username, LocalDateTime transactionDate, String productName) {
        this.orderId = orderId;
        this.username = username;
        this.productName = productName;
        this.transactionDate = transactionDate;
    }
}
