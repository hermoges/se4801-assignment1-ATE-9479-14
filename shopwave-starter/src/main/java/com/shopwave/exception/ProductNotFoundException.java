// Student Name: Hermela [Your Last Name]
// Student Number: ATE-9479-14
package com.shopwave.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Product not found with id: " + id);
    }
}