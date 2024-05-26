package hello.aimju.user.dto;

import lombok.Getter;

@Getter
public class UserInfoResponseDto {
    private String userName;
    private String password;

    public UserInfoResponseDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
