package hello.aimju.controller;

import hello.aimju.Board.dto.ModifyBoardRequestDto;
import hello.aimju.Comment.dto.ModifyCommentRequestDto;
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
    @RequestMapping(value = "/{boardId}", method = RequestMethod.GET)
    public ResponseEntity<?> retrieveComment(@PathVariable("boardId") Long boardId){
        return commentService.retrieveComments(boardId);
    }
    @RequestMapping(value = "/{commentId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId,HttpSession session) throws Exception {
        commentService.deleteComment(commentId,session);
        return ResponseEntity.status(HttpStatus.OK).body("댓글삭제완료");
    }
    @PatchMapping
    public ResponseEntity<?> modifyComment(@RequestBody ModifyCommentRequestDto modifyBoardRequestDto, HttpSession session) throws Exception {
        return commentService.modifyBoard(modifyBoardRequestDto,session);

    }
}
