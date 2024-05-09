package hello.aimju.Comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetrieveCommentsResponseDto {
    private String name;
    private String content;
}
