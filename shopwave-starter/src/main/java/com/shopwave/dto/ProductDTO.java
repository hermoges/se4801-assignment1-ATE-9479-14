// Student Name: Hermela [Your Last Name]
// Student Number: ATE-9479-14
package com.shopwave.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder // <--- THIS IS THE MOST IMPORTANT LINE
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Long categoryId;
}