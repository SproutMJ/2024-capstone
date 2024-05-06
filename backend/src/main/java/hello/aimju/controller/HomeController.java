package hello.aimju.controller;

import hello.aimju.User.domain.User;
import hello.aimju.login.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {
    /*
    * robo 페이지 보여줌
    * */
    @GetMapping("/view/robo")
    public String robo(){
        return "robo";
    }

    @GetMapping("/")
    public String home(@Login User loginUser, Model model) {

        //세션에 회원 데이터가 없으면 home
        if (loginUser == null) {
            return "home";
        }

        //세션이 유지되면 화면으로 이동
        model.addAttribute("member", loginUser);
        return "robo";
    }

    @GetMapping("/api/signup")
    public String addForm(@ModelAttribute("member")User user) {
        return "members/addMemberForm";
    }

    @GetMapping("/api/login")
    public String loginForm() {
        return "login/loginForm";
    }

}
