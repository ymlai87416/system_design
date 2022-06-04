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
```