package hello.aimju.controller;

import hello.aimju.image.Crawling.Service.CrawlingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class CrawlingController {

    private final CrawlingService crawlingService;

    @GetMapping("/menu-image/{query}")
    public String fetchImage(@PathVariable("query") String query) {
        try {
            return crawlingService.fetchImageUrl(query);
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Unable to fetch image";
        }
    }
}
