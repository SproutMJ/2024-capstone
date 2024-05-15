package hello.aimju.gpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GptRecipeRequestDto {
    private String menu;
    private List<String> ingredients;
}
