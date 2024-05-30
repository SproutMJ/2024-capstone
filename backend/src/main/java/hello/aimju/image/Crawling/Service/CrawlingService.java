package hello.aimju.image.Crawling.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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
        String url = "https://search.naver.com/search.naver?ssc=tab.image.all&where=image&sm=tab_jum&query=" + query;
        System.out.println("URL: " + url);

        // 페이지 가져오기
        Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36")
                .get();
        System.out.println("Document fetched");
//        System.out.println(doc.outerHtml()); // 페이지 생김새 출력

        Elements imgElements = doc.select("div.keyword_tag_area._fe_image_tab_tag_root img"); // 이미지 태그 선택
        if (!imgElements.isEmpty()) {
            Element imgElement = imgElements.first(); // 첫 번째 이미지 태그 선택
            String imgUrl = imgElement.attr("src"); // 이미지 태그의 src 속성 값 가져오기
            System.out.println("Image URL: " + imgUrl);
            return imgUrl;
        } else {
            System.out.println("No image element found");
            return null;
        }
    }
}