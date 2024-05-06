package hello.aimju.recipe.domain;

import hello.aimju.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;
    private String menu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
