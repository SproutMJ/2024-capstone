package hello.aimju.Comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ModifyCommentResponseDto {
    private Long id;
    private String content;
}
