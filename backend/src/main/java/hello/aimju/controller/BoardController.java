package hello.aimju.controller;

import hello.aimju.Board.dto.ModifyBoardRequestDto;
import hello.aimju.Board.dto.WriteBoardRequestDto;
import hello.aimju.Board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> writeBoard(@RequestBody WriteBoardRequestDto writeBoardRequestDto, HttpSession session){
        boardService.writeBoard(writeBoardRequestDto,session);
        return ResponseEntity.status(HttpStatus.CREATED).body("잘만들음");
    }
    @GetMapping
    public ResponseEntity<?> retrieve(@RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "5") int size,
                                      @RequestParam(required = false) String searchKeyword){
        if (searchKeyword == null) {
            return boardService.retrieveBoard(page, size);
        }else{
            return boardService.retrieveSearchBoard(page, size,searchKeyword);
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveOneBoard(@PathVariable Long id){
        return boardService.retrieveOneBoard(id);
    }
    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId,HttpSession session) throws Exception {
        boardService.deleteBoard(boardId,session);
        return ResponseEntity.status(HttpStatus.OK).body("댓글삭제완료");
    }
    @PutMapping
    public ResponseEntity<?> modifyBoard(@RequestBody ModifyBoardRequestDto modifyBoardRequestDto, HttpSession session) throws Exception {
        return boardService.modifyBoard(modifyBoardRequestDto,session);
    }
}
