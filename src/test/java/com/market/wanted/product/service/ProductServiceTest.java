package com.market.wanted.product.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;


    @Test
    void productFindAll() {

//        List<ProductDto> productDtos = productService.findAll();
//        assertThat(productDtos.size()).isEqualTo(3);
    }

    @Test
    void findDto() {
//        ProductDto productDto = productService.findDtoById(1L);

//        assertThat(productDto).isNotNull();
//        assertThat(productDto.getProductId()).isEqualTo(1L);

    }

}