//package com.example.blps_lab1.model.basic;
//
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.List;
//
//@Entity
//@Table(name = "recipe_on_reviews")
//@Getter
//@Setter
//@NoArgsConstructor
//public class RecipeOnReview {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column(length = 4096, nullable = false)
//    private String description;
//    private Integer countPortion;
//
//    @JoinColumn(nullable = false, name = "user_login")
//    @ManyToOne(fetch = FetchType.EAGER)
//    private User user;
//
//    @JoinColumn(nullable = false, name = "cuisine_id")
//    @ManyToOne(fetch = FetchType.EAGER)
//    private NationalCuisine nationalCuisine;
//
//    @JoinColumn(nullable = false, name = "dish_id")
//    @ManyToOne(fetch = FetchType.EAGER)
//    private Dish dish;
//
//    @ManyToMany(cascade = {CascadeType.REFRESH})
//    @JoinTable(
//            name = "recipe_tastes",
//            joinColumns = {@JoinColumn(name = "recipe_id")},
//            inverseJoinColumns = {@JoinColumn(name = "taste_id")}
//    )
//    private List<Tastes> tastes;
//
//    @ManyToMany(cascade = {CascadeType.REFRESH})
//    @JoinTable(
//            name = "recipe_ingredients",
//            joinColumns = {@JoinColumn(name = "recipe_id")},
//            inverseJoinColumns = {@JoinColumn(name = "ingredient_id")}
//    )
//    private List<Ingredients> ingredients;
//
//    private boolean updateRecipe;
//
//
//    public RecipeOnReview(Long id, String description, Integer countPortion,
//                          User user, NationalCuisine nationalCuisine, Dish dish, List<Tastes> tastes,
//                          List<Ingredients> ingredients) {
//        this.id = id;
//        this.description = description;
//        this.countPortion = countPortion;
//        this.user = user;
//        this.nationalCuisine = nationalCuisine;
//        this.dish = dish;
//        this.tastes = tastes;
//        this.ingredients = ingredients;
//    }
//
//    public RecipeOnReview(String description, Integer countPortion, User user,
//                          NationalCuisine nationalCuisine, Dish dish, List<Tastes> tastes,
//                          List<Ingredients> ingredients) {
//        this.description = description;
//        this.countPortion = countPortion;
//        this.user = user;
//        this.nationalCuisine = nationalCuisine;
//        this.dish = dish;
//        this.tastes = tastes;
//        this.ingredients = ingredients;
//    }
//
//}
