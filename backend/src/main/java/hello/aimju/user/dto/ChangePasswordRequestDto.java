package hello.aimju.user.dto;

import lombok.Getter;

@Getter
public class ChangePasswordRequestDto {
    String userName;
    String passWord;
    String newPassword;
}
