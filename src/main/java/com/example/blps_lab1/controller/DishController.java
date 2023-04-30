package com.example.blps_lab1.controller;

import com.example.blps_lab1.dto.request.AddDishRequest;
import com.example.blps_lab1.dto.request.UpdateDishRequest;
import com.example.blps_lab1.model.basic.Dish;
import com.example.blps_lab1.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/dish")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Dish addDish(@Valid @RequestBody AddDishRequest addDishRequest) {
        return dishService.saveDish(addDishRequest);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDish(@RequestParam("dishId") Long dishId) {
        dishService.deleteDish(dishId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping()
    public Dish updateDish(@RequestParam("dishId") Long dishId,
                           @Valid @RequestBody UpdateDishRequest updateDishRequest) {
        return dishService.updateDish(dishId, updateDishRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping()
    public List<Dish> getAllDishes(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int size) {
        return dishService.getAllDish(page, size).getContent();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("{dishId}")
    public Dish getDish(@PathVariable Long dishId) {
        return dishService.getDish(dishId);
    }
}
