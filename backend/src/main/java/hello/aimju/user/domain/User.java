package hello.aimju.user.domain;

import hello.aimju.Board.domain.Board;
import hello.aimju.Comment.domain.Comment;
import hello.aimju.chat.chat_room.domain.ChatRoom;
import hello.aimju.recipe.domain.Recipe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String userName;
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Recipe> recipes = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ChatRoom> chatRooms = new ArrayList<>();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Board> boards = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // 레시피 개수 반환
    public int getRecipeCount() {
        return recipes.size();
    }

    // 채팅방 개수 반환
    public int getChatRoomCount() {
        return chatRooms.size();
    }

    // 게시판 개수 반환
    public int getBoardCount() {
        return boards.size();
    }

    // 댓글 개수 반환
    public int getCommentCount() {
        return comments.size();
    }
}
