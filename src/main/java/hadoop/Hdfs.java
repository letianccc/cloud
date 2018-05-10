
package hadoop;




import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import database.Database;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;


public class Hdfs {
    public void deleteFile(String hdfspath) throws URISyntaxException, FileExit, IOException {
        if (!isExist(hdfspath)) {
            throw new FileExit(hdfspath + " is not exist.");
        }

        FileSystem hdfs = getHadoopFileSystem();
        Path p = new Path(hdfspath);
        hdfs.delete(p, false);

        hdfs.close();
    }

    public void downloadFile(String hdfspath, String localpath) throws URISyntaxException, IOException, FileExit {
        if (!isExist(hdfspath)) {
            throw new FileExit(hdfspath + " is not exist.");
        }

        FileSystem hdfs = getHadoopFileSystem();
        Path p = new Path(hdfspath);
        FileStatus[] files = hdfs.listStatus(p);
        FSDataInputStream hdfsIn = hdfs.open(files[0].getPath());
        File f = new File(localpath);
        FileOutputStream localOut = new FileOutputStream(f);

        byte[] buffer = new byte[1024];
        while (hdfsIn.read(buffer) > 0) {
            int end = getBufferEnd(buffer);
            localOut.write(buffer, 0, end);
        }
        localOut.close();
        hdfsIn.close();
        hdfs.close();

    }

    public void uploadFile(String filepath) throws URISyntaxException, IOException, FileExit, SQLException {
        if (isExist(filepath)) {
            throw new FileExit(filepath + " is exist.");
        }

        FileSystem hdfs = getHadoopFileSystem();
        Path p = new Path(filepath);
        FSDataOutputStream out = hdfs.create(p);

        File f = new File(filepath);
        FileInputStream fis = new FileInputStream(f);
        byte[] buffer = new byte[1024];
        int n = 0;
        while ((n = fis.read(buffer)) > 0) {
            out.write(buffer, 0, n);
        }

        out.close();
        fis.close();
        hdfs.close();
    }

    private int getBufferEnd(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] == 0) {
                return i;
            }
        }
        return buffer.length;
    }

    private FileSystem getHadoopFileSystem() throws URISyntaxException, IOException {
        Configuration conf = new Configuration();
        String rootPath = "hdfs://localhost:9000";
        URI uri = new URI(rootPath);
        FileSystem hdfs = FileSystem.get(uri, conf);
        return hdfs;
    }

    private boolean isExist(String hdfspath) throws URISyntaxException, IOException{
        FileSystem hdfs = getHadoopFileSystem();
        Path p = new Path(hdfspath);
        boolean isExist = hdfs.exists(p);
        hdfs.close();
        return isExist;
    }

}

class FileExit extends Exception {
    public FileExit(String message) {
        super(message);
    }
}
