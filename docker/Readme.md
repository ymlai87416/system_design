Refer to: 
https://www.baeldung.com/ops/kafka-docker-setup
https://github.com/big-data-europe/docker-hadoop-spark-workbench/blob/master/hadoop.env

https://medium.com/@chrischuck35/how-to-create-a-mysql-instance-with-docker-compose-1598f3cc1bee

https://www.jianshu.com/p/aaa797181c2d


https://stackoverflow.com/questions/29418966/how-to-submit-spark-job-from-within-java-program-to-standalone-spark-cluster-wit

To start up the environment, run

./bin/spark-submit \
--master yarn \
--deploy-mode cluster \
--conf "spark.sql.shuffle.partitions=20000" \
--jars "dependency1.jar,dependency2.jar"
--class com.sparkbyexamples.WordCountExample \
spark-by-examples.jar 


./bin/spark-submit --master spark://spark-master:7077 \
--conf spark.delta.logStore.class=org.apache.spark.sql.delta.storage.S3SingleDriverLogStore \
--conf spark.hadoop.fs.s3a.endpoint=http://172.18.0.8:9000 \
--conf spark.hadoop.fs.s3a.access.key=minio \
--conf spark.hadoop.fs.s3a.secret.key=minio123 \
--conf spark.hadoop.fs.s3a.path.style.access=true \
--conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem \
--jars /spark/examples/spark_job-1.0-SNAPSHOT.jar \
--class ymlai87416.ds.spark.WordCount /spark/examples/spark_job-1.0-SNAPSHOT.jar s3a://testapp/input.txt s3a://testapp/output.txt


SPARK_MASTER="cas001-spark-master"

docker cp /Users/yiuminglai/GitProjects/system_design/spark_job/target/spark_job-1.0-SNAPSHOT.jar spark-master:/spark/examples

maven-archetype-quickstart

