package com.market.wanted.product.dto;

import com.market.wanted.product.entity.ProductStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseProduct {

    private Long productId;
    private String productName;
    private long price;
    private ProductStatus status;
    private String sellerName;

}
