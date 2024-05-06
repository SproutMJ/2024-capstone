package hello.aimju.recipe.repository;

import hello.aimju.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    // Recipe 엔티티에 대한 추가적인 메서드가 필요한 경우 여기에 추가할 수 있습니다.
}
