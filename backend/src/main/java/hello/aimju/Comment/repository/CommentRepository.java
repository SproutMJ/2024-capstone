package hello.aimju.Comment.repository;

import hello.aimju.Board.domain.Board;
import hello.aimju.Comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserId(Long userId);

    List<Comment> findAllByBoard(Board board);
}
