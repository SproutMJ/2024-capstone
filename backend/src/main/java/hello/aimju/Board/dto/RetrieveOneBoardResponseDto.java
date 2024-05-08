package hello.aimju.Board.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class RetrieveOneBoardResponseDto {
    private LocalDate createdTime;
    private String author;
    private String title;
    private String content;
}
