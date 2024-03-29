package edu.uoc.epcsd.productcatalog.controllers;

import edu.uoc.epcsd.productcatalog.entities.Category;
import edu.uoc.epcsd.productcatalog.repositories.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        log.trace("getAllCategories");
        return categoryRepository.findAll();
    }

    // add the code for the missing operations here
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCategory(@RequestBody @Valid Category category,
                               @RequestParam(name = "parent", required = false) Long parentId) {
        log.trace("createCategory");
        if (parentId != null) {
            var parentCategory = categoryRepository.findById(parentId).orElseThrow(
                    () -> new RuntimeException("Parent category not found")
            );
            category.setParent(parentCategory);
            categoryRepository.save(category);
            return;
        }

        categoryRepository.save(category);
    }
}
