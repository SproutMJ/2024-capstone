package hello.aimju.recipe.service;

import hello.aimju.gpt.dto.GptRecipeResponseDto;
import hello.aimju.login.session.SessionConst;
import hello.aimju.recipe.domain.Recipe;
import hello.aimju.recipe.dto.ChatRecipeRequestDto;
import hello.aimju.recipe.dto.GetAllRecipesResponseDto;
import hello.aimju.recipe.dto.GetRecipeResponseDto;
import hello.aimju.recipe.dto.SaveRecipeRequestDto;
import hello.aimju.recipe.ingredients.domain.Ingredients;
import hello.aimju.recipe.ingredients.repository.IngredientsRepository;
import hello.aimju.recipe.recipe_info.domain.RecipeInfo;
import hello.aimju.recipe.recipe_info.repository.RecipeInfoRepository;
import hello.aimju.recipe.repository.RecipeRepository;
import hello.aimju.user.domain.User;
import hello.aimju.user.dto.StatusResponseDto;
import hello.aimju.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientsRepository ingredientsRepository;
    private final RecipeInfoRepository recipeInfoRepository;
    private final UserRepository userRepository;

    @Transactional
    public String saveRecipe(SaveRecipeRequestDto saveRecipeRequestDto, HttpSession session) {
        // Recipe 엔티티 생성 및 저장
        Recipe recipe = new Recipe();
        recipe.setMenu(saveRecipeRequestDto.getMenu());
        recipe.setUser(getUserFromSession(session));
        recipe = recipeRepository.save(recipe);

        // 재료(Ingredients) 저장
        List<Ingredients> ingredientsList = new ArrayList<>();
        for (String ingredient : saveRecipeRequestDto.getIngredients()) {
            Ingredients ingredients = new Ingredients();
            ingredients.setIngredient(ingredient);
            ingredients.setRecipe(recipe);
            ingredientsList.add(ingredients);
        }
        ingredientsRepository.saveAll(ingredientsList);

        // 레시피 정보(RecipeInfo) 저장
        List<RecipeInfo> recipeInfoList = new ArrayList<>();
        for (String recipeInfo : saveRecipeRequestDto.getInstructions()) {
            RecipeInfo info = new RecipeInfo();
            info.setInformation(recipeInfo);
            info.setRecipe(recipe);
            recipeInfoList.add(info);
        }
        recipeInfoRepository.saveAll(recipeInfoList);

        return recipe.getMenu();
    }

    @Transactional
    public ResponseEntity<?> saveChatAsRecipe(ChatRecipeRequestDto requestDto, HttpSession session) {
        String recipe_info = requestDto.getRecipe();

        Recipe recipe = new Recipe();
        recipe.setMenu(requestDto.getMenu());
        recipe.setUser(getUserFromSession(session));
        recipe = recipeRepository.save(recipe);

        List<String> ingredients = new ArrayList<>();
        List<String> instructions = new ArrayList<>();

        parseRecipe(ingredients, instructions, recipe_info); //전달받은 레시피를 원하는 형식으로 바꿔줌

        List<Ingredients> ingredientsList = new ArrayList<>();
        for (String ingredient : ingredients) {
            Ingredients newIngredient = new Ingredients();
            newIngredient.setIngredient(ingredient);
            newIngredient.setRecipe(recipe);
            ingredientsList.add(newIngredient);
        }
        ingredientsRepository.saveAll(ingredientsList);

        List<RecipeInfo> recipeInfoList = new ArrayList<>();
        for (String recipeInfo : instructions) {
            RecipeInfo info = new RecipeInfo();
            info.setInformation(recipeInfo);
            info.setRecipe(recipe);
            recipeInfoList.add(info);
        }
        recipeInfoRepository.saveAll(recipeInfoList);

        return new ResponseEntity<>(new StatusResponseDto( requestDto.getMenu() + " 레시피를 저장했습니다.", 200), HttpStatus.OK);
    }

    private void parseRecipe(List<String> ingredients, List<String> instructions, String recipe) {
        int ingredientStartIndex = recipe.indexOf("재료:");
        if (ingredientStartIndex != -1) {
            int ingredientEndIndex = recipe.indexOf("레시피:", ingredientStartIndex);
            if (ingredientEndIndex != -1) {
                String ingredientsSection = recipe.substring(ingredientStartIndex + 4, ingredientEndIndex).trim();
                extractItems(ingredientsSection, ingredients);
            }
        }

        int instructionStartIndex = recipe.indexOf("레시피:");
        if (instructionStartIndex != -1) {
            String instructionsSection = recipe.substring(instructionStartIndex + 5).trim();
            extractItems(instructionsSection, instructions);
        }
    }


    public List<GetAllRecipesResponseDto> getAllRecipes(HttpSession session) {
        List<Recipe> recipes = recipeRepository.findAllByUserId(getUserFromSession(session).getId());
        List<GetAllRecipesResponseDto> responseDtoList = new ArrayList<>();
        for (Recipe recipe : recipes) {
            responseDtoList.add(new GetAllRecipesResponseDto(recipe.getMenu(), recipe.getId()));
        }
        return responseDtoList;
    }

    public GetRecipeResponseDto getRecipeDetails(Long recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다. ID: " + recipeId));

        GetRecipeResponseDto responseDto = new GetRecipeResponseDto();
        responseDto.setMenu(recipe.getMenu());
        responseDto.setIngredients(recipe.getIngredients().stream()
                .map(Ingredients::getIngredient)
                .collect(Collectors.toList()));
        responseDto.setRecipeInfoList(recipe.getRecipeInfoList().stream()
                .map(RecipeInfo::getInformation)
                .collect(Collectors.toList()));

        return responseDto;
    }

    public ResponseEntity<?> deleteRecipe(Long recipeId, HttpSession session) {
        Long currentUserId = getUserFromSession(session).getId();
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (recipe.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 레시피입니다.");
        }
        Long ownerId = recipe.get().getUser().getId();

        if (!Objects.equals(currentUserId, ownerId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 레시피 삭제 로직
        recipeRepository.deleteById(recipeId);

        // 삭제 후 응답
        return ResponseEntity.ok("레시피가 성공적으로 삭제되었습니다.");
    }

    @Transactional
    public String updateRecipe(Long recipeId, SaveRecipeRequestDto saveRecipeRequestDto, HttpSession session) {
        // 기존의 레시피를 찾습니다.
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new IllegalArgumentException("레시피를 찾을 수 없습니다."));

        // 현재 사용자의 ID를 가져옵니다.
        Long currentUserId = getUserFromSession(session).getId();

        // 기존 레시피의 소유자와 현재 사용자를 비교하여 권한이 있는지 확인합니다.
        if (!Objects.equals(existingRecipe.getUser().getId(), currentUserId)) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        // 레시피 내용을 업데이트합니다.
        existingRecipe.setMenu(saveRecipeRequestDto.getMenu());

        // 재료(Ingredients)를 업데이트합니다.
        List<String> ingredientsList = saveRecipeRequestDto.getIngredients();
        List<Ingredients> existingIngredients = existingRecipe.getIngredients();
        existingIngredients.clear(); // 기존 재료를 모두 제거합니다.
        for (String ingredient : ingredientsList) {
            Ingredients ingredients = new Ingredients();
            ingredients.setIngredient(ingredient);
            ingredients.setRecipe(existingRecipe);
            existingIngredients.add(ingredients); // 새로운 재료를 추가합니다.
        }

        // 레시피 정보(RecipeInfo)를 업데이트합니다.
        List<String> recipeInfoList = saveRecipeRequestDto.getInstructions();
        List<RecipeInfo> existingRecipeInfoList = existingRecipe.getRecipeInfoList();
        existingRecipeInfoList.clear(); // 기존 레시피 정보를 모두 제거합니다.
        for (String recipeInfo : recipeInfoList) {
            RecipeInfo info = new RecipeInfo();
            info.setInformation(recipeInfo);
            info.setRecipe(existingRecipe);
            existingRecipeInfoList.add(info); // 새로운 레시피 정보를 추가합니다.
        }

        // 업데이트된 레시피를 저장합니다.
        recipeRepository.save(existingRecipe);

        return existingRecipe.getMenu();
    }

    private User getUserFromSession(HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 만약 세션에 사용자 정보가 없다면 로그인하지 않은 상태이므로 적절히 처리
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return loginUser;
    }

    private static void extractItems(String section, List<String> items) {
        // Split the section into lines
        String[] lines = section.split("\\n");

        // Extract items from each line
        for (String line : lines) {
            // Remove the numbering (e.g., "1. ", "2. ") and trim the line
            String item = line.replaceAll("^\\d+\\.\\s*", "").trim();
            items.add(item);
        }
    }
}
