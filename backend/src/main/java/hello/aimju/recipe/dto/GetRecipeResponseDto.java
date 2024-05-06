package hello.aimju.recipe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetRecipeResponseDto {
    private String menu;
    private Long recipeId;
}
