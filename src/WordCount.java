import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class WordCount {
	
	
	
	public static class Map extends Mapper<LongWritable,Text,Text,IntWritable> {
		
		Text k = new Text();
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			String val = value.toString();			
			
			if(val.length()>0) {

			String[] ratings=val.split("\t");
			
				k.set(ratings[2]);
				context.write(k, new IntWritable(1));
			}
			
			
		}
		
		
		
	}
	
	
	public static class Reduce extends Reducer<Text,IntWritable,Text,IntWritable>{

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context)
				throws IOException,InterruptedException {
			int sum=0;
			// TODO Auto-generated method stub
			for(IntWritable x: values)
			{
				sum+=x.get();
			}
			context.write(key, new IntWritable(sum));
			
		}
		
	}
	

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Job job=new Job();
		job.setJarByClass(WordCount.class);
		job.setJobName("wordcount Demo");
		
		Path in_path=new Path(args[0]);
		Path out_path=new Path(args[1]);
		FileInputFormat.addInputPath(job, in_path);
		FileOutputFormat.setOutputPath(job, out_path);
		
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		System.exit(job.waitForCompletion(true) ? 0 :1);
		
		

	}

}
