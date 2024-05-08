package hello.aimju.Board.service;

import hello.aimju.Board.domain.Board;
import hello.aimju.Board.dto.RetrieveBoardResponseDto;
import hello.aimju.Board.dto.WriteBoardRequestDto;
import hello.aimju.Board.repository.BoardRepository;
import hello.aimju.login.session.SessionConst;
import hello.aimju.user.domain.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public void writeBoard(WriteBoardRequestDto writeBoardRequestDto, HttpSession session) {
        LocalDate now = LocalDate.now();
        Board board = Board.builder()
                .title(writeBoardRequestDto.getTitle())
                .created_Time(now)
                .content(writeBoardRequestDto.getContent())
                .user(getUserFromSession(session))
                .build();
        boardRepository.save(board);
    }

    public ResponseEntity<?> retrieveBoard() {
        List<Board> boardLists = boardRepository.findAll();
        List<RetrieveBoardResponseDto> getBoardLists = boardLists.stream()
                .map(tmp -> RetrieveBoardResponseDto.builder()
                        .title(tmp.getTitle())
                        .content(tmp.getContent())
                        .build())
                .collect(Collectors.toList());

        return new ResponseEntity<>(getBoardLists,HttpStatus.OK);
    }

    public ResponseEntity<?> retrieveOneBoard(Long id) {
        List<Board> allByUserId = boardRepository.findAllByUserId(id);
        List<RetrieveBoardResponseDto> getBoardLists = allByUserId.stream()
                .map(tmp -> RetrieveBoardResponseDto.builder()
                        .title(tmp.getTitle())
                        .content(tmp.getContent())
                        .build())
                .collect(Collectors.toList());
        return new ResponseEntity<>(getBoardLists,HttpStatus.OK);
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
}
