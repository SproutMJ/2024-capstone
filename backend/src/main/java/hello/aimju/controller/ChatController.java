package hello.aimju.controller;

import hello.aimju.chat.chat_room.dto.ChatRoomRequestDto;
import hello.aimju.chat.chat_room.dto.GetAllChatRoomResponseDto;
import hello.aimju.chat.service.ChatService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/recommendation-save")
    public ResponseEntity<String> saveChatData(@RequestBody ChatRoomRequestDto chatRoomRequestDto, HttpSession session) {
        chatService.saveChatData(chatRoomRequestDto, session);
        return ResponseEntity.status(HttpStatus.CREATED).body("Chat data saved successfully.");
    }

    @GetMapping("/chatrooms")
    public List<GetAllChatRoomResponseDto> getAllChatRooms(HttpSession session) {
        return chatService.getAllChatRooms(session);
    }
}
