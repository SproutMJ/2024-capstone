package hello.aimju.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.aimju.chat.chat_message.dto.GetAllChatMessageResponseDto;
import hello.aimju.chat.chat_room.dto.ChatRoomRequestDto;
import hello.aimju.chat.chat_room.dto.GetAllChatRoomResponseDto;
import hello.aimju.chat.service.ChatService;
import hello.aimju.image.Service.IngredientsImageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/recommendation-save")
    public ResponseEntity<String> saveChatData(@RequestBody ChatRoomRequestDto chatRoomRequestDto, HttpSession session) {
        Long chatId = chatService.saveChatData(chatRoomRequestDto, session);
        return ResponseEntity.status(HttpStatus.CREATED).body(chatId.toString());
    }

    @GetMapping("/chatrooms")
    public List<GetAllChatRoomResponseDto> getAllChatRooms(HttpSession session) {
        return chatService.getAllChatRooms(session);
    }

    @RequestMapping(value = "/chatroom/{chatId}", method = RequestMethod.GET)
    public List<GetAllChatMessageResponseDto> getAllChatMessages(@PathVariable("chatId") Long chatId, HttpSession session) {
        return chatService.getAllChatMessages(chatId, session);
    }

    @RequestMapping(value = "/chatroom/{chatId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteChatRoom(@PathVariable("chatId") Long chatId, HttpSession session) {
        return chatService.deleteChatRoom(chatId, session);
    }
}
