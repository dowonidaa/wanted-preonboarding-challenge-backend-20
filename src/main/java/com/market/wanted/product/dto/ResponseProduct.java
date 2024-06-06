package com.market.wanted.product.dto;

import com.market.wanted.order.dto.TransactionDetail;
import com.market.wanted.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ResponseProduct {

    private Long productId;
    private String productName;
    private long price;
    private List<TransactionDetail> transactionDetails;
    private boolean isSeller;
    private ProductStatus status;
    private String sellerName;
}
