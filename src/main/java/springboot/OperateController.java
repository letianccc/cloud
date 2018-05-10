package springboot;

import control.Control;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@Controller
public class OperateController {

    Control control;

    public OperateController() {
        control = new Control();
    }

    @RequestMapping(value="/home", method= RequestMethod.GET, params="action=delete")
    public String deleteFile(Model model, @RequestParam(value="id") String id, @RequestParam(value="name") String name) throws Exception {
        control.deleteFile(id, name);
        return "redirect:/home";
    }

    @RequestMapping(value="/home", method= RequestMethod.GET, params="action=download")
    public String downloadFile(Model model, @RequestParam(value="id") String id, @RequestParam(value="name") String name) throws Exception {
        control.downloadFile(name);
        return "redirect:/home";
    }
}
