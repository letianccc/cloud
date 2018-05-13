package control;

import database.Database;
import hadoop.Hdfs;
import org.springframework.web.multipart.MultipartFile;
import springboot.File;
import springboot.User;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public void uploadFile(MultipartFile file, User user) throws Exception {
        String fileName = file.getOriginalFilename();
        hdfs.uploadFile(file, user);
        database.uploadFile(fileName, user);
        // File f = database.getFile(fileName, user);
        // return f;
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
