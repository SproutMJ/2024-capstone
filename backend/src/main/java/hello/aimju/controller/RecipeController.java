package hello.aimju.controller;

import hello.aimju.recipe.dto.RecipeRequestDto;
import hello.aimju.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping("/save-recipe")
    public String saveRecipe(@RequestBody RecipeRequestDto recipeRequestDto) {
        return recipeService.saveRecipe(recipeRequestDto);
    }
}
