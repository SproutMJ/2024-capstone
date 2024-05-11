package hello.aimju.Board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ModifyBoardResponseDto {
    private Long id;
    private String title;
    private String content;
}
