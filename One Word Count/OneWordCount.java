package com.ufl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class OneWordCount {

	public static void main(String[] args) throws Exception {

		Job job = Job.getInstance(new Configuration()); //initialize the MaprReduce job with new configuration
		job.setJobName("One Word Count");
		job.setOutputKeyClass(Text.class); //set the type of final output key from reducer
		job.setOutputValueClass(IntWritable.class); //set the type of final output value from reducer

		job.setMapperClass(OneWordMapper.class); //set the Mapper
		job.setReducerClass(SumReducer.class); //set the Reducer

		job.setInputFormatClass(TextInputFormat.class);  // set the type of Input to the Job
		job.setOutputFormatClass(TextOutputFormat.class); //set the type of Output expected from the job

		FileInputFormat.setInputPaths(job, new Path(args[0])); // set the input file path from command-line
		FileOutputFormat.setOutputPath(job, new Path(args[1])); //set the output file path from command-line

		job.setJarByClass(OneWordCount.class); //set the main driver class

		System.exit(job.waitForCompletion(true) ? 0 : 1); //run the job and wait for its completion

	}

}
