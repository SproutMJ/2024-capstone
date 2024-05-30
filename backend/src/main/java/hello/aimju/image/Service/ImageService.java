package hello.aimju.image.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hello.aimju.image.Enum.IngredientMapper;
import hello.aimju.login.session.SessionConst;
import hello.aimju.tteesstt.Image;
import hello.aimju.tteesstt.ImageRepository;
import hello.aimju.user.domain.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class ImageService {
    // Roboflow API 키
    @Value("${roboflow.api-key}")
    private String ROBOFLOW_API_KEY;

    // Roboflow API Endpoint
    @Value("${roboflow.api-endpoint}")
    private String ROBOFLOW_API_ENDPOINT;
    @Autowired
    private ImageRepository imageRepository;

    /**
     * roboflow 모델 API에 이미지 보내서 인식된 재료를 String 형으로 가져옴
     * @return : "파, 마늘, 양파, 삼겹살, ..."
     * */
    public String uploadAndProcessImage(MultipartFile imageFile, HttpSession httpSession) throws IOException {
        //이미지 저장 로직
        String fileName = imageFile.getOriginalFilename();
        byte[] imageData = imageFile.getBytes();
//        User user = getUserFromSession(httpSession);

        Image image = new Image();
        image.setName(fileName);
        image.setImage(imageData);
//        image.setUser(user);
        imageRepository.save(image);

        try {
            // Base64 인코딩
            String encodedFile = new String(Base64.getEncoder().encode(imageFile.getBytes()), StandardCharsets.US_ASCII);

            // 업로드 URL 생성
            String uploadURL = ROBOFLOW_API_ENDPOINT + "?api_key=" + ROBOFLOW_API_KEY + "&name=YOUR_IMAGE.jpg";

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

                // 예측 결과를 String 형으로 변환하여 반환

                return extractClassIdFromResponse(responseBuilder.toString());
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

    /**
     * 클래스 이름(재료명) 추출
     * 재료가 많아질 시, class 이름으로 중복 검사하기에는 시간이 오래걸림
     * 지금은 사용하지 않음
     * */
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

    /**
     * class_id 중복을 없애줌
     * 이후 joinIngredients 사용
     * */
    private String extractClassIdFromResponse(String response) {
        Set<Integer> classIds = new HashSet<>(); // 빠른 중복검사를 위해 HashSet 사용
        String ingredients = "";
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
                        if (predictionNode.has("class_id")) {
                            int classId = predictionNode.get("class_id").asInt();
                            System.out.println(predictionNode.get("class").asText() + classId);
                            classIds.add(classId);
                        }
                    }
                }
            }
            ingredients = joinIngredients(classIds);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    /**
     * IngredientMapper Enum Class를 이용해서 class_id에 맞는 재료의 한글명 가져옴
     * */
    private String joinIngredients(Set<Integer> classIds) {
        StringJoiner resultJoiner = new StringJoiner(", ");

        // classIds에 대해 반복하면서 IngredientMapper를 참조하여 문자열 구성
        for (int classId : classIds) {
            try {
                // 정적(static) 메서드 호출: 클래스 이름으로 직접 호출
                IngredientMapper ingredient = IngredientMapper.getByClassId(classId);
                String className = ingredient.getClassName();

                // StringJoiner에 클래스 이름 추가
                resultJoiner.add(className);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid class_id: " + classId);
                // 예외 처리: 유효하지 않은 class_id에 대한 처리
            }
        }

        // StringJoiner를 사용하여 최종 결과 문자열 생성
        return resultJoiner.toString();
    }

    private User getUserFromSession(HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 만약 세션에 사용자 정보가 없다면 로그인하지 않은 상태이므로 적절히 처리
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return loginUser;
    }
}
