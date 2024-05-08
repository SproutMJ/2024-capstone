package hello.aimju.controller;

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
    public ResponseEntity<?> retrieve(){
        return boardService.retrieveBoard();
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> retrieveOneBoard(@PathVariable Long id){
        return boardService.retrieveOneBoard(id);
    }
}
