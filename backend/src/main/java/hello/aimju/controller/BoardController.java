package hello.aimju.controller;

import hello.aimju.Board.dto.WriteBoardRequestDto;
import hello.aimju.Board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> writeBoard(@RequestBody WriteBoardRequestDto writeBoardRequestDto){
        return boardService.writeBoard(writeBoardRequestDto);
    }
    @GetMapping
    public ResponseEntity<?> retrieve(){
        return boardService.retrieveBoard();
    }
    @GetMapping("/{boardId}")
    public ResponseEntity<?> retrieve(@PathVariable Long id){
        return boardService.retrieveOneBoard(id);
    }
}
