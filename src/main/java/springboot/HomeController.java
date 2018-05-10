package springboot;

import control.Control;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {
    Control control;

    public HomeController() {
        control = new Control();
    }

    @GetMapping("/home")
    public String showUserFile(Model model) throws SQLException {
        ArrayList files = control.getFiles();
        model.addAttribute("files", files);
        return "home.html";
    }

    @GetMapping("/")
    public String showLoginPage(Model model) throws SQLException {
        model.addAttribute("user", new User());
        return "login.html";
    }
}
