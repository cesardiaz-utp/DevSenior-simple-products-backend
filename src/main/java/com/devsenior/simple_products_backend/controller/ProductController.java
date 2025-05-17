package com.devsenior.simple_products_backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.devsenior.simple_products_backend.model.Product;
import com.devsenior.simple_products_backend.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // GET /api/products/{id} -> Obtener por ID
    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable("id") Long id) {
        return productRepository.findById(id);
    }

    // POST /api/products -> Crear nuevo
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    // PUT /api/products/{id} -> Actualizar
    @PutMapping("/{id}")
    public Optional<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product productDetails) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isPresent()) {
            // Si existe, actualiza sus campos con los detalles recibidos
            Product existingProduct = product.get();
            existingProduct.setName(productDetails.getName());
            existingProduct.setDescription(productDetails.getDescription());
            existingProduct.setPrice(productDetails.getPrice());
            existingProduct.setQuantity(productDetails.getQuantity());
            // Nota: No cambiamos existingProduct.setId(id);

            Product updatedProduct = productRepository.save(existingProduct);
            return Optional.of(updatedProduct);
        } else {
            return Optional.empty();
        }
    }

    // DELETE /api/products/{id} -> Eliminar
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

}
