package com.hn.market.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hn.market.entity.Product;
import com.hn.market.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/products")
    public String listProducts(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10;
        Page<Product> productPage = productService.getProducts(PageRequest.of(page - 1, pageSize)); // Сдвигаем номер страницы на 1
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "index";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, @RequestParam(defaultValue = "1") int page) {
        productService.deleteProduct(id);
        if (page > 1 && isEmptyPage(page - 1)) {
            return "redirect:/products?page=" + (page - 1);
        }
        return "redirect:/products?page=" + page;
    }

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable Long id, Model model) throws JsonProcessingException {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        // TODO Вынести из контроллера
        Map<String, String> specification = objectMapper.readValue(product.getSpecification(), Map.class);
        model.addAttribute("specification", specification);
        return "card";
    }

    private boolean isEmptyPage(int page) {
        if (page < 0) {
            return true;
        }
        return productService.getProducts(PageRequest.of(page, 10)).isEmpty();
    }
}
