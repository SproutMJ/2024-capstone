package hello.aimju.controller;

import hello.aimju.user.dto.SignupRequestDto;
import hello.aimju.user.dto.UserInfoResponseDto;
import hello.aimju.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @GetMapping("/user-info/{userId}")
    public UserInfoResponseDto getUserInfo(@PathVariable Long userId){
        return userService.getUserInfo(userId);
    }
}
