package util;

import control.Control;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class TestUtil {
    public static void initLocalFile(String path, String content) throws IOException {
        try {
            File f = new File(path);
            FileOutputStream out = new FileOutputStream(f);
            out.write(content.getBytes());
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileSystem getHadoopFileSystem() throws URISyntaxException, IOException {
        Configuration conf = new Configuration();
        String rootPath = "hdfs://localhost:9000";
        URI uri = new URI(rootPath);
        FileSystem hdfs = FileSystem.get(uri, conf);
        return hdfs;
    }

    public static void clearLocal(String path) {
        new File(path).delete();
    }

    public static void clearHdfs(String path) {
        try {
            Configuration conf = new Configuration();
            String rootPath = "hdfs://localhost:9000";
            URI uri = new URI(rootPath);
            FileSystem hdfs = FileSystem.get(uri, conf);

            Path p = new Path(path);
            if (hdfs.exists(p)) {
                hdfs.delete(p, false);
            }
            hdfs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void initData() throws Exception {
        Control control = new Control();
        String content = "this is test.txt.";
        String name = "file";
        for (int id = 1; id < 10; id++) {
            String path = name + String.valueOf(id);
            clearLocal(path);
            clearHdfs(path);
            TestUtil.initLocalFile(path, content);
            control.uploadFile(path);
            clearLocal(path);
        }
    }



}
