import java.io.IOException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.GenericOptionsParser;

public class SubstringFinder {
  private final static int segmentsOfT = 2;
  
  public static class SubstringMapper extends Mapper <Object, Text, IntWritable, IntWritable> { 
    private IntWritable k = new IntWritable();
    private IntWritable v = new IntWritable();

    @Override
    public void map(Object key, Text value, Context context)
      throws IOException, InterruptedException {
      String line = value.toString();
      String[] vals = line.split("\\s+");
      // vals[0] --> substring from S
      // vals[1] --> substring from T
      // vals[2] --> start index of S
      // vals[3] --> start index of T
      String s = vals[0];
      String t = vals[1];
      int startS = Integer.parseInt(vals[2]);
      int startT = Integer.parseInt(vals[3]);
      
      int M = t.length();
      int N = s.length();

      int[] F = new int[M + 1];
      F[0] = F[1] = 0;
      for(int i = 2; i <= M; i++) {
        System.out.println("Preprocessing -- " + i);
        int j = F[i - 1];
        while(true) {
          if(t.charAt(j) == t.charAt(i - 1)) {
            F[i] = j + 1;
            break;
          }
          if(j == 0) {
            F[i] = 0;
            break;
          }
          j = F[j];
        }
      }

      int i = 0;
      int j = 0;
      while(true) {
        if(j == N) break;

        if(s.charAt(j) == t.charAt(i)) {
          i++;
          j++;
          if(i == M) {
            k.set(startS + i - startT);
            v.set(startT);
            context.write(k, v);
            i = 0;
          }         
        } else if(i > 0) {
          i = F[i];
        } else {
          j++;
        }

        System.out.println("KMP -- " + i + " " + j);
      }

      // do KMP in future, doing n^2 right now
      // int sizeS = s.length();
      // int sizeT = t.length();
      // for(int i = 0; i + sizeT < sizeS; i++) {
      //   int isSubstring = 1;
      //   for(int j = 0; j < sizeT; j++) {
      //     if(s.charAt(i + j) != t.charAt(j)) {
      //       isSubstring = 0;
      //       break;
      //     }
      //   }
      //   if(isSubstring == 1) {
      //     k.set(startS + i - startT);
      //     v.set(startT);
      //     context.write(k, v);
      //     System.out.println("Mapper -- " + k.get() + ": " + v.get());
      //   }
      // }
    }
  }

  public static class SubstringReducer extends Reducer <IntWritable, IntWritable, IntWritable, IntWritable> {
    
    private IntWritable one = new IntWritable(1);
    
    @Override
    public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
      int len = 0;
      for(IntWritable val : values) {
        len++;
      }
      System.out.println("Reducer -- " + key.get() + ": " + len);
      if(len == segmentsOfT) {
        context.write(key, one);
      }
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    GenericOptionsParser optionParser = new GenericOptionsParser(conf, args);

    String[] remainingArgs = optionParser.getRemainingArgs();
    if((remainingArgs.length != 2) && ( remainingArgs.length != 4 )) {
      System.err.println("Usage: substringfinder <in> <out>");
      System.exit(2);
    }

    Job job = Job.getInstance(conf, "substring finder");
    job.setJarByClass(SubstringFinder.class);
    job.setMapperClass(SubstringMapper.class);
    job.setReducerClass(SubstringReducer.class);
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);

    List<String> otherArgs = new ArrayList<String>();
    for(int i = 0; i < remainingArgs.length; i++) {
      otherArgs.add(remainingArgs[i]);
    }

    FileInputFormat.addInputPath(job, new Path(otherArgs.get(0)));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs.get(1)));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
