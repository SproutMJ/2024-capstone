package hello.aimju.Comment.service;


import hello.aimju.Board.domain.Board;
import hello.aimju.Board.dto.ModifyBoardRequestDto;
import hello.aimju.Board.dto.ModifyBoardResponseDto;
import hello.aimju.Board.repository.BoardRepository;
import hello.aimju.Comment.domain.Comment;
import hello.aimju.Comment.dto.ModifyCommentRequestDto;
import hello.aimju.Comment.dto.ModifyCommentResponseDto;
import hello.aimju.Comment.dto.RetrieveCommentsResponseDto;
import hello.aimju.Comment.dto.WriteCommentRequestDto;
import hello.aimju.Comment.repository.CommentRepository;
import hello.aimju.login.session.SessionConst;
import hello.aimju.user.domain.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    public void writeComment(WriteCommentRequestDto writeCommentRequestDto, HttpSession session) {
        LocalDate now = LocalDate.now();
        Comment comment = Comment.builder()
                        .createdComment(now)
                        .content(writeCommentRequestDto.getContent())
                        .board(boardRepository.findById(writeCommentRequestDto.getBoardId()).get())
                        .user(getUserFromSession(session))
                        .build();

        commentRepository.save(comment);
    }


    public ResponseEntity<?> retrieveComments(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        List<Comment> boardComments = commentRepository.findAllByBoard(board);
        List<RetrieveCommentsResponseDto> getCommentLists = boardComments.stream()
                .map(tmp -> RetrieveCommentsResponseDto.builder()
                        .id(tmp.getId())
                        .name(tmp.getUser().getUserName())
                        .content(tmp.getContent())
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(getCommentLists,HttpStatus.OK);
    }
    public ResponseEntity<?> modifyBoard(ModifyCommentRequestDto modifyCommentRequestDto, HttpSession session) throws Exception {
        Comment comment = commentRepository.findById(modifyCommentRequestDto.getId()).get();
        checkUser(getUserFromSession(session), comment.getUser());
        comment.update(modifyCommentRequestDto.getContent());
        ModifyCommentResponseDto resComment = ModifyCommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
        commentRepository.save(comment);
        return new ResponseEntity<>(resComment, HttpStatus.OK);
    }
    public void deleteComment(Long commentId, HttpSession session) throws Exception {
        User userFromSession = getUserFromSession(session);
        Comment comment = commentRepository.findById(commentId).get();
        User user = comment.getUser();
        System.out.println("userFromSession.getId() = " + userFromSession.getId());
        System.out.println("user.getId() = " + user.getId());
        if (userFromSession.getId() != user.getId()){
            throw new Exception("자신의 댓글만 삭제할 수 있어요");
        }
        commentRepository.deleteById(commentId);
    }

    private User getUserFromSession(HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        User loginUser = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 만약 세션에 사용자 정보가 없다면 로그인하지 않은 상태이므로 적절히 처리
        if (loginUser == null) {
            throw new IllegalArgumentException("로그인이 필요합니다.");
        }

        return loginUser;
    }
    private void checkUser(User live, User save) throws Exception {
        if (live.getId() != save.getId()){
            throw new Exception("자신의 댓글만 삭제할 수 있어요");
        }
    }
}
