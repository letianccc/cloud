package control;

import database.Database;
import hadoop.Hdfs;

import java.sql.SQLException;
import java.util.ArrayList;

public class Control {
    Database database;
    Hdfs hdfs;
    public Control() {
        database = new Database();
        hdfs = new Hdfs();
    }

    public void downloadFile(String filename) throws Exception {
        hdfs.downloadFile(filename, filename);
    }

    public void uploadFile(String path) throws Exception {
        hdfs.uploadFile(path);
        database.uploadFile(path);
    }

    public void deleteFile(String id, String name) throws Exception {
        hdfs.deleteFile(name);
        database.deleteFile(id);
    }

    public ArrayList getFiles() throws SQLException {
        return database.getFiles();
    }


}
