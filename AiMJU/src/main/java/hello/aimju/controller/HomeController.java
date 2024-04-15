package hello.aimju.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(value = "/view")
public class HomeController {
    /*
    * robo 페이지 보여줌
    * */
    @GetMapping("/robo")
    public String robo(){
        return "robo";
    }

}
