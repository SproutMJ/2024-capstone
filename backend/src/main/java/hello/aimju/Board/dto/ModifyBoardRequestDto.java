package hello.aimju.Board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ModifyBoardRequestDto {
    private Long id;
    private String title;
    private String content;
}
