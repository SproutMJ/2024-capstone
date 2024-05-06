package hello.aimju.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.aimju.gpt.config.ChatGptConfig;
import hello.aimju.gpt.dto.ChatCompletionDto;
import hello.aimju.gpt.dto.CompletionDto;
import hello.aimju.gpt.dto.GptRecipeResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * ChatGPT Service 구현체
 *
 * @author : lee
 * @fileName : ChatGPTServiceImpl
 * @since : 12/29/23
 */
@Slf4j
@Service
public class ChatGptServiceImpl implements ChatGptService {

    private final ChatGptConfig chatGPTConfig;

    public ChatGptServiceImpl(ChatGptConfig chatGptConfig) {
        this.chatGPTConfig = chatGptConfig;
    }

    @Value("${openai.url.model}")
    private String modelUrl;

    @Value("${openai.url.model-list}")
    private String modelListUrl;

    @Value("${openai.url.prompt}")
    private String promptUrl;

    @Value("${openai.url.legacy-prompt}")
    private String legacyPromptUrl;

    /**
     * 사용 가능한 모델 리스트를 조회하는 비즈니스 로직
     *
     * @return List<Map < String, Object>>
     */
    @Override
    public List<Map<String, Object>> modelList() {
        log.debug("[+] 모델 리스트를 조회합니다.");
        List<Map<String, Object>> resultList = null;

        // [STEP1] 토큰 정보가 포함된 Header를 가져옵니다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // [STEP2] 통신을 위한 RestTemplate을 구성합니다.
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(modelUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            // [STEP3] Jackson을 기반으로 응답값을 가져옵니다.
            ObjectMapper om = new ObjectMapper();
            Map<String, Object> data = om.readValue(response.getBody(), new TypeReference<>() {
            });

            // [STEP4] 응답 값을 결과값에 넣고 출력을 해봅니다.
            resultList = (List<Map<String, Object>>) data.get("data");
            for (Map<String, Object> object : resultList) {
                log.debug("ID: " + object.get("id"));
                log.debug("Object: " + object.get("object"));
                log.debug("Created: " + object.get("created"));
                log.debug("Owned By: " + object.get("owned_by"));
            }
        } catch (JsonMappingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (JsonProcessingException e) {
            log.debug("JsonProcessingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return resultList;
    }

    /**
     * 모델이 유효한지 확인하는 비즈니스 로직
     *
     * @param modelName {}
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> isValidModel(String modelName) {
        log.debug("[+] 모델이 유효한지 조회합니다. 모델 : " + modelName);
        Map<String, Object> result = new HashMap<>();

        // [STEP1] 토큰 정보가 포함된 Header를 가져옵니다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // [STEP2] 통신을 위한 RestTemplate을 구성합니다.
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(modelListUrl + "/" + modelName, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        try {
            // [STEP3] Jackson을 기반으로 응답값을 가져옵니다.
            ObjectMapper om = new ObjectMapper();
            result = om.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return result;
    }

    /**
     * 신규 모델에 대한 프롬프트
     *
     * @param chatCompletionDto {}
     * @return chatCompletionDto
     */
    @Override
    public Map<String, Object> prompt(ChatCompletionDto chatCompletionDto) {
        log.debug("[+] 신규 프롬프트를 수행합니다.");

        Map<String, Object> resultMap = new HashMap<>();

        // [STEP1] 토큰 정보가 포함된 Header를 가져옵니다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // [STEP5] 통신을 위한 RestTemplate을 구성합니다.
        HttpEntity<ChatCompletionDto> requestEntity = new HttpEntity<>(chatCompletionDto, headers);
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);
        try {
            // [STEP6] String -> HashMap 역직렬화를 구성합니다.
            ObjectMapper om = new ObjectMapper();
            resultMap = om.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return resultMap;
    }


    public GptRecipeResponseDto getRecipeResponse(String question) {
        Map<String, Object> resultMap = legacyPrompt(question);
        GptRecipeResponseDto gptRecipeResponseDto = new GptRecipeResponseDto();
        List<String> ingredients = new ArrayList<>();
        List<String> instructions = new ArrayList<>();

        // 결과를 RecipeResponseDto에 매핑
        if (resultMap.containsKey("choices")) {
            List<Map<String, String>> choices = (List<Map<String, String>>) resultMap.get("choices");
            if (!choices.isEmpty()) {
                String text = choices.get(0).get("text");
                // "재료:" 다음 부분을 재료로 설정
                int ingredientStartIndex = text.indexOf("재료:") + "재료:".length();
                int recipeStartIndex = text.indexOf("레시피:");

                if (ingredientStartIndex != -1 && recipeStartIndex != -1) {
                    String ingredientsText = text.substring(ingredientStartIndex, recipeStartIndex).trim();
                    String[] ingredientLines = ingredientsText.split("\n");
                    for (String line : ingredientLines) {
                        ingredients.add(line.trim());
                    }
                    gptRecipeResponseDto.setIngredients(ingredients);

                    // "레시피:" 다음 부분을 레시피로 설정
                    String recipeText = text.substring(recipeStartIndex + "레시피:".length()).trim();
                    String[] recipeLines = recipeText.split("\n");
                    for (String line : recipeLines) {
                        instructions.add(line.trim());
                    }
                    gptRecipeResponseDto.setInstructions(instructions);
                }
            }
        }
        return gptRecipeResponseDto;
    }

    public List<String> extractFoods(String question) {
        Map<String, Object> resultMap = legacyPrompt(question);
        List<String> foods = new ArrayList<>();

        if (resultMap.containsKey("choices")) {
            List<Map<String, String>> choices = (List<Map<String, String>>) resultMap.get("choices");
            if (!choices.isEmpty()) {
                String text = choices.get(0).get("text");

                // 쉼표로 나누어 음식 목록을 추출하는 부분
                foods = Arrays.asList(text.split(", "));
            }
        }

        return foods;
    }



    /**
     * ChatGTP 프롬프트 검색
     *
     * @param question
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> legacyPrompt(String question) {
        log.debug("[+] 레거시 프롬프트를 수행합니다.");

        CompletionDto completionDto = makePrompt(question);

        // [STEP1] 토큰 정보가 포함된 Header를 가져옵니다.
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // [STEP5] 통신을 위한 RestTemplate을 구성합니다.
        HttpEntity<CompletionDto> requestEntity = new HttpEntity<>(completionDto, headers);
        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(legacyPromptUrl, HttpMethod.POST, requestEntity, String.class);

        Map<String, Object> resultMap = new HashMap<>();
        try {
            ObjectMapper om = new ObjectMapper();
            // [STEP6] String -> HashMap 역직렬화를 구성합니다.
            resultMap = om.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (RuntimeException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }
        return resultMap;
    }


    /**
     * 인식한 재료 확인 후 CompletionDto 만들어줌
     */
    private CompletionDto makePrompt(String question) {
        return CompletionDto.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt(question)
                .temperature(0.8f)
                .max_tokens(1000)
                .build();
    }
}
