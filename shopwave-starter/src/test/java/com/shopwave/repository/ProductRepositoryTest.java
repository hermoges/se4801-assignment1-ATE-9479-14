// Student Name: Hermela [Your Last Name]
// Student Number: ATE-9479-14
package com.shopwave.repository;
import com.shopwave.shopwave_starter.ShopwaveStarterApplication;
import com.shopwave.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@DataJpaTest
@ContextConfiguration(classes = ShopwaveStarterApplication.class)
@EntityScan(basePackages = "com.shopwave.model") // Replace with your actual model package
@EnableJpaRepositories(basePackages = "com.shopwave.repository") // Replace with your actual repo package
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testFindByNameContainingIgnoreCase() {
        // Arrange
        Product p = Product.builder().name("iPhone 15").price(new BigDecimal("999")).stock(10).build();
        productRepository.save(p);

        // Act
        List<Product> results = productRepository.findByNameContainingIgnoreCase("iphone");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("iPhone 15");
    }
}