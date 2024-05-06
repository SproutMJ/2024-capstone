package hello.aimju.controller;

import hello.aimju.recipe.dto.GetRecipeResponseDto;
import hello.aimju.recipe.dto.SaveRecipeRequestDto;
import hello.aimju.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/save-recipe")
    public String saveRecipe(@RequestBody SaveRecipeRequestDto saveRecipeRequestDto) {
        return recipeService.saveRecipe(saveRecipeRequestDto);
    }

    @GetMapping("/recipes")
    public List<GetRecipeResponseDto> getRecipes() {
        return recipeService.getAllRecipes();
    }
}
