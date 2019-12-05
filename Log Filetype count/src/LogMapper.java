import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class LogMapper extends Mapper<Object, Text, Text, IntWritable>{
	
	@Override
	public void map(Object key, Text value, Context context) throws IOException, InterruptedException
	{
		String line = value.toString();
		int dot = line.indexOf(".",line.indexOf("GET "));
		int end = line.indexOf(" ",dot);
		String resource = line.substring(dot,end).trim();
		if(resource.endsWith("gif"))
			context.getCounter("ImageCounter","gif").increment(1);
		else if(resource.endsWith("jpeg"))
			context.getCounter("ImageCounter","jpeg").increment(1);
		else
			context.getCounter("ImageCounter","other").increment(1);
	}

}