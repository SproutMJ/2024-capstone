package hello.aimju.controller;

import hello.aimju.image.Service.IngredientsImageService;
import hello.aimju.image.Service.RoboflowService;
import hello.aimju.image.entity.IngredientsImage;
import hello.aimju.user.dto.StatusResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class IngredientsImageController {

    private final RoboflowService roboflowService;
    private final IngredientsImageService ingredientsImageService;

    /*
    * 사진 업로드 후 재료 인식
    * */
    @PostMapping("/photo-recognition")
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        return roboflowService.uploadAndProcessImage(file);
    }

    /*
     * 사진 저장...사용 x
     * */
    @PostMapping("/photo-save")
    public ResponseEntity<String> saveImage(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("recipeId") String recipeId) throws IOException{
        if (recipeId.isEmpty() || file.isEmpty()) {
            return null;
        }
        Long numberId = Long.parseLong(recipeId);
        System.out.println("저장 요청 도착: " + numberId);
        return ingredientsImageService.saveImage(file, numberId);
    }

    @GetMapping("/photo/{chatId}")
    public ResponseEntity<byte[]> getImageByChatId(@PathVariable("chatId") Long chatId) {
        IngredientsImage image = ingredientsImageService.findByChatId(chatId);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // 적절한 미디어 타입으로 설정
                .body(image.getImage());
    }
}
