package database;


import springboot.File;
import springboot.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Database {
    String databaseName = "hadoop";

    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    public Database() {}

    public boolean isUserExist(String userName) throws SQLException {
        String sql = "select * from user where name = ?";
        ArrayList result = query(sql, userName);
        return result != null ? true : false;
    }

    public void register(String userName, String password) throws SQLException {
        String sql = "insert into user(name, password) values(?, ?)";
        update(sql, userName, password);
    }

    public boolean isValidUser(User user) throws SQLException {
        String sql = "select * from user where name = ? and password = ?";
        ArrayList result = query(sql, user.getName());
        return result != null ? true : false;
    }

    public ArrayList<File> getFiles(User user) throws SQLException {
        ArrayList<File> target = new ArrayList<>();
        String sql = "select * from file where user_id = ?";
        ArrayList<HashMap<String, String>> files = query(sql, user.getId());
        for (HashMap<String, String> file: files) {
            String id = file.get("id");
            String name = file.get("name");
            File f = new File(id, name);
            target.add(f);
        }
        return target;
    }

    public ArrayList<File> getFiles(ArrayList<String> fileNames, String userName) throws Exception{
        ArrayList<File> files = new ArrayList();
        for (String fileName: fileNames) {
            String sql = "select file.id, file.name from file, user where file.name = ? and user.name = ? and file.user_id = user.id";
            ArrayList result = query(sql, fileName, userName);
            HashMap file = (HashMap)result.get(0);
            String id = (String)file.get("id");
            String name = (String)file.get("name");
            File f = new File(id, name);
            files.add(f);
        }
        return files;
    }

    public File getFile(String fileName, User user) throws Exception{
        ArrayList<File> files = new ArrayList();
        String sql = "select id, name from file where name = ? and user_id = ?";
        String userId = user.getId();
        ArrayList result = query(sql, fileName, userId);
        HashMap file = (HashMap)result.get(0);
        String id = (String)file.get("id");
        String name = (String)file.get("name");
        File f = new File(id, name);
        return f;
    }

    public User getUser(String userName, String password) throws SQLException {
        String sql = "select id, name from user where name = ? and password = ?";
        ArrayList result = query(sql, userName, password);
        boolean wrongUser = (result == null);
        if (wrongUser) {
            return null;
        } else {
            HashMap row = (HashMap)result.get(0);
            String id = (String)row.get("id");
            String name = (String)row.get("name");
            User user = new User(id, name);
            return user;
        }
    }

    public void deleteFile(String fileId) throws SQLException{
        String sql = "delete from file where id = ?;";
        update(sql, fileId);
    }

    public void uploadFile(String path, User user) throws SQLException{
        String sql = "insert into file(name, user_id) values(?, ?);";
        String id = user.getId();
        update(sql, path, id);
    }

    public void uploadFile(String path, String userId) throws SQLException{
        String sql = "insert into file(name, user_id) values(?, ?);";
        update(sql, path, userId);
    }


    private void update(String sql, String... args) throws SQLException {
        Connection con = connect();
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

    ArrayList<HashMap<String, String>> query(String sql, String... args) throws SQLException {
        Connection con = connect();
        PreparedStatement pst = con.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            int index = i + 1;
            pst.setString(index, args[i]);
        }
        try {
            ResultSet rs = pst.executeQuery();
            ArrayList<HashMap<String, String>> result = formatResultSet(rs);
            return result;
        } finally {
            pst.close();
            con.close();
        }
    }

    private ArrayList<HashMap<String, String>> formatResultSet(ResultSet rs) throws SQLException{
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

    private boolean isExistRow(ResultSet rs) throws SQLException {
        if (rs.next()) {
            rs.previous();
            return true;
        }
        return false;
    }

    private Connection connect() {
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
}
