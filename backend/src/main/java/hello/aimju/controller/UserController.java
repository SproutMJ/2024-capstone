package hello.aimju.controller;

import hello.aimju.user.dto.*;
import hello.aimju.user.service.UserService;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<StatusResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        return userService.signup(signupRequestDto);
    }

    @GetMapping("/current-user")
    public CurrentUserInfoResponseDto getCurrentUserInfo(HttpSession session) {
        return userService.getCurrentUserInfo(session);
    }

    @GetMapping("/user-info/{userId}")
    public UserInfoResponseDto getUserInfo(@PathVariable Long userId){
        return userService.getUserInfo(userId);
    }

    @GetMapping("/user-detail")
    public UserDetailResponseDto getUserDetail(HttpSession session){
        return userService.getUserDetail(session);
    }

    @PutMapping("/user/change-userName")
    public ResponseEntity<?> changeUserName(@RequestBody ChangeUserNameRequestDto requestDto, HttpSession session) {
        return userService.changeUserName(requestDto, session);
    }

    @PutMapping("/user/change-password")
    public ResponseEntity<?> changeUserName(@RequestBody ChangePasswordRequestDto requestDto, HttpSession session) {
        return userService.changePassword(requestDto, session);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestBody SignupRequestDto requestDto, HttpSession session) {
        return userService.deleteUser(requestDto, session);
    }
}
