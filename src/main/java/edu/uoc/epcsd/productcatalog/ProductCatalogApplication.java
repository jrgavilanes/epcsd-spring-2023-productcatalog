package edu.uoc.epcsd.productcatalog;

import edu.uoc.epcsd.productcatalog.entities.Category;
import edu.uoc.epcsd.productcatalog.entities.Product;
import edu.uoc.epcsd.productcatalog.repositories.CategoryRepository;
import edu.uoc.epcsd.productcatalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@SpringBootApplication
@EnableJpaRepositories
public class ProductCatalogApplication implements CommandLineRunner {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProductCatalogApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Demo data

        var categories = List.of(
                new Category(1l, "name1", "description1", null, null),
                new Category(2l, "name2", "description2", null, null)
        );

        for (var category : categories) {
            categoryRepository.save(category);
        }

        var category1 = categoryRepository.findById(1l).get();
        var category2 = categoryRepository.findById(2l).get();

        categoryRepository.save(new Category(3l, "name2", "description2", category1, null));

        var product1 = new Product(-1l, "producto1", "descripcion1", 0.0, "marca", "modelo", category1);
        var product2 = new Product(-1l, "producto2", "descripcion2", 0.0, "marca", "modelo", category2);

        productRepository.save(product1);
        productRepository.save(product2);
    }
}
