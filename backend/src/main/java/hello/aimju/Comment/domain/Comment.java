package hello.aimju.Comment.domain;

import hello.aimju.Board.domain.Board;
import hello.aimju.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
@Entity
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime createdComment;
    private String content;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_Id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
}
