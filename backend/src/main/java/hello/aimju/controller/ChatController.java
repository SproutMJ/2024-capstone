package hello.aimju.controller;

import hello.aimju.chat.chat_room.dto.ChatRoomRequestDto;
import hello.aimju.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/recommendation-save")
    public ResponseEntity<String> saveChatData(@RequestBody ChatRoomRequestDto chatRoomRequestDto) {
        chatService.saveChatData(chatRoomRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Chat data saved successfully.");
    }
}
