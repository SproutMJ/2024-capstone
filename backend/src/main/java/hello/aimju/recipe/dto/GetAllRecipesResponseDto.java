package hello.aimju.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetAllRecipesResponseDto {
    private String menu;
    private Long recipeId;
}
