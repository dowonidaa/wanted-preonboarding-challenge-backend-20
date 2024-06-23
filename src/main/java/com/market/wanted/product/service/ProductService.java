package com.market.wanted.product.service;

import com.market.wanted.common.dto.ApiResponse;
import com.market.wanted.member.entity.Member;
import com.market.wanted.member.repository.MemberRepository;
import com.market.wanted.order.dto.TransactionDetail;
import com.market.wanted.order.repository.OrderFindRepository;
import com.market.wanted.product.dto.AddProduct;
import com.market.wanted.product.dto.ResponseProduct;
import com.market.wanted.product.dto.ResponseProductDetail;
import com.market.wanted.product.entity.Product;
import com.market.wanted.product.entity.ProductStatus;
import com.market.wanted.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final OrderFindRepository orderFindRepository;

    public ApiResponse<ResponseProductDetail> findDtoById(Long productId, String username) {
        Product findProduct = productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("제품을 찾을수 없습니다."));
        boolean isSeller = findProduct.getSeller().getUsername().equals(username);
        if (findProduct.getStatus().equals(ProductStatus.SALE)) {

            ResponseProductDetail response = ResponseProductDetail.builder()
                    .productName(findProduct.getProductName())
                    .price(findProduct.getPrice())
                    .isSeller(isSeller)
                    .sellerName(findProduct.getSeller().getUsername())
                    .status(findProduct.getStatus())
                    .productId(findProduct.getId())
                    .build();

            return ApiResponse.<ResponseProductDetail>builder().status("success").data(response).build();
        }

        if (isSeller) {
            List<TransactionDetail> transactionDetails = orderFindRepository.findOrdersBySellerName(productId, username);
            ResponseProductDetail response = buildProductDetail(findProduct, isSeller, transactionDetails);
            return ApiResponse.<ResponseProductDetail>builder().status("success").data(response).build();
        } else {
            List<TransactionDetail> transactionDetails = orderFindRepository.findOrdersByBuyerName(productId, username);
            ResponseProductDetail response = buildProductDetail(findProduct, isSeller, transactionDetails);
            return ApiResponse.<ResponseProductDetail>builder().status("success").data(response).build();
        }
    }


    public ApiResponse<List<ResponseProduct>> findAll() {
        List<Product> products = productRepository.findAll();
        List<ResponseProduct> responseList = new ArrayList<>();
        for (Product product : products) {
            ResponseProduct productDto = ResponseProduct.builder()
                    .productId(product.getId())
                    .productName(product.getProductName())
                    .status(product.getStatus())
                    .price(product.getPrice())
                    .sellerName(product.getSeller().getUsername()).build();
            responseList.add(productDto);
        }
        return ApiResponse.<List<ResponseProduct>>builder().status("success").data(responseList).build();

    }

    public ResponseProduct findById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->new EntityNotFoundException("제품 정보를 찾을수 없습니다."));
        return ResponseProduct.builder()
                .productId(product.getId())
                .sellerName(product.getSeller().getUsername())
                .price(product.getPrice())
                .status(product.getStatus())
                .productName(product.getProductName())
                .build();
    }

    public void addProduct(AddProduct addProduct, Long memberId) {
        Member seller = memberRepository.findById(memberId).orElseThrow(() -> new EntityNotFoundException("회원정보를 찾을수 없습니다."));
        Product product = new Product(addProduct.getProductName(), addProduct.getPrice(), seller);
        productRepository.save(product);
    }
    private ResponseProductDetail buildProductDetail(Product findProduct, boolean isSeller, List<TransactionDetail> transactionDetails) {
        return ResponseProductDetail.builder()
                .productName(findProduct.getProductName())
                .price(findProduct.getPrice())
                .isSeller(isSeller)
                .sellerName(findProduct.getSeller().getUsername())
                .status(findProduct.getStatus())
                .productId(findProduct.getId())
                .transactionDetails(transactionDetails)
                .build();
    }

}
