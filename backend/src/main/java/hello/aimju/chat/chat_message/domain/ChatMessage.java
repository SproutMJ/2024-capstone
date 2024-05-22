package hello.aimju.chat.chat_message.domain;

import hello.aimju.chat.chat_room.domain.ChatRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Long isUser;
    //    @Em
    private ChatType chatType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private ChatRoom chatRoom;

    public ChatMessage(String content, Long isUser, ChatType chatType, ChatRoom chatRoom) {
        this.content = content;
        this.isUser = isUser;
        this.chatType = chatType;
        this.chatRoom = chatRoom;
    }
}
