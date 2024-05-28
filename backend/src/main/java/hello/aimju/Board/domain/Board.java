package hello.aimju.Board.domain;

import hello.aimju.Comment.domain.Comment;
import hello.aimju.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;
    private String title;
    private LocalDate createdTime = LocalDate.now();
    @Column(columnDefinition = "TEXT")
    private String content;
    @OneToMany(mappedBy = "board",cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void update(String title, String content) {
        System.out.println("title = " + title);
        System.out.println("content = " + content);
        this.title = title;
        this.content = content;
    }
}
