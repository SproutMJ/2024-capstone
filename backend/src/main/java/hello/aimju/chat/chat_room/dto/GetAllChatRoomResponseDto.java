package hello.aimju.chat.chat_room.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class GetAllChatRoomResponseDto {
    private Long chatId;
    private LocalDateTime createdAt;
    private String menu;
}
