package hello.aimju.Board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetrieveBoardResponseDto {
    private Long id;
    private String title;
    private String content;
}