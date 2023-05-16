package edu.uoc.epcsd.productcatalog.controllers;

import edu.uoc.epcsd.productcatalog.entities.Category;
import edu.uoc.epcsd.productcatalog.entities.ItemStatus;
import edu.uoc.epcsd.productcatalog.entities.Product;
import edu.uoc.epcsd.productcatalog.entities.ProductItem;
import edu.uoc.epcsd.productcatalog.kafka.KafkaConstants;
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
import java.util.*;

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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Product> getProductById(@PathVariable("id") Long id) {
        log.trace("getProductById");
        return productRepository.findById(id);
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

        try {
            var item = productItemRepository.save(productItem);
            productKafkaTemplate.send(
                    KafkaConstants.PRODUCT_TOPIC + KafkaConstants.SEPARATOR + KafkaConstants.UNIT_AVAILABLE,
                    new ProductMessage(product.getBrand(), product.getModel())
            );
            return item;
        } catch (Exception e) {
            log.error("createItem error: " + e.getMessage());
        }

        return null;

    }

    @GetMapping("{id_product}/{id_item}")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<ProductItem> getItemById(@PathVariable("id_product") Long id_product,
                                             @PathVariable("id_item") Long id_item) {
        log.trace("getItemById");

        return productItemRepository.findById(id_item);

    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable("id") Long id
    ) {
        log.trace("deleteProduct");
        var product = productRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product not found")
        );
        productRepository.delete(product);
    }

    @PatchMapping("/item/{item_id}/setNotOperational")
    @ResponseStatus(HttpStatus.OK)
    public void setNotOperactionalItem(@PathVariable("item_id") Long id
    ) {
        log.trace("setNotOperactionalItem");
        var productItem = productItemRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product item not found")
        );
        productItem.setStatus(ItemStatus.NON_OPERATIONAL);
        productItemRepository.save(productItem);
    }

    @PatchMapping("/item/{item_id}/setOperational")
    @ResponseStatus(HttpStatus.OK)
    public void setOperactionalItem(@PathVariable("item_id") Long id
    ) {
        log.trace("setNotOperactionalItem");
        var productItem = productItemRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Product item not found")
        );
        productItem.setStatus(ItemStatus.OPERATIONAL);
        productItemRepository.save(productItem);
    }

    @GetMapping("/findCategoriesByCriteria")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Category> findCategoriesByCriteria(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "parent_id", required = false) Long parentId
    ) {
        log.trace("findCategoriesByCriteria");

        if (name != null) {
            return categoryRepository.findCategoriesByName(name);
        }

        if (description != null) {
            return categoryRepository.findCategoriesByDescription(description);
        }

        if (parentId != null) {
            return categoryRepository.findCategoriesByParent(parentId);
        }

        throw new RuntimeException("Not valid criteria given");
    }

    @GetMapping("/findProductByCriteria")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Product> findProductByCriteria(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category_id", required = false) Long categoryId

    ) {
        log.trace("findProductByCriteria");

        if (name != null) {
            return productRepository.findByName(name);
        }

        if (categoryId != null) {
            var category = categoryRepository.findById(categoryId);
            return productRepository.findByCategory(categoryId);
        }

        throw new RuntimeException("Not valid criteria given");
    }


}

