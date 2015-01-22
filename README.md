# WordCount
Different programs which count one-word frequency, one-word frequency using one list as a source with Distributed Cache and double-word frequency in a given file.

# Name of the programs
1. OneWordCount.jar
OneWordCount.java & class #This is the driver class
OneWordMapper.java & class #This is the mapper class
SumReducer.java & class #This is the reducer class

2. DoubleWordCount.jar
DoubleWordCount.java & class #This is the driver class
DoubleWordMapper.java & class #This is the mapper class
SumReducer.java & class #This is the reducer class

3. CacheWordCount.jar
WordCount.java & class #This is the driver class
WordMapper.java & class #This is the mapper class
SumReducer.java & class #This is the reducer class

# Program Description
1. One Word Count - This program takes a list of input files and generates files containing words and their respective counts in the given input files.

OneWordCount.java
This is the driver class.
In the driver class, a Job instance in initialized and is configured. Various facets of the job, such as the input/output paths (passed via the command line), key/value types, input/output formats etc. are configured in the Job Configuration. It then calls the waitForCompletion to submit the job and monitor its progress.

OneWordMapper.java
This class is the mapper class. It receives a key and single line as value in input. The line is then tokenized into words and for each word a key/value pair of the form <word, 1> is set in the Job Context.

SumReducer.java
This class is the reducer class. It receives a key- a single word and a list of counts for that word. It will then sum up the counts to produce the final output key/value pair as <word, count>

2. Double Word Count - This program takes a list of input files and generates files containing double words and their respective counts in the given input files.

DoubleWordCount.java
This program is same as OneWordCount.java which is the driver class for the Double Word Count Job.

DoubleWordMapper.java
This class is the mapper class for Double Word Count Job. It differs from One Word Mapper in that it outputs a key/value pair of the form <previousWord currentWord, 1>.
The previous token is initialized before the while loop to the first word for the line.
For every next word in the while loop, we form a new key – ‘previous token space current token’. And this key is set with the value 1 in the context. The current token now becomes the previous token.

SumReducer.java
This class is similar to One Word Count Job reducer class and it just sums up the counts for the double word pairs.

3. Distributed Cache Word Count

WordCount.java
This is the main class. It implements Tool interface to handle custom arguments to the program. The run method is called by the ToolRunner with the command-line arguments. The run method loops through the commandline arguments to find the location for the small word list file. It then sets this file for distributed caching in the job context by calling addCacheFile method on the job instance. 

WordMapper.java
This class is the mapper class. In the setup method of the Mapper, the location of the cached file in the local disk of the task node is fetched using the method getLocalCacheFiles on the context object. The file is read from this location by the parseSearchFile method and a list of words present in the file is stored in an array.
The map method then uses this list of words to output count of only those words present in this list.

SumReducer.java
This is the reducer class and is similar to the One Word Count Job class.

# Example commands for the jobs on futuregrid
bin/hadoop jar OneWordCount.jar OneWordCount Data/input Output
bin/hadoop jar DoubleWordCount.jar DoubleWordCount Data/input Output
bin/hadoop jar CacheWordCount.jar CacheWordCount –searchList Data/word-patterns.txt Data/input Output

# Output location on AWS
OneWordCount Program – http://uflsaili.s3.amazonaws.com/OWCoutput/OneWordCountOutput.zip
DoubleWordCount Program – 
http://uflsaili.s3.amazonaws.com/DWCoutput/DoubleWordCountOutput.zip
DistributedWordCount Program – 
http://uflsaili.s3.amazonaws.com/DCWCoutput/DistributedCacheWordCount.zip

# Results
1. One Word Count Job – The output of the job is n number of part files where n is the number of reduce tasks. Each file contains a list of <key,value> pairs in sorted order.
The format of the <key,value> pair is <word,count>.
E.g. character 180. The word character appears a total of 180 times in the 10 input files.

2. Double Word Count Job - The output of the job is n number of part files where n is the number of reduce tasks. Each file contains a list of <key,value> pairs in sorted order.
The format of the <key,value> pair is <word1 word2,count>. Thus it gives the count of double word pairs in the input file.
E.g. all with 230. The two words ‘all’and ‘with’ appear together a total of 230 times in the 10 input files.

3. Distributed Cache Word Count Job - The output of the job is n number of part files where n is the number of reduce tasks. Each file contains a list of <key,value> pairs in sorted order.
The format of the <key,value> pair is <word,count>. The output contains only those words which are present in the word-patterns.txt
E.g. behold 14990. The word behold from the word-patterns file appears a total of 14990 times in the 10 input files.
