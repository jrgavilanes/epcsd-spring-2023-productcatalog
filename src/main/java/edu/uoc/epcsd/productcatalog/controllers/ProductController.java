package edu.uoc.epcsd.productcatalog.controllers;

import edu.uoc.epcsd.productcatalog.entities.ItemStatus;
import edu.uoc.epcsd.productcatalog.entities.Product;
import edu.uoc.epcsd.productcatalog.entities.ProductItem;
import edu.uoc.epcsd.productcatalog.kafka.ProductMessage;
import edu.uoc.epcsd.productcatalog.repositories.CategoryRepository;
import edu.uoc.epcsd.productcatalog.repositories.ProductItemRepository;
import edu.uoc.epcsd.productcatalog.repositories.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private KafkaTemplate<String, ProductMessage> productKafkaTemplate;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        log.trace("getAllProducts");
        return productRepository.findAll();
    }

    // add the code for the missing operations here
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Map<String, Object>> createProduct(@RequestBody @Valid Product product,
                                                             @RequestParam(name = "category_id", required = false) Long categoryId) {
        log.trace("createProduct");
        if (categoryId != null) {
            var category = categoryRepository.findById(categoryId);
            if (category.isPresent()) {
                product.setCategory(category.get());
            } else {
                Map<String, Object> body = new LinkedHashMap<>();
                body.put("timestamp", LocalDateTime.now());
                body.put("message", "ID Category not found: " + categoryId);

                return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
            }
        }
        productRepository.save(product);
        return null;
    }

    @PostMapping("{id_product}/createItem")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductItem createItem(@PathVariable("id_product") Long id_product,
                                  @RequestParam(name = "serial_number") String serialNumber) {
        log.trace("createItem");

        var product = productRepository.findById(id_product).orElseThrow(
                () -> new RuntimeException("Product not found")
        );

        var productItem = new ProductItem(-1L, serialNumber, ItemStatus.OPERATIONAL, product);
        return productItemRepository.save(productItem);


    }
}

