package hello.aimju.recipe.recipe_info.repository;

import hello.aimju.recipe.recipe_info.domain.RecipeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeInfoRepository extends JpaRepository<RecipeInfo, Long> {
    // RecipeInfo 엔티티에 대한 추가적인 메서드가 필요한 경우 여기에 추가할 수 있습니다.
}
