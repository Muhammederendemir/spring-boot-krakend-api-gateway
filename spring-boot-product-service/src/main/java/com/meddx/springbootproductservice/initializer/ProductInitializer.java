package com.meddx.springbootproductservice.initializer;

import com.meddx.springbootproductservice.entity.Product;
import com.meddx.springbootproductservice.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.stream.IntStream;

@Configuration
public class ProductInitializer {

    @Bean
    CommandLineRunner initializeProducts(ProductRepository productRepository) {
        return args -> {
            productRepository.deleteAll(); // Her başlangıçta veritabanını temizle

            // Veri tabanına 3 adet sahte ürün ekle
            IntStream.range(1, 4)
                    .mapToObj(i -> new Product(
                            "Product " + i,
                            "Description for Product " + i,
                            new BigDecimal("100.5" + i),
                            100 + i
                    ))
                    .forEach(productRepository::save);

            System.out.println("Products loaded: " + productRepository.findAll().size());
        };
    }
}