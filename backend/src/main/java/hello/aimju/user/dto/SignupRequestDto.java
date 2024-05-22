package hello.aimju.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @NotBlank
//    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "아이디는 소문자 및 숫자로 구성된 4~10자의 문자열이어야 합니다.")
    private String userName;
    @NotBlank
//    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$", message = "비밀번호는 대소문자, 숫자로만 구성된 8~15자의 문자열이어야 합니다.")
    private String password;
}
