package com.market.wanted.product.controller;

import com.market.wanted.common.dto.ApiResponse;
import com.market.wanted.product.dto.AddProduct;
import com.market.wanted.product.dto.ResponseProduct;
import com.market.wanted.product.dto.ResponseProductDetail;
import com.market.wanted.product.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@Validated @RequestBody AddProduct addProduct, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        productService.addProduct(addProduct, memberId);
        return ResponseEntity.ok("제품이 등록 되었습니다.");
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<ResponseProduct>>> productList() {
        ApiResponse<List<ResponseProduct>> response = productService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable("productId") Long productId, @AuthenticationPrincipal UserDetails user) {
        if (user==null){
            ResponseProduct productDto = productService.findById(productId);
            ApiResponse<ResponseProduct> response = ApiResponse.<ResponseProduct>builder().status("success").data(productDto).build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        ApiResponse<ResponseProductDetail> response = productService.findDtoById(productId, user.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
