# Substring finder using Map-Reduce Framework

## Running the code

### Load the modules
```bash
module load hdfs
export HADOOP_CLASSPATH=$(hadoop classpath)
```

### Add files to hdfs
```bash
hadoop fs -copyFromLocal <input file> <hadoop directory>
```

### Compile and create jar
```bash
javac -classpath ${HADOOP_CLASSPATH} -d ./classes_folder ./SubstringFinder.java
jar -cvf sf.jar -C ./classes_folder
```

### Launch map reduce
```bash
hadoop jar sf.jar SubstringFinder -D mapred.reducer.tasks=<number of reducers> <hadoop path to input> <hadoop path to output> -numSegments <number of segments of t in input>
```
