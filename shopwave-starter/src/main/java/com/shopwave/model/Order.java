// Student Name: Hermela [Your Last Name]
// Student Number: ATE-9479-14
package com.shopwave.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private BigDecimal totalAmount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    // Convenience Method
    public void addItem(Product product, int quantity) {
        OrderItem item = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getPrice())
                .order(this)
                .build();
        this.items.add(item);
        
        // Update total amount logic (optional but good practice)
        if (this.totalAmount == null) this.totalAmount = BigDecimal.ZERO;
        this.totalAmount = this.totalAmount.add(item.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
    }

    public enum OrderStatus {
        PENDING, SHIPPED, DELIVERED, CANCELLED
    }
}