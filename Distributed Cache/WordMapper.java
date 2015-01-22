package com.ufl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

public class WordMapper extends Mapper<Object, Text, Text, IntWritable> {
	private Text word = new Text();
	private final static IntWritable one = new IntWritable(1);

	List<String> searchWordList = new ArrayList<String>(); // List to store the words to be counted from other list

	/* (non-Javadoc)
	 * @see org.apache.hadoop.mapreduce.Mapper#map(KEYIN, VALUEIN, org.apache.hadoop.mapreduce.Mapper.Context)
	 */
	@Override
	public void map(Object key, Text value, Context contex) throws IOException,
			InterruptedException {
		// Break line into words for processing
		StringTokenizer wordList = new StringTokenizer(value.toString());
		String currentToken = null;
		while (wordList.hasMoreTokens()) {
			currentToken = wordList.nextToken();
			/*if the current word is present in the list of words from small
			file then only generate the <word,1> key/value pair*/
			if (searchWordList.contains(currentToken)) {
				word.set(currentToken);
				contex.write(word, one);
			}
		}
	}

	/**
	 * This method is called before every map task starts and it reads from the
	 * cached file to populate a list of words to be counted in the bigger file.
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected void setup(Mapper<Object, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		Path[] searchListFiles = new Path[2];
		try {
			searchListFiles = context.getLocalCacheFiles(); // get the path on local disk of the task nodes for the cached file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Caught exception while getting cached file : "
					+ StringUtils.stringifyException(e));
		}
		parseSearchFile(searchListFiles[0].toString());
		super.setup(context);

	}

	/**
	 * This method reads the cached file present on every task node and
	 * populates a list of words from that file
	 * 
	 * @param searchFile
	 */
	private void parseSearchFile(String searchFile) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					searchFile));
			String line = null;
			while ((line = reader.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line);
				String token = null;
				while (tokenizer.hasMoreTokens()) {
					token = tokenizer.nextToken();
					searchWordList.add(token);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.err.println("Caught exception while opening cached file : "
					+ StringUtils.stringifyException(e));
		} catch (IOException e) {
			System.err.println("Caught exception while reading cached file : "
					+ StringUtils.stringifyException(e));
		}

	}
}
