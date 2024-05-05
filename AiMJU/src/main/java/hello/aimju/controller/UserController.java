package hello.aimju.controller;

import hello.aimju.User.dto.SignupRequestDto;
import hello.aimju.User.dto.UserInfoResponseDto;
import hello.aimju.User.service.UserService;
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
