A simple environment to test out system design concept

## Story

A simple system to allow user to login and save some random message.
A summary dashboard to show the total word count of all message.

## Features

### Todo list

- [x] Web API
- [x] Web
- [x] Zookeeper
- [x] Redis
- [x] Kafka
- [x] Minio
- [x] Spark
- [ ] Notification and STOMP

### Tech spec

Deployed following cluster on Kubernetes for fun.

| Tech  | Spec  | Usage  |
|---|---|---|
| Redis  | cluster of 3 by [bitnami/redis-cluster](https://github.com/bitnami/charts/tree/master/bitnami/redis-cluster)  | As a cache cluster  |
| Kafka  | cluster of 3 by [bitnami/kafka](https://github.com/bitnami/charts/tree/master/bitnami/kafka)  | As message queue  |
| Minio  | cluster of 4 by [minio/minio](https://github.com/minio/charts)  | Store data for Spark to process.  |
| Spark  | Use  [GoogleCloudPlatform/spark-on-k8s-operator](https://github.com/GoogleCloudPlatform/spark-on-k8s-operator) | Ask backend wordcount processing  |
| Zookeeper  | cluster of 3 by [bitnami/zookeeper](https://github.com/bitnami/charts/tree/master/bitnami/zookeeper)  | Keep track of Kafka and spring boot discovery  |
| MySQL  | A single host database  | As database  |


### Related dockerhub repo

| docker hub  | function  | 
|---|---|
| [sd-read-api](https://hub.docker.com/repository/docker/ymlai87416/sd-read-api)  | Read API  |
| [sd-write-api](https://hub.docker.com/repository/docker/ymlai87416/sd-write-api)  | Write API  |
| [sd-main-api](https://hub.docker.com/repository/docker/ymlai87416/sd-main-api)  | Main API  |
| [sd-wordcount-spark](https://hub.docker.com/repository/docker/ymlai87416/sd-wordcount-spark)  | Spark job  |
| [sd-kafka-consumer](https://hub.docker.com/repository/docker/ymlai87416/sd-kafka-consumer)  | Kafka consumer  |
| [sd-web](https://hub.docker.com/repository/docker/ymlai87416/sd-web)  | Web  |