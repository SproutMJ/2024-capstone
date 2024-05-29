package hello.aimju.Board.service;

import hello.aimju.Board.domain.Board;
import hello.aimju.Board.dto.*;
import hello.aimju.Board.repository.BoardRepository;
import hello.aimju.Comment.domain.Comment;
import hello.aimju.login.session.SessionConst;
import hello.aimju.user.domain.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    public void writeBoard(WriteBoardRequestDto writeBoardRequestDto, HttpSession session) {
        LocalDate now = LocalDate.now();
        Board board = Board.builder()
                .title(writeBoardRequestDto.getTitle())
                .createdTime(now)
                .content(writeBoardRequestDto.getContent())
                .user(getUserFromSession(session))
                .build();
        boardRepository.save(board);
    }

    public ResponseEntity<RetrieveBoardListResponseDto> retrieveBoard(int page, int size) {
        Pageable pageable;
        pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Board> boardLists = boardRepository.findAll(pageable);
        List<RetrieveBoardResponseDto> getBoardLists = boardLists.stream()
                .map(tmp -> RetrieveBoardResponseDto.builder()
                        .id(tmp.getId())
                        .title(tmp.getTitle())
                        .createdTime(tmp.getCreatedTime())
                        .commentNum((long) tmp.getComments().size())
                        .username(tmp.getUser().getUserName())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new RetrieveBoardListResponseDto((long) boardLists.getTotalPages(), getBoardLists));
    }

    public ResponseEntity<RetrieveBoardListResponseDto>  retrieveSearchBoard(int page, int size, String searchKeyword) {
        Pageable pageable;
        pageable = PageRequest.of(page, size, Sort.by("id").descending());
        Page<Board> boardLists = boardRepository.findByTitleContaining(searchKeyword,pageable);
        List<RetrieveBoardResponseDto> getBoardLists = boardLists.stream()
                .map(tmp -> RetrieveBoardResponseDto.builder()
                        .id(tmp.getId())
                        .title(tmp.getTitle())
                        .createdTime(tmp.getCreatedTime())
                        .commentNum((long) tmp.getComments().size())
                        .username(tmp.getUser().getUserName())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new RetrieveBoardListResponseDto((long) boardLists.getTotalPages(), getBoardLists));
    }

    public ResponseEntity<RetrieveBoardListResponseDto> retrieveCurrentUserBoard(int page, int size, HttpSession session) {
        Pageable pageable;
        pageable = PageRequest.of(page, size, Sort.by("id").descending());
        User user = getUserFromSession(session);
        Page<Board> boardLists = boardRepository.findAllByUserId(user.getId(), pageable);
        List<RetrieveBoardResponseDto> getBoardLists = boardLists.stream()
                .map(tmp -> RetrieveBoardResponseDto.builder()
                        .id(tmp.getId())
                        .title(tmp.getTitle())
                        .createdTime(tmp.getCreatedTime())
                        .commentNum((long) tmp.getComments().size())
                        .username(tmp.getUser().getUserName())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new RetrieveBoardListResponseDto((long) boardLists.getTotalPages(), getBoardLists));
    }

    public ResponseEntity<RetrieveBoardListResponseDto>  retrieveCurrentUserSearchBoard(int page, int size, HttpSession session, String searchKeyword) {
        Pageable pageable;
        pageable = PageRequest.of(page, size, Sort.by("id").descending());
        User user = getUserFromSession(session);
        Page<Board> boardLists = boardRepository.findByUserIdAndTitleContaining(user.getId(), searchKeyword, pageable);
        List<RetrieveBoardResponseDto> getBoardLists = boardLists.stream()
                .map(tmp -> RetrieveBoardResponseDto.builder()
                        .id(tmp.getId())
                        .title(tmp.getTitle())
                        .createdTime(tmp.getCreatedTime())
                        .commentNum((long) tmp.getComments().size())
                        .username(tmp.getUser().getUserName())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new RetrieveBoardListResponseDto((long) boardLists.getTotalPages(), getBoardLists));
    }

    public ResponseEntity<?> retrieveOneBoard(Long id) {
        Board findBoard = boardRepository.findById(id).get();
        RetrieveOneBoardResponseDto board = RetrieveOneBoardResponseDto.builder()
                .id(id)
                .author(findBoard.getUser().getUserName())
                .title(findBoard.getTitle())
                .createdTime(findBoard.getCreatedTime())
                .content(findBoard.getContent())
                .build();
        return new ResponseEntity<>(board,HttpStatus.OK);
    }
    public ResponseEntity<?> modifyBoard(ModifyBoardRequestDto modifyBoardRequestDto, HttpSession session) throws Exception {
        System.out.println("modifyBoardRequestDto.getId() = " + modifyBoardRequestDto.getId());
        Board board = boardRepository.findById(modifyBoardRequestDto.getId()).get();
        checkUser(getUserFromSession(session),board.getUser());
        board.update(modifyBoardRequestDto.getTitle(),modifyBoardRequestDto.getContent());
        ModifyBoardResponseDto resBoard = ModifyBoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .build();
        boardRepository.save(board);
        return new ResponseEntity<>(resBoard,HttpStatus.OK);
    }

    public void deleteBoard(Long boardId, HttpSession session) throws Exception {
        Board board = boardRepository.findById(boardId).get();
        checkUser(getUserFromSession(session),board.getUser());
        boardRepository.deleteById(boardId);
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
