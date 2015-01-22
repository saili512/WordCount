package com.ufl;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class DoubleWordMapper extends Mapper<Object, Text, Text, IntWritable> {

	private Text word = new Text();
	private final static IntWritable one = new IntWritable(1);

	@Override
	public void map(Object key, Text value, Context contex) throws IOException,
			InterruptedException {
		// Break line into words for processing
		StringTokenizer wordList = new StringTokenizer(value.toString());
		String prevToken = null;
		if (wordList.hasMoreTokens()) {
			prevToken = wordList.nextToken(); // save the previous word in the line
		}
		String currentToken = null;
		while (wordList.hasMoreTokens()) {
			currentToken = wordList.nextToken();
			word.set(prevToken + " " + currentToken); // key would be 'previousWord currentWord'
			contex.write(word, one);
			prevToken = currentToken; // assign current word as previous word for next word
		}
	}
}
