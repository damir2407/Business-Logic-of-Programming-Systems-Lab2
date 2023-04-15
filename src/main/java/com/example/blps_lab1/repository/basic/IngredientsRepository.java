package com.example.blps_lab1.repository.basic;

import com.example.blps_lab1.model.basic.Ingredients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {

    Optional<Ingredients> findIngredientsByName(String name);

    Optional<Ingredients> findIngredientsById(Long id);

    Page<Ingredients> findAll(Pageable pageable);

    boolean existsIngredientsByName(String name);

}
