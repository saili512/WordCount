package com.ufl;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class WordCount extends Configured implements Tool {

	public int run(String[] args) throws Exception {
		Job job = Job.getInstance(getConf());

		List<String> otherArgs = new ArrayList<String>();

		for (int i = 0; i < args.length; i++) {
			System.out.println(args[i]);
			if ("-searchList".equals(args[i])) {
				String cacheFile = args[++i]; //read the filename given as argument following keyword '-searchList'
				job.addCacheFile(new Path(cacheFile).toUri()); //add given file to distributed cache
			} else {
				otherArgs.add(args[i]); //store input and output path arguments
			}
			System.out.println("OtherArgs :" + otherArgs);
		}

		job.setJobName("Saili's Word Count");
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setMapperClass(WordMapper.class);
		job.setReducerClass(SumReducer.class);

		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.setInputPaths(job, new Path(otherArgs.get(0)));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs.get(1)));

		job.setJarByClass(WordCount.class);
		return job.waitForCompletion(true) ? 0 : 1;

	}

	public static void main(String[] args) throws Exception {
		int res = ToolRunner.run(new Configuration(), new WordCount(), args); //Use tools interface to handle custom arguments
		System.exit(res);

	}
}
