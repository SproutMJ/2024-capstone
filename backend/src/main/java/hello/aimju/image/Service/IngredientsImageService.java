package hello.aimju.image.Service;

import hello.aimju.chat.chat_room.domain.ChatRoom;
import hello.aimju.chat.chat_room.repository.ChatRoomRepository;
import hello.aimju.image.entity.IngredientsImage;
import hello.aimju.image.repository.IngredientsImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class IngredientsImageService {
    private final IngredientsImageRepository ingredientsImageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ResponseEntity<String> saveImage(MultipartFile imageFile, Long chatId) throws IOException {
        String fileName = imageFile.getOriginalFilename();
        byte[] imageData = imageFile.getBytes();

        IngredientsImage image = new IngredientsImage();
        image.setName(fileName);
        image.setImage(imageData);

        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다. ID: " + chatId));

        image.setChatRoom(chatRoom);
        ingredientsImageRepository.save(image);

        // 성공적인 응답 반환
        return ResponseEntity.status(HttpStatus.CREATED).body("채팅이 저장되었습니다.");
    }

    public IngredientsImage findByChatId(Long chatId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다. ID: " + chatId));
        return ingredientsImageRepository.findByChatRoom(chatRoom);
    }
}
