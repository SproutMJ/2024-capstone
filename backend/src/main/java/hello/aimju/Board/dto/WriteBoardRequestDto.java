package hello.aimju.Board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WriteBoardRequestDto {
    private String title;
    private String content;
}
