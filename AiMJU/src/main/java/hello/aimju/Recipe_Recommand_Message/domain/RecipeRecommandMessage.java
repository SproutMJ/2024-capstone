package hello.aimju.Recipe_Recommand_Message.domain;

import hello.aimju.Recipe.domain.Recipe;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class RecipeRecommandMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private Long isUser;
    //    @Em
    private ChatType chatType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
