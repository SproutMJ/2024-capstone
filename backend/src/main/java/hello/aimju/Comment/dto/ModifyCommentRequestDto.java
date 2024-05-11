package hello.aimju.Comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifyCommentRequestDto {
    private Long id;
    private String content;
}
