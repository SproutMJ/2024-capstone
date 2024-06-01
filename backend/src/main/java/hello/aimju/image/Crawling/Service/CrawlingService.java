package hello.aimju.image.Crawling.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class CrawlingService {

    public String fetchImageUrl(String query) throws IOException {
        // 쿼리 인코딩
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        System.out.println("Original query: " + query);
        System.out.println("Encoded query: " + encodedQuery);

        // URL 생성
        String url = "https://www.10000recipe.com/recipe/list.html?q=" + encodedQuery;
        System.out.println("URL: " + url);

        // 페이지 가져오기
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .get();
        System.out.println("Document fetched");

        // 이미지 URL 추출
        Element imgElement = doc.select("div.common_sp_thumb img:not(.common_vod_label img)").first();

        if (imgElement != null) {
            String imageUrl = imgElement.attr("src");
            System.out.println("Image URL: " + imageUrl);
            return imageUrl;
        } else {
            System.out.println("No image element found");
            return null;
        }
    }
}