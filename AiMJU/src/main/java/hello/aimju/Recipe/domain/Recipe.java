package hello.aimju.Recipe.domain;

import hello.aimju.Recipe_Recommand_Message.domain.RecipeRecommandMessage;
import hello.aimju.User.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;
    private String menu;
    private Long scrap;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "recipe")
    private List<RecipeRecommandMessage> recipeRecommandMessages = new ArrayList<>();
}
