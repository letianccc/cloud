package util;

import control.Control;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import springboot.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TestUtil {
    public static void createLocalFile(String path, String content) throws IOException {
        try {
            File f = new File(path);
            FileOutputStream out = new FileOutputStream(f);
            out.write(content.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileSystem getHadoopFileSystem(String userName) throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        String rootPath = "hdfs://localhost:9000";
        URI uri = new URI(rootPath);
        FileSystem hdfs = FileSystem.get(uri, conf, userName);
        return hdfs;
    }

    public static void clearLocal(String path) {
        new File(path).delete();
    }

    public static void clearHdfs(String path, String userName) {
        try {
            FileSystem hdfs = getHadoopFileSystem(userName);

            Path p = new Path(path);
            if (hdfs.exists(p)) {
                hdfs.delete(p, false);
            }
            hdfs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void initData(String databaseName) throws Exception {
        String text = "this is file";
        String filename = "file";
        String userName = "test";

        for (int id = 1; id < 10; id++) {
            String path = filename + String.valueOf(id);
            clearLocal(path);
        }

        String tableName = "file";
        clearTable(databaseName, tableName);

        for (int id = 1; id < 10; id++) {
            String path = filename + String.valueOf(id);
            String content = text + String.valueOf(id) + "\n";
            createLocalFile(path, content);
        }

        for (int id = 1; id < 10; id++) {
            String path = filename + String.valueOf(id);
            Path p = new Path(path);
            FileSystem hdfs = getHadoopFileSystem(userName);
            hdfs.copyFromLocalFile(p, p);
            hdfs.close();
            String sql = "insert into file(name, user_id) values(?, ?)";
            TestUtil.update(databaseName, sql, path, "1");
        }

        for (int id = 1; id < 10; id++) {
            String path = filename + String.valueOf(id);
            clearLocal(path);
        }

    }

    public static void clearTable(String databaseName, String tableName) throws Exception {
        Connection con = connect(databaseName);
        String sql = "truncate table " + tableName;
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
        con.close();
    }


    public static void update(String databaseName, String sql, String... args) throws SQLException {
        Connection con = connect(databaseName);
        PreparedStatement pst = con.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            int index = i + 1;
            pst.setString(index, args[i]);
        }
        try {
            pst.executeUpdate();
        } finally {
            pst.close();
            con.close();
        }
    }

    public static ArrayList query(String databaseName, String sql, String... args) throws SQLException {
        Connection con = connect(databaseName);
        PreparedStatement pst = con.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            int index = i + 1;
            pst.setString(index, args[i]);
        }
        try {
            ResultSet rs = pst.executeQuery();
            ArrayList result = formatResultSet(rs);
            return result;
        } finally {
            pst.close();
            con.close();
        }
    }

    private static ArrayList formatResultSet(ResultSet rs) throws SQLException{
        if (!isExistRow(rs)) {
            return null;
        }
        ResultSetMetaData md = rs.getMetaData();
        int rowSize = rs.getFetchSize();
        int colSize = md.getColumnCount();
        ArrayList resultSet = new ArrayList(rowSize);
        while (rs.next()) {
            HashMap<String, String> row = new HashMap(colSize);
            for(int i = 1; i <= colSize; i++){
                String name = md.getColumnName(i);
                String value = rs.getString(i);
                row.put(name, value);
            }
            resultSet.add(row);
        }
        return resultSet;
    }

    private static boolean isExistRow(ResultSet rs) throws SQLException {
        if (rs.next()) {
            rs.previous();
            return true;
        }
        return false;
    }

    private static Connection connect(String databaseName) {
        try {
            String root = "jdbc:mysql://localhost:3306/";
            String conf = "useSSL=false";
            String DB_URL = String.format("%s%s?%s", root, databaseName, conf);
            Class.forName("com.mysql.jdbc.Driver");
            String name = "latin";
            String password = "123456";
            Connection con = DriverManager.getConnection(DB_URL, name, password);
            return con;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isUserEqual(User u1, User u2) {
        if (u1.getId().equals(u2.getId())) {
            if (u1.getName().equals(u2.getName())) {
                return true;
            }
        }
        return false;
    }

    public static void clearUsers() {
        
    }


}
