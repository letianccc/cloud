
import hadoop.Hdfs;
import org.junit.Before;
import org.junit.Test;


import java.io.*;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import util.TestUtil;


public class HdfsTest {
    Hdfs hdfs;

    @Before
    public void setup() throws Exception {
        hdfs = new Hdfs();
        String path = "test.txt";
        TestUtil.clearLocal(path);
        TestUtil.clearHdfs(path);
    }

    @Test
    public void testDelete() throws Exception {
        initHdfs();

        String path = "test.txt";
        hdfs.deleteFile(path);

        FileSystem hdfs = TestUtil.getHadoopFileSystem();
        Path p = new Path(path);
        assert !hdfs.exists(p);

        hdfs.close();
    }

    @Test
    public void testDownload() throws Exception {
        initHdfs();

        String path = "test.txt";
        hdfs.downloadFile(path, path);

        BufferedReader in = new BufferedReader(new FileReader(path));
        String line;
        String content = "this is test.txt.";
        while((line = in.readLine()) != null)
        {
            assert line.equals(content);
        }

        in.close();
    }

    @Test
    public void testUpload() throws Exception {
        String path = "test.txt";
        String content = "this is test.txt.\n";
        TestUtil.initLocalFile(path, content);

        hdfs.uploadFile(path);

        FileSystem hdfs = TestUtil.getHadoopFileSystem();
        FSDataInputStream in = getInputStream(hdfs, path);
        File f = new File(path);
        FileInputStream caseIn = new FileInputStream(f);
        compStream(in, caseIn);

        in.close();
        caseIn.close();
        hdfs.close();
    }

    private void initHdfs() {
        try {
            String path = "test.txt";
            String content = "this is test.txt.";
            TestUtil.initLocalFile(path, content);
            hdfs.uploadFile(path);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void compStream(InputStream in1, InputStream in2) throws IOException {
        byte buffer1[] = new byte[1024];
        byte buffer2[] = new byte[1024];
        int n = 0;
        while((n = in1.read(buffer1)) > 0) {
            in2.read(buffer2);
            for (int i = 0; i < n; i++) {
                if (buffer1[i] != buffer2[i]) {
                    System.out.println(new String(buffer1));
                    System.out.println(new String(buffer2));
                    assert false;
                }
            }
        }
    }

    private FSDataInputStream getInputStream(FileSystem hdfs, String path) throws Exception {
        Path p = new Path(path);
        FileStatus[] files = hdfs.listStatus(p);
        FSDataInputStream in = hdfs.open(files[0].getPath());
        return in;
    }


}
