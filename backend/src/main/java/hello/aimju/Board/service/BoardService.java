package hello.aimju.Board.service;

import hello.aimju.Board.dto.WriteBoardRequestDto;
import hello.aimju.Board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public ResponseEntity<?> writeBoard(WriteBoardRequestDto writeBoardRequestDto) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> retrieveBoard() {
    }

    public ResponseEntity<?> retrieveOneBoard(Long id) {
    }
}
