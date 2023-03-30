# Develop

## Contribute

## Component explain

### Service recovery - zookeeper

    at least 3 for leader election
    Webservice use this for service recovery


### Web services

To build, execute the followng command
```
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-read-api:1.1 .
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-write-api:1.1 .
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-main-api:1.1 .
```

### Database - MySQL

    3 database sharding. have read replica.

    Please check ./database for creating schema and populating data.

### Cache - Redis

    3 cache sharding. have read replica.

### Web - React and spring boot

To build, execute the followng command
```
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-web:1.0 .
docker push  ymlai87416/sd-web:1.0
```

### Message - Kafka
    
useful command
```
kafka-topics --create --topic testapp-wordcount --bootstrap-server kafka:9092
kafka-console-producer --topic testapp-wordcount --bootstrap-server kafka:9092
kafka-console-consumer --topic testapp-wordcount --from-beginning --bootstrap-server kafka:9092

DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-kafka-consumer:pyhon-1.0 .
```

### Batch processing - Apache Spark and Minio

For the new version, I use [GoogleCloudPlatform/spark-on-k8s-operator](https://github.com/GoogleCloudPlatform/spark-on-k8s-operator)

useful command
```bash
# build the spark base image by cloning your local env.
docker-image-tool.sh -r ymlai87416 -t java-1.0 build
docker push ymlai87416/spark:java-1.0

# build my image
DOCKER_BUILDKIT=1 docker build -t ymlai87416/sd-wordcount-spark:1.0 .
docker push ymlai87416/sd-wordcount-spark:1.0

```

### Notification - SockJS

cluster: https://stackoverflow.com/questions/32861645/spring-websocket-sendtouser-from-a-cluster-does-not-work-from-backup-server

According to research, make use of fcm to do the notification.
Have a function to let user register token and we send the token to this client.

```bash

```

## Spark history server
https://chowdera.com/2021/10/20211023053725807Q.html
https://github.com/GoogleCloudPlatform/spark-on-k8s-operator/issues/1295
https://stackoverflow.com/questions/51798927/spark-ui-history-server-on-kubernetes