package com.example.blps_lab1.controller;

import com.example.blps_lab1.dto.request.AddIngredientRequest;
import com.example.blps_lab1.dto.request.UpdateIngredientRequest;
import com.example.blps_lab1.model.basic.Ingredients;
import com.example.blps_lab1.service.IngredientsService;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredient")
@CrossOrigin(origins = "*", maxAge = 3600)
public class IngredientController {
    private final IngredientsService ingredientsService;

    public IngredientController(IngredientsService ingredientsService) {
        this.ingredientsService = ingredientsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Ingredients addIngredient(@Valid @RequestBody AddIngredientRequest addIngredientRequest) {
        return ingredientsService.saveIngredient(addIngredientRequest);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIngredient(@RequestParam("ingredientId") Long ingredientId) {
        ingredientsService.deleteIngredient(ingredientId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public Ingredients updateIngredient(@RequestParam("ingredientId") Long ingredientId,
                                        @Valid @RequestBody UpdateIngredientRequest updateIngredientRequest) {
        return ingredientsService.updateIngredient(ingredientId, updateIngredientRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping()
    public List<Ingredients> getAllIngredients(@RequestParam(value = "page", defaultValue = "1") int page,
                                               @RequestParam(value = "size", defaultValue = "10") int size) {
        return ingredientsService.getIngredientsPage(page, size).getContent();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("{ingredientId}")
    public Ingredients getIngredient(@PathVariable Long ingredientId) {
        return ingredientsService.getIngredient(ingredientId);
    }

}

