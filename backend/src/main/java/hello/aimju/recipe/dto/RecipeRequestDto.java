package hello.aimju.recipe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeRequestDto {
    private String menu;
    private Long userId;
    private List<String> Ingredients;
    private List<String> RecipeInfoList;
}
