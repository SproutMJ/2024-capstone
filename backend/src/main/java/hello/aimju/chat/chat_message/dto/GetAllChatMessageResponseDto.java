package hello.aimju.chat.chat_message.dto;

import hello.aimju.chat.chat_message.domain.ChatType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetAllChatMessageResponseDto {
    private ChatType chatType;
    private Long isUser;
    private String content;
}
