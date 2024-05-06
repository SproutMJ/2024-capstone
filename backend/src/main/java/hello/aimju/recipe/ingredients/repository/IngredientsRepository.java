package hello.aimju.recipe.ingredients.repository;

import hello.aimju.recipe.ingredients.domain.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {
    // Ingredients 엔티티에 대한 추가적인 메서드가 필요한 경우 여기에 추가할 수 있습니다.
}
