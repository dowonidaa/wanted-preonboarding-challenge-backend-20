package com.market.wanted.product.service;

import com.market.wanted.common.dto.ApiResponse;
import com.market.wanted.member.entity.Member;
import com.market.wanted.member.repository.MemberRepository;
import com.market.wanted.order.dto.TransactionDetail;
import com.market.wanted.order.repository.OrderFindRepository;
import com.market.wanted.product.dto.AddProduct;
import com.market.wanted.product.dto.ProductDto;
import com.market.wanted.product.dto.ResponseProduct;
import com.market.wanted.product.entity.Product;
import com.market.wanted.product.entity.ProductStatus;
import com.market.wanted.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderFindRepository orderFindRepository;

    public ApiResponse findDtoById(Long productId, String username) throws Exception {
        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new Exception("Product not found"));
        boolean isSeller = findProduct.getSeller().getUsername().equals(username);
        if (findProduct.getStatus().equals(ProductStatus.SALE)) {

            ResponseProduct response = ResponseProduct.builder()
                    .productName(findProduct.getProductName())
                    .price(findProduct.getPrice())
                    .isSeller(isSeller)
                    .sellerName(findProduct.getSeller().getUsername())
                    .status(findProduct.getStatus())
                    .productId(findProduct.getId())
                    .build();

            return ApiResponse.builder().status("success").data(response).build();
        }

        if (isSeller) {
            List<TransactionDetail> transactionList = orderFindRepository.findOrdersBySellerName(productId, username);
            ResponseProduct response = ResponseProduct.builder()
                    .productName(findProduct.getProductName())
                    .price(findProduct.getPrice())
                    .isSeller(isSeller)
                    .sellerName(findProduct.getSeller().getUsername())
                    .status(findProduct.getStatus())
                    .productId(findProduct.getId())
                    .transactionDetails(transactionList)
                    .build();
            return ApiResponse.builder().status("success").data(response).build();
        } else {
            List<TransactionDetail> transactionDetailList = orderFindRepository.findOrdersByBuyerName(productId, username);
            ResponseProduct response = ResponseProduct.builder()
                    .productName(findProduct.getProductName())
                    .price(findProduct.getPrice())
                    .isSeller(isSeller)
                    .sellerName(findProduct.getSeller().getUsername())
                    .status(findProduct.getStatus())
                    .productId(findProduct.getId())
                    .transactionDetails(transactionDetailList)
                    .build();
            return ApiResponse.builder().status("success").data(response).build();
        }
    }


    public ApiResponse findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = ProductDto.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .status(product.getStatus())
                    .price(product.getPrice())
                    .sellerName(product.getSeller().getUsername()).build();
            productDtos.add(productDto);
        }
        return ApiResponse.builder().status("success").data(productDtos).build();

    }

    public ProductDto findById(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        return ProductDto.builder()
                .productId(product.getId())
                .sellerName(product.getSeller().getUsername())
                .price(product.getPrice())
                .status(product.getStatus())
                .productName(product.getProductName())
                .build();
    }

    public void addProduct(AddProduct addProduct, Long memberId) {
        Member seller = memberRepository.findById(memberId).orElse(null);
        Product product = new Product(addProduct.getProductName(), addProduct.getPrice(), seller);
        productRepository.save(product);
    }
}
