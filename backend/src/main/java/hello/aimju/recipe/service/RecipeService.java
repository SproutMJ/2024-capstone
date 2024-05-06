package hello.aimju.recipe.service;

import hello.aimju.recipe.domain.Recipe;
import hello.aimju.recipe.dto.RecipeRequestDto;
import hello.aimju.recipe.ingredients.domain.Ingredients;
import hello.aimju.recipe.ingredients.repository.IngredientsRepository;
import hello.aimju.recipe.recipe_info.domain.RecipeInfo;
import hello.aimju.recipe.recipe_info.repository.RecipeInfoRepository;
import hello.aimju.recipe.repository.RecipeRepository;
import hello.aimju.user.domain.User;
import hello.aimju.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientsRepository ingredientsRepository;
    private final RecipeInfoRepository recipeInfoRepository;
    private final UserRepository userRepository;

    public String saveRecipe(RecipeRequestDto recipeRequestDto) {
        // Recipe 엔티티 생성 및 저장
        Recipe recipe = new Recipe();
        recipe.setMenu(recipeRequestDto.getMenu());
        Optional<User> optionalUser = userRepository.findById(recipeRequestDto.getUserId());
        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found"));
        recipe.setUser(user);
        recipeRepository.save(recipe);

        // 재료(Ingredients) 저장
        List<String> ingredientsList = recipeRequestDto.getIngredients();
        for (String ingredient : ingredientsList) {
            Ingredients ingredients = new Ingredients();
            ingredients.setIngredient(ingredient);
            ingredients.setRecipe(recipe);
            ingredientsRepository.save(ingredients);
        }

        // 레시피 정보(RecipeInfo) 저장
        List<String> recipeInfoList = recipeRequestDto.getRecipeInfoList();
        for (String recipeInfo : recipeInfoList) {
            RecipeInfo info = new RecipeInfo();
            info.setInformation(recipeInfo);
            info.setRecipe(recipe);
            recipeInfoRepository.save(info);
        }

        return recipe.getMenu();
    }
}
