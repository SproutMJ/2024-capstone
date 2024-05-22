package hello.aimju.Comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WriteCommentRequestDto {
    private Long boardId;
    private String content;
}
