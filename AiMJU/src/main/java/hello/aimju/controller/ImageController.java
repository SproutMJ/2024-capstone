package hello.aimju.controller;

import hello.aimju.gpt.dto.CompletionDto;
import hello.aimju.roboflow.ImageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public CompletionDto uploadImage(@RequestParam("file") MultipartFile file) {
        return imageService.uploadAndProcessImage(file);
    }
}
