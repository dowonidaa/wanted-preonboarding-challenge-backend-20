package com.market.wanted.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse {

    private String status;
    private Object data;
    private String message;
    private String errorCode;
}
