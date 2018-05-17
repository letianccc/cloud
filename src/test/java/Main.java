import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import util.TestDatabase;
import util.TestUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static springboot.Util.log;

public class Main {
    public static void main(String[] args) throws Exception {
         String databaseName = "hadoop";
         TestUtil.initData(databaseName);
//
//        String in = "hdfs://localhost:9000/user/test";
//        String out = "hdfs://localhost:9000/user/test/search_output";
////        Search.init(in, out);

    }

    //
    //
    // class Search {
    //
    //   public static class TestMap
    //        extends Mapper<Object, Text, Text, Text> {
    //
    //       public void map(Object key, Text value, Context context
    //                     ) throws IOException, InterruptedException {
    //       // StringTokenizer itr = new StringTokenizer(value.toString());
    //       // while (itr.hasMoreTokens()) {
    //       //   word.set(itr.nextToken());
    //       //   context.write(word, one);
    //       // }
    //       String filePath = ((FileSplit) context.getInputSplit()).getPath().toString();
    //       log(filePath, value.toString());
    //       context.write(new Text(filePath), value);
    //     }
    //   }
    //
    //   public static class TestReduce
    //        extends Reducer<Text,Text,Text,Text> {
    //
    //
    //     public void reduce(Text key, Iterable<Text> values,
    //                        Context context
    //                        ) throws IOException, InterruptedException {
    //         Configuration conf = context.getConfiguration();
    //         String targetText = conf.get("targetText");
    //       for (Text text : values) {
    //           String content = text.toString().toLowerCase();
    //           if(content.contains(targetText)) {
    //               context.write(key, text);
    //           }
    //       }
    //     }
    //   }
    //
    //   public static void init(String input, String output) throws Exception {
    //     Configuration conf = new Configuration();
    //     conf.set("targetText", "test9.txt");
    //     Job job = Job.getInstance(conf, "word count");
    //     job.setJarByClass(Search.class);
    //     job.setMapperClass(TestMap.class);
    //     job.setCombinerClass(TestReduce.class);
    //     job.setReducerClass(TestReduce.class);
    //     job.setOutputKeyClass(Text.class);
    //     job.setOutputValueClass(Text.class);
    //     FileInputFormat.addInputPath(job, new Path(input));
    //       FileInputFormat.setInputDirRecursive(job, true);
    //     FileOutputFormat.setOutputPath(job, new Path(output));
    //     System.exit(job.waitForCompletion(true) ? 0 : 1);
    //   }
    // }

}
