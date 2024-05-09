package hello.aimju.controller;

import hello.aimju.Comment.dto.WriteCommentRequestDto;
import hello.aimju.Comment.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> writeBoard(@RequestBody WriteCommentRequestDto writeCommentRequestDto, HttpSession session){
        commentService.writeComment(writeCommentRequestDto,session);
        return ResponseEntity.status(HttpStatus.CREATED).body("댓글잘만듬");
    }
    @GetMapping("/{boardId}")
    public ResponseEntity<?> retrieveComment(@PathVariable Long boardId){
        return commentService.retrieveComments(boardId);
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글삭제완료");
    }
}
