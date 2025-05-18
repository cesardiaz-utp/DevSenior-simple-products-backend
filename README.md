# Simple Products Backend

Este proyecto es una API REST para la gestión de productos, desarrollada con Spring Boot, Spring Data JPA y una base de datos H2 embebida.

---

## 1. Requisitos previos

Antes de comenzar, asegúrate de tener instalado:

- **Java 21** o superior
- **Maven 3.9.x** o superior
- **Visual Studio Code**

### Extensiones recomendadas para VSCode

Instala las siguientes extensiones desde el Marketplace de VSCode para trabajar cómodamente con Java y Spring Boot:

- [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)
- [Spring Boot Extension Pack](https://marketplace.visualstudio.com/items?itemName=Pivotal.vscode-boot-dev-pack)

Puedes buscar e instalar extensiones presionando `Ctrl+Shift+X` y escribiendo el nombre de la extensión.

---

## 2. Crear el proyecto con Spring Initializr desde VSCode

1. Abre **Visual Studio Code**.
2. Presiona `Ctrl+Shift+P` y escribe `Spring Initializr: Create a Maven Project`.
3. Completa los siguientes campos:
   - **Group Id:** `com.devsenior`
   - **Artifact Id:** `simple-products-backend`
   - **Name:** `simple-products-backend`
   - **Package Name:** `com.devsenior.simple_products_backend`
   - **Packaging:** `Jar`
   - **Java:** `21`
4. En **Dependencies** agrega:
   - `Spring Web`
   - `Spring Data JPA`
   - `H2 Database`
5. Elige una carpeta para guardar el proyecto y espera a que se genere.

---

## 3. Abrir el proyecto en VSCode

- Selecciona **Archivo > Abrir carpeta...** y elige la carpeta `simple-products-backend`.
- Espera a que VSCode cargue las dependencias Maven.

---

## 4. Estructura recomendada

```text
simple-products-backend/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/devsenior/simple_products_backend/
│   │   │        ├── SimpleProductsBackendApplication.java
│   │   │        ├── model/
│   │   │        │    └── Product.java
│   │   │        ├── repository/
│   │   │        │    └── ProductRepository.java
│   │   │        └── controller/
│   │   │             └── ProductController.java
│   │   └── resources/
│   │        └── application.properties
│   └── test/
│        └── java/
│             └── com/devsenior/simple_products_backend/
│                  └── ProductControllerTest.java
├── pom.xml
└── README.md
```

---

## 5. Agrega el código fuente necesario

### 5.1 Clase Principal: `com.devsenior.simple_products_backend.SimpleProductsBackendApplication`

Es la clase principal que arranca la aplicación Spring Boot. Contiene el método `main` y la anotación `@SpringBootApplication`.

No hay que modificar nada de ella.

```java
package com.devsenior.simple_products_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleProductsBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(SimpleProductsBackendApplication.class, args);
    }
}
```

### 5.2. Entidad `com.devsenior.simple_products_backend.model.Product`

Es la entidad JPA que representa la tabla de `productos` en la base de datos. Define los atributos del producto, constructores y sus getters/setters.

````java
package com.devsenior.simple_products_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    public Product() {
    }

    public Product(Long id, String name, String description, Double price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
````

### 5.3. Repositorio `com.devsenior.simple_products_backend.repository.ProductRepository`

Es el repositorio que extiende `JpaRepository` y permite realizar operaciones CRUD sobre la entidad `Product` sin necesidad de implementar métodos manualmente.

```Java
package com.devsenior.simple_products_backend.repository;

import com.devsenior.simple_products_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
```

### 5.4. Controlador `com.devsenior.simple_products_backend.controller.ProductController`

Es el controlador REST que expone los endpoints para gestionar productos (listar, crear, actualizar, eliminar) y recibe las peticiones HTTP.

```Java
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

```

### 5.5. Archivo de Configuración `src/main/resources/application.properties`

Archivo de configuración donde se define la conexión a la base de datos H2, parámetros de JPA/Hibernate y la habilitación de la consola H2.

```Properties
spring.application.name=simple-products-backend

# Configuración de la base de datos H2 en memoria
spring.datasource.url=jdbc:h2:file:./db/products_db
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Configuración de Spring Data JPA y Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Habilitar la consola web de H2 (útil para ver los datos)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true
```

## 6. Ejecutar la aplicación

En la terminal de VSCode, ejecuta:

```bash
./mvnw spring-boot:run
```

O en Windows:

```terminal
mvnw.cmd spring-boot:run
```

La API estará disponible en:
[http://localhost:8080/api/products](http://localhost:8080/api/products)

## 7. Probar la API

Puedes usar Thunder Client o Postman para probar los endpoints:

- `GET    /api/products` - Listar todos los productos
- `GET    /api/products/{id}` - Obtener un producto por ID
- `POST   /api/products` - Crear un nuevo producto
    Ejemplo del body:

    ```json
    {
      "name": "Nuevo producto",
      "description": "Descripción",
      "price": 100,
      "quantity": 10
    }
    ```

- `PUT    /api/products/{id}` - Actualizar un producto existente
- `DELETE /api/products/{id}` - Eliminar un producto

## 8. Recursos útiles

- [Documentación Spring Boot](https://spring.io/projects/spring-boot)
- [Documentación Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Documentación H2 Database](https://www.h2database.com/html/main.html)

---

¡Listo! Ahora puedes desarrollar, ejecutar y probar tu backend de productos en VSCode desde cero usando Spring Initializr.
