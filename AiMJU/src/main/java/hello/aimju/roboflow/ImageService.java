package hello.aimju.roboflow;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class ImageService {

    // Roboflow API 엔드포인트
    private static final String ROBOFLOW_API_ENDPOINT = "https://detect.roboflow.com/your-model/42";

    // Roboflow API 키
    @Value("${roboflow.api-key}")
    private String ROBOFLOW_API_KEY;

    private String fake_key = "1111111111";

    public void uploadImage(MultipartFile imageFile) {
        try {
            // Base64 인코딩
            String encodedFile = new String(Base64.getEncoder().encode(imageFile.getBytes()), StandardCharsets.US_ASCII);

            // 업로드 URL 생성
            String uploadURL = "https://detect.roboflow.com/" + "food_ingredients-r1qox/1" + "?api_key=" + ROBOFLOW_API_KEY
                    + "&name=YOUR_IMAGE.jpg";

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
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                reader.close();
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
    }
}
