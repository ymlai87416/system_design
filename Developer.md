# Develop

## Contribute

## Component explain

Service recovery - zookeeper

    at least 3 for leader election
    Webservice use this for service recovery

Webservice - Spring boot

    Read API
    Write API
    Enhance to have rate limit function

To build, execute the followng command
```
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-read-api:1.1 .
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-write-api:1.1 .
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-main-api:1.1 .
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

DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-kafka-consumer:pyhon-1.0 .
```

Batch processing - Apache Spark and Minio

For the new version, I use [GoogleCloudPlatform/spark-on-k8s-operator](https://github.com/GoogleCloudPlatform/spark-on-k8s-operator)

useful command
```bash
# build the spark base image
docker-image-tool.sh -r ymlai87416 -t java-1.0 build
docker push ymlai87416/spark:java-1.0

# build my image
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-wordcount-spark:1.0 .
docker push ymlai87416/sd-wordcount-spark:1.0

```

Old version, move to k8s, the following project is removed
* sd-spark-master
* sd-spark-worker
* sd-spark-submit

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

