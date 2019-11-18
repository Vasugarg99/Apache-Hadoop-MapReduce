import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WCPDriver extends Configured implements Tool 
{
    public int run(String[] args) throws Exception 
    {
        if (args.length != 2) 
        {
			System.out.println("Usage: [input] [output]");
			System.exit(-1);
		}

		Job job = Job.getInstance(getConf());
		job.setJobName("wordcount");
		job.setJarByClass(WCPDriver.class);
		job.setNumReduceTasks(26);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(WCPMapper.class);
		job.setPartitionerClass(WCPartitioner.class);
		job.setCombinerClass(WCPReducer.class);
		job.setReducerClass(WCPReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		Path inputFilePath = new Path(args[0]);
		Path outputFilePath = new Path(args[1]);

		FileInputFormat.addInputPath(job, inputFilePath);
		FileOutputFormat.setOutputPath(job, outputFilePath);

		/*Delete output filepath if already exists*/
		FileSystem fs = FileSystem.newInstance(getConf());

        if (fs.exists(outputFilePath)) 
        {
			fs.delete(outputFilePath, true);
		}

		return job.waitForCompletion(true) ? 0: 1;
}
    public static void main(String[] args) throws Exception 
    {
		WCPDriver wordcountDriver = new WCPDriver();
		int res = ToolRunner.run(wordcountDriver, args);
		System.exit(res);
	}
}