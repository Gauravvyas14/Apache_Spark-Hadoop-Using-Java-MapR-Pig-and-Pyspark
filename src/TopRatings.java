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




public class TopRatings {

	public static class Map extends Mapper<LongWritable,Text,Text,IntWritable> {
		
		Text k=new Text();
		IntWritable v=new IntWritable();
		
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			String val=value.toString();
			
			if(val.length()>0) {
				
				String[] ret=val.split("\t");
				k.set(ret[1]);
				v.set(Integer.parseInt(ret[2]));
				context.write(k,  v);
				
			}
		}
		
	}
	public static class Reduce extends Reducer<Text, IntWritable,Text,IntWritable> {
		
		
		
		
		public void reduce(Text key, Iterable <IntWritable> value, Context context) throws IOException, InterruptedException {
			
			int sum=0;
			
			for(IntWritable i: value) {
				
				sum=sum+i.get();
				
			}
			context.write(key, new IntWritable(sum));
		}
		
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Job job=new Job();
		job.setJarByClass(TopRatings.class);
		job.setJobName("Top Movies");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job,new Path(args[1]));
		
		System.exit(job.waitForCompletion(true) ? 0 :1);

	}

}
