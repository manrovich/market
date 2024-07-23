package com.hn.market;

import com.hn.market.entity.Product;
import com.hn.market.entity.security.User;
import com.hn.market.repository.ProductRepository;
import com.hn.market.repository.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile({"dev", "dev-h2"})
public class DevDataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        createAdminUser();
        createProducts();
    }

    private void createAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            userRepository.save(admin);
        }
    }

    private void createProducts() {
        String longDescription = generateLongDescription(5);
        if (productRepository.count() == 0) {
            List<Product> products = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                Product product = new Product();
                product.setName(String.format("name %s", i));
                product.setShortDescription(String.format("description %s", i));
                product.setSpecification(longDescription);
                products.add(product);
            }
            productRepository.saveAll(products);
        }
    }

    private String generateLongDescription(int length) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int j = 0; j < length; j++) {
            sb.append("\"Характеристика ")
                    .append(j)
                    .append("\"")
                    .append(":")
                    .append("\"Значение ")
                    .append(j)
                    .append("\"");
            if (j != length - 1) {
                sb.append(",");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
