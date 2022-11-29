module load hdfs
export HADOOP_CLASSPATH=$(hadoop classpath)
cd /data0/kunal2/newfolder
javac -classpath ${HADOOP_CLASSPATH} -d ./classes_folder ./SubstringFinder.java
jar -cvf sf.jar -C ./classes_folder .
hadoop jar sf.jar SubstringFinder /dsproject/input.txt $1
hadoop fs -cat $1
