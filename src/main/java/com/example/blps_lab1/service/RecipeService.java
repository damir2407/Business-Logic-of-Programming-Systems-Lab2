package com.example.blps_lab1.service;

import com.example.blps_lab1.dto.request.AddRecipeRequest;
import com.example.blps_lab1.dto.request.UpdateRecipeRequest;
import com.example.blps_lab1.exception.*;
import com.example.blps_lab1.model.basic.*;
import com.example.blps_lab1.repository.basic.RecipeOnReviewRepository;
import com.example.blps_lab1.repository.basic.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeOnReviewRepository recipeOnReviewRepository;

    private final UserService userService;

    private final DishService dishService;

    private final IngredientsService ingredientsService;

    private final TastesService tastesService;

    private final NationalCuisineService nationalCuisineService;


    public RecipeService(RecipeRepository recipeRepository, RecipeOnReviewRepository recipeOnReviewRepository, UserService userService, DishService dishService,
                         IngredientsService ingredientsService, TastesService tastesService,
                         NationalCuisineService nationalCuisineService) {
        this.recipeRepository = recipeRepository;
        this.recipeOnReviewRepository = recipeOnReviewRepository;
        this.userService = userService;
        this.dishService = dishService;
        this.ingredientsService = ingredientsService;
        this.tastesService = tastesService;
        this.nationalCuisineService = nationalCuisineService;
    }

    public RecipeOnReview saveRecipe(String login, AddRecipeRequest addRecipeRequest) {
        Dish dish = dishService.findDishByName(addRecipeRequest.getDishName());
        User user = userService.findUserByLogin(login);
        NationalCuisine nationalCuisine = nationalCuisineService.findNationalCuisineByName(addRecipeRequest.getNationalCuisineName());
        List<Tastes> tastesList = tastesService.findAllTastesByTasteNames(addRecipeRequest.getTastesNames());
        List<Ingredients> ingredientsList = ingredientsService.findAllIngredientsByNames(addRecipeRequest.getIngredientsNames());
        RecipeOnReview recipe = new RecipeOnReview(addRecipeRequest.getDescription(),
                addRecipeRequest.getCountPortion(), user, nationalCuisine, dish, tastesList, ingredientsList);
        recipeOnReviewRepository.save(recipe);
        return recipe;
    }
    public Recipe findRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Рецепт с номером " + id + " не найден в базе!"));
    }

    public void checkUserOnRecipeOwner(User user, Recipe recipe) {
        if (!user.getLogin().equals(recipe.getUser().getLogin()))
            throw new PermissionDeniedException("Пользователь " + user.getLogin() +
                    " не является владельцем рецепта по номеру " + recipe.getId());
    }

    public void deleteRecipe(String login, Long id) {
        Recipe recipe = findRecipeById(id);
        User user = userService.findUserByLogin(login);
        checkUserOnRecipeOwner(user, recipe);

        recipeRepository.delete(recipe);
    }

    public void updateRecipe(String login, Long id, UpdateRecipeRequest updateRecipeRequest) {
        Recipe recipe = findRecipeById(id);
        User user = userService.findUserByLogin(login);
        checkUserOnRecipeOwner(user, recipe);
        RecipeOnReview recipeOnReview = new RecipeOnReview();
        recipeOnReview.setId(id);
        Dish dish = dishService.findDishByName(updateRecipeRequest.getDishName());
        NationalCuisine nationalCuisine = nationalCuisineService.
                findNationalCuisineByName(updateRecipeRequest.getNationalCuisineName());
        List<Tastes> tastesList = tastesService.
                findAllTastesByTasteNames(updateRecipeRequest.getTastesNames());
        List<Ingredients> ingredientsList = ingredientsService.
                findAllIngredientsByNames(updateRecipeRequest.getIngredientsNames());
        recipeOnReview.setUser(user);
        recipeOnReview.setDish(dish);
        recipeOnReview.setNationalCuisine(nationalCuisine);
        recipeOnReview.setIngredients(ingredientsList);
        recipeOnReview.setTastes(tastesList);
        recipeOnReview.setDescription(updateRecipeRequest.getDescription());
        recipeOnReview.setCountPortion(updateRecipeRequest.getCountPortion());
        recipeOnReview.setUpdateRecipe(true);
        recipeOnReviewRepository.save(recipeOnReview);
    }

    public Page<Recipe> getAllRecipes(int page, int size, String sortOrder) {
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "id"));
        return recipeRepository.findAll(pageable);
    }

}
