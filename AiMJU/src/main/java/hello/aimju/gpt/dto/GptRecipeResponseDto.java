package hello.aimju.gpt.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GptRecipeResponseDto {
    private List<String> ingredients;
    private List<String> instructions;
}
