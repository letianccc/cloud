package springboot;

import control.Control;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

import static springboot.Util.log;


@Controller
public class LoginController {

    Control control;

    public LoginController() {
        control = new Control();
    }

    // @RequestMapping(value="/login", method= RequestMethod.GET)
    // public String register(
    // @RequestParam(value="userName") String name,
    // @RequestParam(value="password") String password, HttpSession session) throws Exception{
    //     User user = control.getUser(name, password);
    //     if (isUserValid(user)) {
    //         session.setAttribute("user", user);
    //         return "redirect:userPage";
    //     }
    //     return "index.html";
    // }

    @GetMapping("/loginCheck")
    public String login(
            @RequestParam(value="userName") String name,
            @RequestParam(value="password") String password,
            HttpSession session) throws Exception{
        User user = control.getUser(name, password);
        if (isUserValid(user)) {
            session.setAttribute("user", user);
            return "redirect:userPage";
        }
        return "index.html";
    }

    @GetMapping("/loginPage")
    public String login() throws Exception{
        return "login.html";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.removeAttribute("files");
        return "redirect:loginPage";
    }

    private boolean isUserValid(User user) {
        return user != null;
    }

}
