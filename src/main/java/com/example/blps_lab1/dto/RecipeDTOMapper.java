package com.example.blps_lab1.dto;

import com.example.blps_lab1.dto.response.RecipeResponse;
import com.example.blps_lab1.model.basic.Recipe;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RecipeDTOMapper implements Function<Recipe, RecipeResponse>{
    @Override
    public RecipeResponse apply(Recipe recipe) {
        return new RecipeResponse(
                recipe.getId(),
                recipe.getDescription(),
                recipe.getCountPortion(),
                recipe.getUser().getLogin(),
                recipe.getNationalCuisine(),
                recipe.getDish(),
                recipe.getTastes(),
                recipe.getIngredients()
        );
    }
}
