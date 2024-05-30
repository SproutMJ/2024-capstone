package hello.aimju.controller;

import hello.aimju.image.Service.ImageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public String uploadImage(@RequestParam("file") MultipartFile file, HttpSession httpSession) throws IOException {
        return imageService.uploadAndProcessImage(file,httpSession);
    }

}
