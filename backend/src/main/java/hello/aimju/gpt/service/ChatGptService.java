package hello.aimju.gpt.service;

import hello.aimju.gpt.dto.ChatCompletionDto;
import hello.aimju.gpt.dto.GptRecipeRequestDto;
import hello.aimju.gpt.dto.GptRecipeResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ChatGPT 서비스 인터페이스
 *
 * @author : lee
 * @fileName : ChatGPTService
 * @since : 12/29/23
 */

@Service
public interface ChatGptService {
    //***************************************사용중*************************************

    Map<String, Object> prompt(ChatCompletionDto chatCompletionDto);

    List<String> extractFoodsPrompt(String ingredients);

    GptRecipeResponseDto extractRecipePrompt(GptRecipeRequestDto requestDto);

    String extractRecipePromptByString(GptRecipeRequestDto requestDto);

    //***************************************사용 x*************************************

    List<Map<String, Object>> modelList();

    Map<String, Object> isValidModel(String modelName);

    Map<String, Object> legacyPrompt(String question);

    GptRecipeResponseDto getRecipeResponse(String question);

    List<String> extractFoods(String question);
}
