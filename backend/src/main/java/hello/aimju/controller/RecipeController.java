package hello.aimju.controller;

import hello.aimju.recipe.dto.GetAllRecipesResponseDto;
import hello.aimju.recipe.dto.GetRecipeResponseDto;
import hello.aimju.recipe.dto.SaveRecipeRequestDto;
import hello.aimju.recipe.service.RecipeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/save-recipe")
    public String saveRecipe(@RequestBody SaveRecipeRequestDto saveRecipeRequestDto, HttpSession session) {
        return recipeService.saveRecipe(saveRecipeRequestDto, session);
    }

    @GetMapping("/recipes")
    public List<GetAllRecipesResponseDto> getRecipes(HttpSession session) {
        return recipeService.getAllRecipes(session);
    }

    @GetMapping("/recipe/{recipeId}")
    public GetRecipeResponseDto getRecipeDetails(@PathVariable Long recipeId) {
        return recipeService.getRecipeDetails(recipeId);
    }

    @DeleteMapping("/recipe/{recipeId}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long recipeId, HttpSession session) {
        return recipeService.deleteRecipe(recipeId, session);
    }


}
