package hello.aimju.roboflow;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.aimju.gpt.dto.CompletionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ImageService {

    // Roboflow API 엔드포인트
    private static final String ROBOFLOW_API_ENDPOINT = "https://detect.roboflow.com/your-model/42";

    // Roboflow API 키
    @Value("${roboflow.api-key}")
    private String ROBOFLOW_API_KEY;

    public CompletionDto uploadAndProcessImage(MultipartFile imageFile) {
        try {
            // Base64 인코딩
            String encodedFile = new String(Base64.getEncoder().encode(imageFile.getBytes()), StandardCharsets.US_ASCII);

            // 업로드 URL 생성
            String uploadURL = "https://detect.roboflow.com/foodey/3?api_key=" + ROBOFLOW_API_KEY + "&name=YOUR_IMAGE.jpg";

            // HTTP 요청 설정
            HttpURLConnection connection = null;
            try {
                // URL에 연결
                URL url = new URL(uploadURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", Integer.toString(encodedFile.getBytes().length));
                connection.setRequestProperty("Content-Language", "en-US");
                connection.setUseCaches(false);
                connection.setDoOutput(true);

                // 요청 보내기
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(encodedFile);
                wr.close();

                // 응답 받기
                InputStream stream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }
                reader.close();

                // 예측 결과를 CompletionDto로 변환하여 반환
                List<String> classNames = extractClassNamesFromResponse(responseBuilder.toString());
                String prompt = String.join(" ", classNames) + "으로 만들 수 있는 음식 하나를 추천해주고, 필요한 재료와 순차적으로 레시피를 설명해줘";

                return CompletionDto.builder()
                        .model("gpt-3.5-turbo-instruct")
                        .prompt(prompt)
                        .temperature(0.3f)
                        .max_tokens(1000)
                        .build();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // 예측 결과를 받지 못한 경우
    }

    private List<String> extractClassNamesFromResponse(String response) {
        List<String> classNames = new ArrayList<>();

        try {
            // JSON 파싱을 위한 ObjectMapper 생성
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON 문자열을 JsonNode로 변환
            JsonNode rootNode = objectMapper.readTree(response);

            // "predictions" 배열의 각 객체에서 "class" 필드 값을 추출하여 리스트에 추가
            if (rootNode.has("predictions")) {
                JsonNode predictionsNode = rootNode.get("predictions");
                if (predictionsNode.isArray()) {
                    for (JsonNode predictionNode : predictionsNode) {
                        if (predictionNode.has("class")) {
                            String className = predictionNode.get("class").asText();
                            classNames.add(className);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classNames;
    }
}
