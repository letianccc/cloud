package database;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.TestDatabase;
import database.Database;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;
import static springboot.Util.log;

public class DatabaseTest {
    String databaseName = "test_hadoop";
    TestDatabase tdb = new TestDatabase(databaseName);
    Database db = new Database(databaseName);

    @Before
    public void setup() throws Exception {
        tdb.clear();
    }

    @Test
    public void getFiles() {
        try {
            tdb.init();
            ArrayList files = db.getFiles();
            assert files.size() == 1;
            HashMap file = (HashMap)files.get(0);
            assert file.get("id").equals("1");
            assert file.get("name").equals("file.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void uploadFile() {
        try {
            String path = "file.txt";
            String id = "1";
            db.uploadFile(path);
            String result = tdb.query(id);
            assert path.equals(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteFile() {
        try {
            tdb.init();
            String path = "file.txt";
            String id = "1";
            String result = tdb.query(id);
            assert path.equals(result);
            db.deleteFile(id);
            result = tdb.query(id);
            assert result == null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
