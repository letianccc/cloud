package database;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Database {
    String databaseName = "hadoop";

    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    public Database() {}

    public ArrayList getFiles() throws SQLException {
        String sql = "select * from file;";
        ArrayList files = query(sql);
        return files;
    }

    public void uploadFile(String path) throws SQLException{
        String sql = "insert into file(name) values(?);";
        update(sql, path);
    }

    public void deleteFile(String id) throws SQLException{
        String sql = "delete from file where id = ?;";
        update(sql, id);
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

    ArrayList query(String sql, String... args) throws SQLException {
        Connection con = connect();
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

    private ArrayList formatResultSet(ResultSet rs) throws SQLException{
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
