package hello.aimju.controller;

import hello.aimju.gpt.dto.CompletionDto;
import hello.aimju.image.ImageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /*
    * 사진 업로드 후 재료 인식
    * */
    @PostMapping("/photo-recognition")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        return imageService.uploadAndProcessImage(file);
    }

    /*
    * 인식한 재료 확인 후 CompletionDto 만들어줌
    * */
    @PostMapping("recommendation")
    public CompletionDto checkIngredients(@RequestBody List<String> ingredients) {
        return imageService.checkAndConfirmIngredients(ingredients);
    }
}
