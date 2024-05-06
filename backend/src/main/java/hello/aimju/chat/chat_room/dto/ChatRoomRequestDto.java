package hello.aimju.chat.chat_room.dto;

import hello.aimju.chat.chat_message.dto.ChatMessageRequestDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomRequestDto {
    private String menu;
    private Long userId;
    private List<ChatMessageRequestDto> messages;
}
