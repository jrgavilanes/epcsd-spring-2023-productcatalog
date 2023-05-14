package edu.uoc.epcsd.productcatalog.repositories;

import edu.uoc.epcsd.productcatalog.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where name = :name")
    Collection<Category> findCategoriesByName(@Param("name") String name);

    @Query("select c from Category c where description = :description")
    Collection<Category> findCategoriesByDescription(@Param("description") String description);


//    Collection<Category> findCategoriesByParent(@Param("Parent") Optional<Category> parent);
}
