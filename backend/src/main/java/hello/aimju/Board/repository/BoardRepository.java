package hello.aimju.Board.repository;

import hello.aimju.Board.domain.Board;
import hello.aimju.recipe.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByUserId(Long userId);

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
}