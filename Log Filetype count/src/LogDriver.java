import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class LogDriver
{
	
	public static int main(String[] args) throws Exception
	{
		
		if(args.length!=2)
		{
			System.out.println("Usage:<in><out>");
			System.exit(-1);
		}
		
		Job job = new Job();
	    job.setJarByClass(LogDriver.class);
	    job.setJobName("Image Counter");
	    
	    FileInputFormat.setInputPaths(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));
	    
	    job.setMapperClass(LogMapper.class);
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(IntWritable.class);
	    job.setNumReduceTasks(0);
	    
	    boolean success = job.waitForCompletion(true);
	    if(success)
	    {
	    	long gif = job.getCounters().findCounter("ImageCounter","gif").getValue();
	    	long jpeg = job.getCounters().findCounter("ImageCounter","jpeg").getValue();
	    	long other = job.getCounters().findCounter("ImageCounter","other").getValue();
	    	System.out.println("Number of gif: "+gif);
	    	System.out.println("Number of jpeg: "+jpeg);
	    	System.out.println("Number of other: "+other);
	    	return 0;	
	    }
	    else
	    	return 1;
	}
}