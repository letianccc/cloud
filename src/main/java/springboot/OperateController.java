package springboot;

import control.Control;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.ArrayList;


@Controller
public class OperateController {

    Control control;
    String user = "test";

    public OperateController() {
        control = new Control();
    }

    @GetMapping(value="/home", params="action=delete")
    public String deleteFile(
            @RequestParam(value="id") String fileId,
            @RequestParam(value="name") String fileName,
            RedirectAttributes attribute,
            HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        File file = new File(fileId, fileName, user.getName());
        control.deleteFile(file);
        updateDeleteSession(fileId, session);
        String message = "删除成功";
        attribute.addFlashAttribute("operateResult", message);
        return "redirect:userPage";
    }

    private void updateDeleteSession(String fileId, HttpSession session) {
        ArrayList<File> files = (ArrayList<File>)session.getAttribute("files");
        for (File f: files) {
            if (f.getId().equals(fileId)) {
                files.remove(f);
                session.setAttribute("files", files);
                return;
            }
        }
    }

    @GetMapping(value="/home", params="action=download")
    public String downloadFile(
            @RequestParam(value="id") String fileId,
            @RequestParam(value="name") String fileName,
            RedirectAttributes attribute,
            HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        control.downloadFile(fileName, user.getName());
        String message = "下载成功";
        attribute.addFlashAttribute("operateResult", message);
        return "redirect:userPage";
    }



    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes attribute,
            HttpSession session) throws Exception {
        if (file.isEmpty()) {
            attribute.addFlashAttribute("operateResult", "请选择文件");
            return "redirect:userPage";
        }
        updateUploadSession(file, session);
        String message = "上传成功";
        attribute.addFlashAttribute("operateResult", message);
        return "redirect:userPage";
    }

    private void updateUploadSession(MultipartFile file, HttpSession session) throws Exception {
        User user = (User)session.getAttribute("user");
        String fileName = control.uploadFile(file, user);
        File f = control.getFile(fileName, user);
        ArrayList<File> files = (ArrayList<File>)session.getAttribute("files");
        files.add(f);
        session.setAttribute("files", files);
    }


    @GetMapping("/search")
    public String search(
            @RequestParam(value="searchText") String searchText,
            RedirectAttributes attribute,
            HttpSession session) throws Exception{
        User user = (User)session.getAttribute("user");
        ArrayList<File> files = control.search(searchText, user.name);
        setFiles(files, session);
        setMessage(files, attribute);
        return "redirect:userPage";
    }

    private void setFiles(ArrayList<File> files, HttpSession session) {
        session.setAttribute("files", files);
    }

    private void setMessage(ArrayList<File> files, RedirectAttributes attribute) {
        String message;
        if (canFind(files)) {
            message = "搜索成功";
        } else {
            message = "不存在符合条件的文件";
        }
        attribute.addFlashAttribute("operateResult", message);
    }

    private boolean canFind(ArrayList<File> files) {
        return files.size() != 0;
    }

}
