package com.shopwave.service;

import com.shopwave.dto.ProductDTO;
import com.shopwave.dto.CreateProductRequest;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.model.Product;
import com.shopwave.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return mapToDTO(product);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> searchProducts(String keyword, BigDecimal maxPrice) {
        List<Product> products = productRepository.findByNameContainingIgnoreCase(keyword);
        List<ProductDTO> dtos = new ArrayList<>();
        
        for (Product p : products) {
            if (p.getPrice().compareTo(maxPrice) <= 0) {
                dtos.add(mapToDTO(p));
            }
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        // This is the simplest way to map a Page without the :: shortcut
        return productRepository.findAll(pageable).map(p -> mapToDTO(p));
    }

    public ProductDTO updateStock(Long id, int delta) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        
        int newStock = product.getStock() + delta;
        if (newStock < 0) {
            throw new IllegalArgumentException("Final stock cannot be negative!");
        }
        
        product.setStock(newStock);
        return mapToDTO(productRepository.save(product));
    }

    // MANUAL MAPPER (No Builder needed)
    private ProductDTO mapToDTO(Product p) {
        Long catId = (p.getCategory() != null) ? p.getCategory().getId() : null;
        
        return new ProductDTO(
            p.getId(), 
            p.getName(), 
            p.getDescription(), 
            p.getPrice(), 
            p.getStock(), 
            catId
        );
    }
    // Inside ProductService class
public ProductDTO createProduct(CreateProductRequest request) {
    // This part creates the actual database entity from the request
    Product product = Product.builder()
            .name(request.getName())
            .description(request.getDescription())
            .price(request.getPrice())
            .stock(request.getStock())
            .build();
    
    // Save it to the database
    Product savedProduct = productRepository.save(product);
    
    // Convert it back to a DTO to send to the user
    return mapToDTO(savedProduct);
}
}