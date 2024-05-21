package hello.aimju.Board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class RetrieveBoardResponseDto {
    private Long id;
    private String title;
    private LocalDate createdTime;
    private Long commentNum;
    private String username;
}
