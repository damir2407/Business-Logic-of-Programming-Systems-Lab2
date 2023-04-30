package com.example.blps_lab1.controller;

import com.example.blps_lab1.config.jwt.AuthTokenFilter;
import com.example.blps_lab1.config.jwt.JwtUtils;
import com.example.blps_lab1.dto.RecipeDTOMapper;
import com.example.blps_lab1.dto.RecipeOnReviewDTOMapper;
import com.example.blps_lab1.dto.request.AddRecipeRequest;
import com.example.blps_lab1.dto.request.UpdateRecipeRequest;
import com.example.blps_lab1.dto.response.RecipeResponse;
import com.example.blps_lab1.model.basic.Recipe;
import com.example.blps_lab1.model.basic.RecipeOnReview;
import com.example.blps_lab1.service.RecipeOnReviewService;
import com.example.blps_lab1.service.RecipeService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recipe")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeOnReviewService recipeOnReviewService;
    private final AuthTokenFilter authTokenFilter;
    private final JwtUtils jwtUtils;

    private final RecipeOnReviewDTOMapper recipeOnReviewDTOMapper;

    private final RecipeDTOMapper recipeDTOMapper;

    public RecipeController(RecipeService recipeService,
                            RecipeOnReviewService recipeOnReviewService,
                            JwtUtils jwtUtils,
                            AuthTokenFilter authTokenFilter,
                            RecipeOnReviewDTOMapper recipeOnReviewDTOMapper,
                            RecipeDTOMapper recipeDTOMapper) {
        this.recipeService = recipeService;
        this.recipeOnReviewService = recipeOnReviewService;
        this.jwtUtils = jwtUtils;
        this.authTokenFilter = authTokenFilter;
        this.recipeOnReviewDTOMapper = recipeOnReviewDTOMapper;
        this.recipeDTOMapper = recipeDTOMapper;
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeResponse newRecipe(@Valid @RequestBody AddRecipeRequest addRecipeRequest,
                                    HttpServletRequest httpServletRequest) {
        String login = jwtUtils.getLoginFromJwtToken(authTokenFilter.parseJwt(httpServletRequest));
        RecipeOnReview recipeOnReview = recipeService.saveRecipe(login, addRecipeRequest);
        return recipeOnReviewDTOMapper.apply(recipeOnReview);

    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@RequestParam Long id, HttpServletRequest httpServletRequest) {
        String login = jwtUtils.getLoginFromJwtToken(authTokenFilter.parseJwt(httpServletRequest));
        recipeService.deleteRecipe(login, id);
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping()
    public RecipeResponse updateRecipe(@RequestParam Long id,
                                       @Valid @RequestBody UpdateRecipeRequest updateRecipeRequest,
                                       HttpServletRequest httpServletRequest) {
        String login = jwtUtils.getLoginFromJwtToken(authTokenFilter.parseJwt(httpServletRequest));
        RecipeOnReview recipeOnReview = recipeService.updateRecipe(login, id, updateRecipeRequest);
        return recipeOnReviewDTOMapper.apply(recipeOnReview);
    }

    @GetMapping()
    public List<RecipeResponse> getAllRecipes(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {
        return recipeService.getAllRecipes(page, size, sortOrder.toString()).getContent()
                .stream()
                .map(recipeDTOMapper)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public RecipeResponse getRecipe(@PathVariable Long id) {
        Recipe recipe = recipeService.findRecipeById(id);
        return recipeDTOMapper.apply(recipe);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("accept/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeResponse acceptRecipe(@PathVariable Long id) {
        return recipeDTOMapper.apply(recipeOnReviewService.saveRecipe(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("decline/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void declineRecipe(@PathVariable Long id) {
        recipeOnReviewService.deleteRecipe(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/review")
    public List<RecipeResponse> getAllRecipesOnReview(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {

        return recipeOnReviewService.
                getAllRecipesOnReview(page, size, sortOrder.toString()).
                getContent()
                .stream()
                .map(recipeOnReviewDTOMapper)
                .collect(Collectors.toList());
    }
}
