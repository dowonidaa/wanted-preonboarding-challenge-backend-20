package com.market.wanted.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

    private String status;
    private T data;
    private String message;
    private String errorCode;
}
