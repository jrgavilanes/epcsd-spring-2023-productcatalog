package edu.uoc.epcsd.productcatalog;

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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
@Log4j2
public class ProductCatalogApplication implements CommandLineRunner {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductItemRepository productItemRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        insertDemoData();

    }

    private void insertDemoData() {
        var categories = List.of(
                new Category(1L, "video", "description video", null, null),
                new Category(2L, "audio", "description audio", null, null)
        );

        for (var category : categories) {
            categoryRepository.save(category);
        }

        var category1 = categoryRepository.findById(1L).get();

        categoryRepository.save(new Category(3L, "name2", "description2", category1, null));

        var product1 = new Product(-1L, "sony-psx", "descripcion1", 0.0, "sony", "psx", category1);
        var product2 = new Product(-1L, "hitachi-ajax", "descripcion2", 0.0, "hitachi", "ajax", category1);

        productRepository.save(product1);
        productRepository.save(product2);

        var p1 = productRepository.findById(1L).get();
        var productItem = new ProductItem(-1L, "sn:0000001", ItemStatus.OPERATIONAL, p1);

        try {
            productItemRepository.save(productItem);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
