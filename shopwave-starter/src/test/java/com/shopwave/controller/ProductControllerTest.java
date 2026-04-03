// Student Name: Hermela [Your Last Name]
// Student Number: ATE-9479-14
package com.shopwave.controller;
import com.shopwave.shopwave_starter.ShopwaveStarterApplication;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import org.springframework.context.annotation.ComponentScan;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@ComponentScan(basePackages = "com.shopwave") // This tells Spring "Find the handler yourself!"
@ContextConfiguration(classes = ShopwaveStarterApplication.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testGetAllProducts_Returns200() throws Exception {
        Page<com.shopwave.dto.ProductDTO> emptyPage = new PageImpl<>(Collections.emptyList());
        when(productService.getAllProducts(any(PageRequest.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/products")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetProductById_NotFound_Returns404() throws Exception {
        when(productService.getProductById(999L)).thenThrow(new ProductNotFoundException(999L));

        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }
}