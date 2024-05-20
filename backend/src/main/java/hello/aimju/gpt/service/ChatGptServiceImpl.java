package hello.aimju.gpt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.aimju.gpt.config.ChatGptConfig;
import hello.aimju.gpt.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /***************************************************gpt-4-turbo*****************************************************
     * 1번 질문
     * 해당 식재료로 만들 수 있는 음식들을 반환해줌
     *
     * @param ingredients {}
     * @return List<String>
     */
    public List<String> extractFoodsPrompt(String ingredients) {
        String question = foodNameQuestionBuilder(ingredients); // 질문 형성
        ChatCompletionDto completionDto = chatCompletionDtoBuilder(question); // 요청 형식 형성
        Map<String, Object> resultMap = prompt(completionDto); // gpt api에 요청 및 반환
        String responseContent = extractContent(resultMap); // 반환형에서 응답만 추출
        if (responseContent == null || responseContent.isEmpty()) {
            return new ArrayList<>();
        }
        // 추출한 응답 List<String>로 반환
        return Arrays.stream(responseContent.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    /**
     * extractFoodsPrompt 에서 사용
     * 어떤 재료로 어떤 음식을 만들 수 있는지에 대한 질문을 만들어줌
     */
    private String foodNameQuestionBuilder(String ingredients) {
        return ingredients + "\\n 위 재료중 일부를 활용해 만들 수 있는 음식 딱 5개 추천해줘."
                + "\\n 형식은 반드시 아래와 같아야해 다른말은 하지 말아줘"
                + "\\n 음식이름, 음식이름, 음식이름, 음식이름, 음식이름.";
    }

    /**
     * 2번 질문
     * 선택한 음식을 만들 수 있는 레시피를 반환해줌
     *
     * @param requestDto GptRecipeRequestDto
     * @return GptRecipeResponseDto
     */
    public GptRecipeResponseDto extractRecipePrompt(GptRecipeRequestDto requestDto) {
        String ingredientsQuestion = realIngredientsQuestionBuilder(requestDto); // 질문 형성
        ChatCompletionDto ingredientsCompletionDto = chatCompletionDtoBuilder(ingredientsQuestion); // 요청 형식 형성
        Map<String, Object> ingredientsResultMap = prompt(ingredientsCompletionDto); // gpt api에 요청 및 반환
        String realIngredients = extractRealIngredients(extractContent(ingredientsResultMap)); // 반환형에서 응답만 추출

        String recipeQuestion = recipeQuestionBuilder(requestDto.getMenu(), realIngredients);
        ChatCompletionDto recipeChatCompletionDto = chatCompletionDtoBuilder(recipeQuestion);
        Map<String, Object> recipeResultMap = prompt(recipeChatCompletionDto);
        String realRecipe = extractContent(recipeResultMap);
        System.out.println(realRecipe);

        return parseRecipe(realRecipe);
    }

    public String extractRecipePromptByString(GptRecipeRequestDto requestDto) {
        String ingredientsQuestion = realIngredientsQuestionBuilder(requestDto); // 질문 형성
        ChatCompletionDto ingredientsCompletionDto = chatCompletionDtoBuilder(ingredientsQuestion); // 요청 형식 형성
        Map<String, Object> ingredientsResultMap = prompt(ingredientsCompletionDto); // gpt api에 요청 및 반환
        String realIngredients = extractRealIngredients(extractContent(ingredientsResultMap)); // 반환형에서 응답만 추출

        String recipeQuestion = recipeQuestionBuilder(requestDto.getMenu(), realIngredients);
        ChatCompletionDto recipeChatCompletionDto = chatCompletionDtoBuilder(recipeQuestion);
        Map<String, Object> recipeResultMap = prompt(recipeChatCompletionDto);

        return extractContent(recipeResultMap);
    }

    /**
     * extractRecipePrompt 에서 사용
     * 해당 음식을 만드는데 꼭 필요한 재료를 추출하는 질문을 만들어줌
     */
    private String realIngredientsQuestionBuilder(GptRecipeRequestDto requestDto) {
        String ingredientsStr = String.join(", ", requestDto.getIngredients());
        return requestDto.getMenu() + "를 만드는데 아래의 식재료들이 모두 필요할까?\\n"
                + ingredientsStr + "\\n 필요없는 식재료는 빼주고 필요한 식재료는 추가해줘"
                + "\\n 형식은 반드시 아래와 같아야해 다른말은 하지 말아줘"
                + "\\n 수정된 식재료: 식재료1, 식재료2, 식재료3....";
    }

    public String extractRealIngredients(String ingredientsResult) {
        int colonIndex = ingredientsResult.indexOf(":");

        if (colonIndex != -1 && colonIndex < ingredientsResult.length() - 1) {
            return ingredientsResult.substring(colonIndex + 2);
        } else {
            return ingredientsResult;
        }
    }

    /**
     * extractRecipePrompt 에서 사용
     * 어떤 재료로 어떤 음식을 만드는데 필요한 레시피를 얻기위한 질문
     */
    private String recipeQuestionBuilder(String menu, String ingredients) {
        return ingredients + "를 가지고 " + menu + " 1인분을 만들꺼야\\n"
                + "구체적인 재료와 레시피를 추천해주는데 형식은 반드시 아래와 같아야해 다른말은 하지 말아줘"
                + "\\n메뉴: 음식이름"
                + "재료:\\n1.재료1\\n2.재료2\\n3.재료3\\n"
                + "레시피:\\n1.요리순서1\\n2.요리순서2\\n3.요리순서3";
    }

    /**
     * extractRecipePrompt 에서 사용
     * 얻은 레시피를 원하는 형식으로 바꿔줌
     */
    private static GptRecipeResponseDto parseRecipe(String recipe) {
        List<String> ingredients = new ArrayList<>();
        List<String> instructions = new ArrayList<>();

        int ingredientStartIndex = recipe.indexOf("재료:");
        if (ingredientStartIndex != -1) {
            int ingredientEndIndex = recipe.indexOf("레시피:", ingredientStartIndex);
            if (ingredientEndIndex != -1) {
                String ingredientsSection = recipe.substring(ingredientStartIndex + 4, ingredientEndIndex).trim();
                extractItems(ingredientsSection, ingredients);
            }
        }

        int instructionStartIndex = recipe.indexOf("레시피:");
        if (instructionStartIndex != -1) {
            String instructionsSection = recipe.substring(instructionStartIndex + 5).trim();
            extractItems(instructionsSection, instructions);
        }

        return new GptRecipeResponseDto(ingredients, instructions);
    }

    private static void extractItems(String section, List<String> items) {
        // Split the section into lines
        String[] lines = section.split("\\n");

        // Extract items from each line
        for (String line : lines) {
            // Remove the numbering (e.g., "1. ", "2. ") and trim the line
            String item = line.replaceAll("^\\d+\\.\\s*", "").trim();
            items.add(item);
        }
    }

    /**
     * 모든 extractPrompt 에서 사용
     * 질문을 포함한 유효한 request 형식을 만들어줌
     */
    private ChatCompletionDto chatCompletionDtoBuilder(String question) {
        String model = "gpt-4-turbo";
        List<ChatRequestMsgDto> messages = new ArrayList<>();

        ChatRequestMsgDto message = ChatRequestMsgDto.builder()
                .role("system")
                .content(question)
                .build();

        messages.add(message);

        return ChatCompletionDto.builder()
                .model(model)
                .messages(messages)
                .build();
    }

    /**
     * 모든 extractPrompt 에서 사용
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

    /**
     * 모든 extractPrompt 에서 사용
     * gpt의 response에서 원하는 값(질문에 대한 대답)만 추출
     */
    public String extractContent(Map<String, Object> resultMap) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) resultMap.get("choices");

            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> choice = choices.get(0);

                Map<String, Object> message = (Map<String, Object>) choice.get("message");

                if (message != null) {
                    return (String) message.get("content");
                }
            }
        } catch (ClassCastException e) {
            log.debug("ClassCastException :: " + e.getMessage());
        }
        return null;
    }


    /************************************************gpt-3.5-turbo-instruct*********************************************
     * gpt-3.5-turbo-instruct용 함수
     *
     * @param question {}
     * 음식 + 재료를 포함한 레시피에 대한 질문을 question으로 받아줌
     * @return GptRecipeResponseDto
     * question에 대한 응답을 {재료:{재료1, 재료2...} 레시피:{레시피1, 레시피2...}} 형식으로 해줌
     */
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

    /**
     * gpt-3.5-turbo-instruct용 함수
     *
     * @param question {}
     * 어떤 재료로 어떤 음식을 만들 수 있는지에 대한 질문
     * @return List<String>
     * question에 대한 응답을 {음식1, 음식2, 음식3, 음식4, 음식5}형식으로 해줌
     */
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
     * @param question {}
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
