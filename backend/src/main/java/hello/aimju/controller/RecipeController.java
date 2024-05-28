package hello.aimju.controller;

import hello.aimju.recipe.dto.ChatRecipeRequestDto;
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

    @PostMapping("/recipe/save-chat")
    public ResponseEntity<?> saveRecipe(@RequestBody ChatRecipeRequestDto requestDto, HttpSession session) {
        return recipeService.saveChatAsRecipe(requestDto, session);
    }

    @GetMapping("/recipes")
    public List<GetAllRecipesResponseDto> getRecipes(HttpSession session) {
        return recipeService.getAllRecipes(session);
    }

    @RequestMapping(value = "/recipe/{recipeId}", method = RequestMethod.GET)
    public GetRecipeResponseDto getRecipeDetails(@PathVariable("recipeId") Long recipeId ,HttpSession session) {
        GetRecipeResponseDto recipeDetails = recipeService.getRecipeDetails(recipeId, session);
        return recipeDetails;
    }

    @RequestMapping(value = "/recipe/{recipeId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRecipe(@PathVariable("recipeId") Long recipeId, HttpSession session) {
        return recipeService.deleteRecipe(recipeId, session);
    }

    @RequestMapping(value = "/recipe/{recipeId}", method = RequestMethod.PUT)
    public String updateRecipe(@PathVariable("recipeId") Long recipeId,
                               @RequestBody SaveRecipeRequestDto saveRecipeRequestDto, HttpSession session) {
        return  recipeService.updateRecipe(recipeId, saveRecipeRequestDto, session);
    }


}
