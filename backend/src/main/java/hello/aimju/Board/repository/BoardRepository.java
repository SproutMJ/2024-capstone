package hello.aimju.Board.repository;

import hello.aimju.Board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByUserId(Long userId);

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);

    Page<Board> findByUserIdAndTitleContaining(Long userId, String searchKeyword, Pageable pageable);

    Page<Board> findAllByUserId(Long userId, Pageable pageable);
}
