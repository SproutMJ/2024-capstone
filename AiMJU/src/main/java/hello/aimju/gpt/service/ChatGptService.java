package hello.aimju.gpt.service;

import hello.aimju.gpt.dto.ChatCompletionDto;
import hello.aimju.gpt.dto.CompletionDto;
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

    List<Map<String, Object>> modelList();

    Map<String, Object> isValidModel(String modelName);

    Map<String, Object> legacyPrompt(List<String> ingredients);

    Map<String, Object> prompt(ChatCompletionDto chatCompletionDto);
}
