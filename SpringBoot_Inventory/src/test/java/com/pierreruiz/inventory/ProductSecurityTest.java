package com.pierreruiz.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.ulr=jdbc:h2:mem:security-tests",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "server.ssl.enabled=false",
        "INVENTORY_READER_PASSWORD=test-reader-password",
        "INVENTORY_ADMIN_PASSWORD=test-admin-password"
})
public class ProductSecurityTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private JpaProductRepository repository;

    private MockMvc mvc;

    private static final String PRODUCT_JSON = """
            {
                "id": 99,
                "name": "Sword",
                "stock": 10
            }
            """;

    @BeforeEach
    public void setup() {
        repository.deleteAll();

        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity()).build();
    }

    void unauthenticatedCannotReadProducts() throws Exception {
        mvc.perform(get("/products/available")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "reader", roles = "USER")
    void readerCanReadProducts() throws Exception{
        mvc.perform(get("/products/available"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "reader", roles = "USER")
    void readerCannotCreateProduct() throws Exception{
        mvc.perform(post("/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(PRODUCT_JSON))
            .andExpect(status().isForbidden());

        org.junit.jupiter.api.Assertions.assertFalse(
                repository.existsById(99L)
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminCanCreateProduct() throws Exception{
        mvc.perform(post("/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(PRODUCT_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/products/99"));

        org.junit.jupiter.api.Assertions.assertTrue(
                repository.existsById(99L)
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminCannotCreateProductWithoutCsrf() throws Exception{
        mvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUCT_JSON))
                .andExpect(status().isForbidden());

        org.junit.jupiter.api.Assertions.assertFalse(
                repository.existsById(99L)
        );
    }
}
