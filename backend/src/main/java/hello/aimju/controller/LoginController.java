package hello.aimju.controller;

import hello.aimju.user.domain.User;
import hello.aimju.login.dto.LoginRequestDto;
import hello.aimju.login.service.LoginService;
import hello.aimju.login.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class LoginController {
    private final LoginService loginService;

    /*
    BindingResult bindingResult: 유효성 검사 후 발생한 바인딩 오류를 포함하는 객체.
    @RequestParam(defaultValue = "/") String redirectURL: 로그인 후 리다이렉트할 URL을 받음. 이 값이 없을 경우 기본적으로 "/"로 리다이렉트.
    HttpServletRequest request: 현재 HTTP 요청에 대한 정보를 담고 있는 객체. 세션을 생성하거나 사용자 정보를 저장하기 위해 사용됨.
    @RequestBody @Valid LoginRequestDto loginRequestDto: 프론트에서 보내줘야함.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto, BindingResult bindingResult,
                                   HttpServletRequest request) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        User loginUser = loginService.login(loginRequestDto.getUserName(), loginRequestDto.getPassword());

        if (loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginUser);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }
}
