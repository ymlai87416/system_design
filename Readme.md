A simple environment to test out system design

## Story

A simple system to allow user to login and save some random message.
A summary dashboard to show the total word count of all message.


## Features

Service recovery - zookeeper

    at least 3 for leader election
    Webservice use this for service recovery

Webservice - Spring boot

    Read API
    Write API
    Enhance to have rate limit function

To build, execute the followng command
```
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-read-api:1.0 .
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-write-api:1.0 .
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-main-api:1.0 .
```

Database - MySQL

    3 database sharding. have read replica.

    Please check ./database for creating schema and populating data.

Cache - Redis

    3 cache sharding. have read replica.

Web - React and spring boot

    Nothing special, allow user to generate some random data.

Message - Kafka

    Able to process message
    
useful command
```
kafka-topics --create --topic testapp-wordcount --bootstrap-server kafka:9092
kafka-console-producer --topic testapp-wordcount --bootstrap-server kafka:9092
kafka-console-consumer --topic testapp-wordcount --from-beginning --bootstrap-server kafka:9092

DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-kafka-consumer:1.0 .
```

Batch processing - Apache Spark and Minio

useful command
```
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-spark-master:3.2.0-hadoop3.2 .
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-spark-worker:3.2.0-hadoop3.2 .

docker cp target/spark_job-1.1-SNAPSHOT.jar spark-master:/jobs/job.jar

java -cp "job.jar:/spark/jars/*" ymlai87416.sd.spark.SubmitJob

spark-submit --master spark://spark-master:7077 \
--conf spark.delta.logStore.class=org.apache.spark.sql.delta.storage.S3SingleDriverLogStore \
--conf spark.hadoop.fs.s3a.endpoint=http://minio1:9000 \
--conf spark.hadoop.fs.s3a.access.key=minio \
--conf spark.hadoop.fs.s3a.secret.key=minio123 \
--conf spark.hadoop.fs.s3a.path.style.access=true \
--conf spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem \
--jars /jobs/job.jar \
--jars /jobs/mysql-connector-java-8.0.29.jar \
--class ymlai87416.sd.spark.WordCount2 /jobs/job.jar 1 s3a://testapp/sample.txt

```


Task to do
* find main interface
* find jdbc on write-api\

study on the disk image
3 volumes expose: zk
    /var/lib/zookeeper/data
    /var/lib/zookeeper/log
    /etc/zookeeper/secrets
2 volumes expose: kafka
    /va/lib/kafka/data
    /etc/kafka/secrets
1 spark master data => seems there is no expose volume, but the log file
    the bde2000
1 spark worker data => all log store at /sparks/log
    but I don't store it for the time being...
1 There is a submit image, I can change it
