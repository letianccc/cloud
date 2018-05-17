
package hadoop;




import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.springframework.web.multipart.MultipartFile;
import springboot.User;


public class Hdfs {
    Search search = new Search();

    public void deleteFile(String hdfspath, String userName) throws URISyntaxException, FileExit, IOException , InterruptedException {
        if (!isExist(hdfspath, userName)) {
            throw new FileExit(hdfspath + " is not exist.");
        }

        FileSystem hdfs = getHadoopFileSystem(userName);
        Path p = new Path(hdfspath);
        hdfs.delete(p, true);
        hdfs.close();
    }

    public void downloadFile(String hdfspath, String localpath, String userName) throws URISyntaxException, IOException, FileExit, InterruptedException  {
        if (!isExist(hdfspath, userName )) {
            throw new FileExit(hdfspath + " is not exist.");
        }
        FileSystem hdfs = getHadoopFileSystem(userName);
        Path src = new Path(hdfspath);
        Path dst = new Path(localpath);
        hdfs.copyToLocalFile(src, dst);
        hdfs.close();
    }

    public void uploadFile(String filepath, String user) throws URISyntaxException, IOException, FileExit, InterruptedException  {
        if (isExist(filepath, user)) {
            throw new FileExit(filepath + " is exist.");
        }
        FileSystem hdfs = getHadoopFileSystem(user);
        Path p = new Path(filepath);
        hdfs.copyFromLocalFile(p, p);
        hdfs.close();

    }

    public void uploadFile(String fileName, MultipartFile file, User user) throws URISyntaxException, IOException, FileExit, InterruptedException  {
        FileSystem hdfs = getHadoopFileSystem(user.getName());
        Path p = new Path(fileName);
        FSDataOutputStream dst = hdfs.create(p);
        InputStream src = file.getInputStream();

        byte[] buffer = new byte[1024];
        int n = 0;
        while ((n = src.read(buffer)) > 0) {
            dst.write(buffer, 0, n);
        }

        src.close();
        dst.close();
        hdfs.close();

    }

    private FileSystem getHadoopFileSystem(String user) throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        String rootPath = "hdfs://localhost:9000";
        URI uri = new URI(rootPath);
        FileSystem hdfs = FileSystem.get(uri, conf, user);
        return hdfs;
    }

    private boolean isExist(String hdfspath, String userName) throws URISyntaxException, IOException, InterruptedException {
        FileSystem hdfs = getHadoopFileSystem(userName);
        Path p = new Path(hdfspath);
        boolean isExist = hdfs.exists(p);
        hdfs.close();
        return isExist;
    }

    public ArrayList<String> search(String searchText, String userName) throws Exception {
        String outputPath = search.exec(searchText, userName);
        ArrayList<String> files = getFiles(userName, outputPath);
        // deleteFile(outputPath, userName);
        return files;
    }

    public ArrayList<String> getFiles(String userName, String outputPath) throws Exception {
        ArrayList<String> target = new ArrayList<>();
        FileSystem hdfs = getHadoopFileSystem(userName);
        FileStatus[] statusList = hdfs.listStatus(new Path(outputPath));
        for (FileStatus status : statusList) {
            ArrayList<String> fileNames = scanFile(status, hdfs);
            target.addAll(fileNames);
        }
        hdfs.close();
        return target;
    }

    public ArrayList<String> scanFile(FileStatus status, FileSystem hdfs) throws Exception {
        ArrayList<String> fileNames = new ArrayList<>();
        Path p = status.getPath();
        FSDataInputStream in = hdfs.open(p);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = reader.readLine()) != null) {
           String filePath = line.split("\t")[0];
           String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
           fileNames.add(fileName);
        }
        reader.close();
        in.close();
        return fileNames;
    }

    public InputStream copyInputStream(FSDataInputStream in) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > -1 ) {
            baos.write(buffer, 0, len);
        }
        baos.flush();
        InputStream target = new ByteArrayInputStream(baos.toByteArray());
        return target;
    }

}

class FileExit extends Exception {
    public FileExit(String message) {
        super(message);
    }
}
