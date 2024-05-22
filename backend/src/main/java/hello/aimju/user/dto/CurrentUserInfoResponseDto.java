package hello.aimju.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentUserInfoResponseDto {
    Long id;
    String userName;
}
