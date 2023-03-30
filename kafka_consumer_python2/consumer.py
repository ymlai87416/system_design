from __future__ import absolute_import
import json
import time
import datetime
import config
from confluent_kafka import Producer, Consumer
from confluent_kafka.admin import AdminClient, NewTopic
import yaml
import kubernetes
import hashlib
import os
import sys
from minio import Minio
import socket
from kubernetes.client.rest import ApiException
import secrets
import requests
from redis.cluster import RedisCluster as Redis

def testRedis():
    rc = Redis(host=config.REDIS_SERVER, port=config.REDIS_PORT)
    rc.set('foo', 'bar')
    rc.delete("foo")

def removeRedisWordCountCache(user_id):
    rc = Redis(host=config.REDIS_SERVER, port=config.REDIS_PORT)
    rc.delete("word_count::"+user_id)

def sendUserNotification(userId, message):
    url = config.NOT_SERVER + "/not/fcm/" + userId;
    payload = message # add paramaters to the dictionary as needed

    r = requests.post(url, data=payload)
    return r.text

def testNotificationService():
    sendUserNotification(2, "This is a testing.")

def runConsumer():
    c = Consumer({
        'bootstrap.servers': config.KAFKA_SERVER,
        'group.id': 'mygroup',
        'auto.commit.enable': False,
        'auto.offset.reset': 'earliest'
    })

    c.subscribe([config.TOPIC])

    while True:
        msg = c.poll(1.0)

        if msg is None:
            continue
        if msg.error():
            print("Consumer error: {}".format(msg.error()))
            continue

        try:
            user_id = int(msg.value().decode('utf-8'))

            # clear redis cache
            # read here: https://github.com/redis/redis-py#cluster-mode
            removeRedisWordCountCache(user_id)
            # call notification service
            sendUserNotification(user_id, "batch completed")

            print("Processed a message")
        except:
            print("Ignore a message: " + msg.value().decode('utf-8'))

        
        c.commit(asynchronous=True)

    c.close()


if __name__ == '__main__':
    if len(sys.argv) == 1:
        runConsumer()
    elif sys.argv[1] == "TEST_REDIS":
        # tested local by docker
        testRedis()
