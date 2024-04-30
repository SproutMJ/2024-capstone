package hello.aimju.Comment.domain;

import hello.aimju.User.domain.User;

import java.time.LocalDateTime;

public class Comment {
    private Long id;
    private User userId;
    private LocalDateTime createdComment;
    private String content;
}
