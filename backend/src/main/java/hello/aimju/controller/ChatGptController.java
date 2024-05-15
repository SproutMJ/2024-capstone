package hello.aimju.controller;

import hello.aimju.gpt.dto.ChatCompletionDto;
import hello.aimju.gpt.dto.GptRecipeRequestDto;
import hello.aimju.gpt.dto.GptRecipeResponseDto;
import hello.aimju.gpt.service.ChatGptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ChatGPT API
 *
 * @author : lee
 * @fileName : ChatGPTController
 * @since : 12/29/23
 */
@Slf4j
@RestController
@RequestMapping(value = "/api")
public class ChatGptController {

    private final ChatGptService chatGptService;

    public ChatGptController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    /**
     * [API] ChatGPT 모델 리스트를 조회합니다.
     */
    @GetMapping("/modelList-gpt")
    public ResponseEntity<List<Map<String, Object>>> selectModelList() {
        List<Map<String, Object>> result = chatGptService.modelList();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * [API] ChatGPT 유효한 모델인지 조회합니다.
     *
     * @param modelName
     * @return
     */
    @GetMapping("/model-gpt")
    public ResponseEntity<Map<String, Object>> isValidModel(@RequestParam(name = "modelName") String modelName) {
        Map<String, Object> result = chatGptService.isValidModel(modelName);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * [API] 최신 ChatGPT 프롬프트 명령어를 수행합니다.
     * 사용 가능한 모델: gpt-4, gpt-4 turbo, gpt-3.5-turbo
     * @param chatCompletionDto
     * @return
     */
    @PostMapping("/prompt-gpt")
    public ResponseEntity<Map<String, Object>> selectPrompt(@RequestBody ChatCompletionDto chatCompletionDto) {
        log.debug("param :: " + chatCompletionDto.toString());
        Map<String, Object> result = chatGptService.prompt(chatCompletionDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/prompt-menu")
    public List<String> menuPrompt(@RequestBody String ingredients) {
        List<String> foods = chatGptService.extractFoodsPrompt(ingredients);
        return foods;
    }

    @PostMapping("/prompt-recipe")
    public GptRecipeResponseDto recipePrompt(@RequestBody GptRecipeRequestDto requestDto) {
        return chatGptService.extractRecipePrompt(requestDto);
    }

    /**
     * [API] Legacy ChatGPT 프롬프트 명령을 수행합니다.
     * 사용 가능한 모델: gpt-3.5-turbo-instruct, babbage-002, davinci-002
     * @param question
     * @return ResponseEntity<Map < String, Object>>
     */
    @PostMapping("/recommendation")
    public ResponseEntity<Map<String, Object>> selectLegacyPrompt(@RequestBody String question) {
        Map<String, Object> result = chatGptService.legacyPrompt(question);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/recommendation-gpt")
    public GptRecipeResponseDto selectRecipeLegacyPrompt(@RequestBody String question) {
        return chatGptService.getRecipeResponse(question);
    }

    @PostMapping("/recommendation-menu")
    public List<String> selectMenuLegacyPrompt(@RequestBody String question) {
        return chatGptService.extractFoods(question);
    }


}
