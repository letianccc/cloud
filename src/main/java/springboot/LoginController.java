package springboot;

import control.Control;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static springboot.Util.log;


@Controller
public class LoginController {

    Control control;

    public LoginController() {
        control = new Control();
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user) {
        log(user.name, user.password);
        return "index";
    }

    private void isUserValid(User user) {
        
    }

}
