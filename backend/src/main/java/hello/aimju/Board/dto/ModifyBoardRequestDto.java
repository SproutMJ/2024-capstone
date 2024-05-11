package hello.aimju.Board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ModifyBoardRequestDto {
    private Long id;
    private String title;
    private String content;
}
