package hello.aimju.recipe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetRecipeResponseDto {
    private String menu;
    private List<String> Ingredients;
    private List<String> RecipeInfoList;
}
