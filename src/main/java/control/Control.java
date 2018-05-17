package control;

import database.Database;
import hadoop.Hdfs;
import org.springframework.web.multipart.MultipartFile;
import springboot.File;
import springboot.User;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

public class Control {
    Database database;
    Hdfs hdfs;
    public Control() {
        database = new Database();
        hdfs = new Hdfs();
    }

    public void downloadFile(String filename, String userName) throws Exception {
        hdfs.downloadFile(filename, filename, userName);
    }

    public String uploadFile(MultipartFile file, User user) throws Exception {
        String fileName = getFileName(file, user);
        hdfs.uploadFile(fileName, file, user);
        database.uploadFile(fileName, user);
        return fileName;
    }

    private String getFileName(MultipartFile file, User user) throws Exception {
        String fileName = file.getOriginalFilename();
        boolean isExist = database.isExistFile(fileName, user);
        if (!isExist) {
            return fileName;
        } else {
            String name = getUnuseCopyName(fileName, user);
            return name;
        }
    }

    private String getUnuseCopyName(String fileName, User user) throws Exception {
        ArrayList<String> copyNames = database.getCopyNames(fileName, user);
        String id = getUnuseId(copyNames);
        String name = fileName + "副本" + id;
        return name;
    }

    private String getUnuseId(ArrayList<String> copyNames) {
        if (copyNames == null) {
            return "1";
        } else {
            ArrayList<Integer> copyIds = getCopyIds(copyNames);
            String id = findId(copyIds);
            return id;
        }
    }

    private String findId(ArrayList<Integer> copyIds) {
        Integer target = copyIds.get(copyIds.size() - 1) + 1;
        for (int i = 1; i <= copyIds.size(); i++) {
            int id = copyIds.get(i - 1);
            if (id != i) {
                target = i;
                break;
            }
        }
        return Integer.toString(target);
    }

    private ArrayList<Integer> getCopyIds(ArrayList<String> fileNames) {
        ArrayList<Integer> target = new ArrayList<>();
        for(String name: fileNames) {
            Integer copyId = getPostfix(name);
            target.add(copyId);
        }
        Collections.sort(target);
        return target;
    }

    private Integer getPostfix(String fileName) {
        int index = fileName.lastIndexOf("副本");
        String postfix = fileName.substring(index + 2);
        return Integer.valueOf(postfix);
    }

    public void deleteFile(File file) throws Exception {
        hdfs.deleteFile(file.getName(), file.getUser());
        database.deleteFile(file.getId());
    }

    public ArrayList<File> getFiles(User user) throws SQLException {
        return database.getFiles(user);
    }

    public File getFile(String fileName, User user) throws Exception{
        return database.getFile(fileName, user);
    }

    public boolean isUserExist(String userName) throws SQLException {
        return database.isUserExist(userName);
    }

    public void register(String userName, String password) throws SQLException {
        database.register(userName, password);
    }

    public boolean isValidUser(User user) throws SQLException {
        return database.isValidUser(user);
    }

    public User getUser(String userName, String password) throws SQLException {
        return database.getUser(userName, password);
    }

    public ArrayList<File> search(String searchText, String userName) throws Exception {
        ArrayList<String> fileNames = hdfs.search(searchText, userName);
        ArrayList<File> files = database.getFiles(fileNames, userName);
        return files;
    }




}
