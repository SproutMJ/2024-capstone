package hello.aimju.Board.domain;

import hello.aimju.User.domain.User;

import java.time.LocalDateTime;

public class Board {
    private Long id;
    private User userId;
    private String title;
    private LocalDateTime created_Time = LocalDateTime.now();
    private String content;

}
