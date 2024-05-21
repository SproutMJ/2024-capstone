package hello.aimju.Board.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public final class RetrieveBoardListResponseDto {
    final Long totalPages;
    final List<RetrieveBoardResponseDto> boardLists;
}
