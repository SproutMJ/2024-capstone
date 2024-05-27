package hello.aimju.Board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModifyBoardResponseDto {
    private Long id;
    private String title;
    private String content;
}
