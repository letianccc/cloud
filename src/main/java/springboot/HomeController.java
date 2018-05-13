package springboot;

import control.Control;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import static springboot.Util.log;

@Controller
public class HomeController {
    Control control;

    public HomeController() {
        control = new Control();
    }

    @GetMapping("/userPage")
    public String show(HttpSession session) throws Exception {
        if (session.getAttribute("files") == null) {
            User user = (User)session.getAttribute("user");
            ArrayList<File> files = control.getFiles(user);
            session.setAttribute("files", files);
        }
        return "home.html";
    }

    @GetMapping("/flushUserPage")
    public String flushUserPage(HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        ArrayList<File> files = control.getFiles(user);
        session.setAttribute("files", files);
        return "redirect:userPage";
    }

    @GetMapping("/")
    public String showLoginPage(Model model) throws Exception {
        return "redirect:loginPage";
        // return "redirect:registerPage";
        // return "redirect:login?userName=test&password=123";
    }

    @GetMapping("/test")
    public String test(Model model) throws Exception {
        return "redirect:login";
        // return "redirect:login?userName=test&password=123";
    }





}
