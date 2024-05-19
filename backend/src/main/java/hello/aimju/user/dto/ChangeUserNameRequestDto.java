package hello.aimju.user.dto;

import lombok.Getter;

@Getter
public class ChangeUserNameRequestDto {
    String userName;
    String password;
    String newUserName;
}
