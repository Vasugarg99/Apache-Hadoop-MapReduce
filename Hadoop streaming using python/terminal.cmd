hdfs dfs -put /home/cloudera/Documents/word.txt /user/cloudera

cd Documents

hadoop jar /usr/lib/hadoop-mapreduce/hadoop-streaming.jar -file mapper.py -mapper mapper.py -file reducer.py -reducer reducer.py -input word.txt -output hstreaming-output

hdfs dfs -cat hstreaming-output/p*