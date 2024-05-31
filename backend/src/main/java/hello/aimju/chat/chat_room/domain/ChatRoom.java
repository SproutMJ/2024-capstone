package hello.aimju.chat.chat_room.domain;

import hello.aimju.chat.chat_message.domain.ChatMessage;
import hello.aimju.image.entity.IngredientsImage;
import hello.aimju.user.domain.User;
import hello.aimju.timestamp.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom extends Timestamped{ // created_at 알아서 생성해줌
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;
    private String menu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> chatMessages = new ArrayList<>();
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<IngredientsImage> ingredientsImages = new ArrayList<>();

    public ChatRoom(User user, String menu) {
        this.user = user;
        this.menu = menu;
    }
}
