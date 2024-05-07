package hello.aimju.recipe.repository;

import hello.aimju.recipe.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findAllByUserId(Long userId);
}
