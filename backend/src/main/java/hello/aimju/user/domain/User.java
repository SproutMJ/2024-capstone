package hello.aimju.user.domain;

import hello.aimju.Board.domain.Board;
import hello.aimju.Comment.domain.Comment;
import hello.aimju.recipe.domain.Recipe;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String userName;
    private String password;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Recipe> recipes = new ArrayList<>();
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
