


package util;

import java.sql.*;

import static springboot.Util.log;

public class TestDatabase {
    String databaseName;

    public String query(String id) throws Exception {
        Connection con = connect();
        String sql = "select name from file where id = ? ;";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, id);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            String name = rs.getString("name");
            return name;
        }
        return null;

    }

    public TestDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    public void init() throws Exception {
        String path = "file.txt";
        String userId = "1";
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

    public void clear() throws Exception {
        String tableName = "file";

        TestUtil.clearTable(databaseName, tableName);
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
