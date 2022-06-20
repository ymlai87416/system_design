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

def delivery_report(err, msg):
    """ Called once for each message produced to indicate delivery result.
        Triggered by poll() or flush(). """
    if err is not None:
        print('Message delivery failed: {}'.format(err))
    else:
        print('Message delivered to {} [{}]'.format(msg.topic(), msg.partition()))

def createTopic():
    admin_client = AdminClient({
        "bootstrap.servers": config.KAFKA_SERVER, 'debug': 'all'
    })

    topic_list = []
    topic_list.append(NewTopic(config.TEST_TOPIC, 1, 1))
    admin_client.create_topics(topic_list)


def testKafkaProducer():
    p = Producer({'bootstrap.servers': config.KAFKA_SERVER,
        'client.id': socket.gethostname()})

    for i in range(100):
        data = {'num': i, 'ts': datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')}
        # Trigger any available delivery report callbacks from previous produce() calls
        p.poll(0)

        # Asynchronously produce a message, the delivery report callback
        # will be triggered from poll() above, or flush() below, when the message has
        # been successfully delivered or failed permanently.
        p.produce(config.TEST_TOPIC, json.dumps(data).encode('utf-8'), callback=delivery_report)

        time.sleep(1)

    # Wait for any outstanding messages to be delivered and delivery report
    # callbacks to be triggered.
    p.flush()

# https://docs.confluent.io/kafka-clients/python/current/overview.html
def testKafkaConsumer():

    c = Consumer({
        'bootstrap.servers': config.KAFKA_SERVER,
        'group.id': 'mygroup',
        'auto.offset.reset': 'earliest'
    })

    c.subscribe([config.TEST_TOPIC])

    while True:
        msg = c.poll(1.0)

        if msg is None:
            continue
        if msg.error():
            print("Consumer error: {}".format(msg.error()))
            continue

        print('Received message: {}'.format(msg.value().decode('utf-8')))

    c.close()


def createJobYaml(type, arg_dict):
    if type == "TEST":
        return config.TEST_JOB_YAML
    else:
        return config.REAL_JOB_YAML.format(**arg_dict)

def yamlToJson(yaml_in):
    yaml_object = yaml.safe_load(yaml_in) # yaml_object will be a list or a dict
    return json.dumps(yaml_object)

def submitJob(k8s_client, yaml_text):
    
    #yaml_dict = yaml.safe_load(yaml_text)
    #kubernetes.utils.create_from_dict(k8s_client,yaml_dict,verbose=True, namespace="spark-opeator")

    api_instance = kubernetes.client.CustomObjectsApi(k8s_client)
    group = 'sparkoperator.k8s.io' # str | The custom resource's group name
    version = 'v1beta2' # str | The custom resource's version
    namespace = 'spark-operator' # str | The custom resource's namespace
    plural = 'sparkapplications' # str | The custom resource's plural name. For TPRs this would be lowercase plural kind.
    body = yaml.safe_load(yaml_text) # object | The JSON schema of the Resource to create.
    
    try:
        api_response = api_instance.create_namespaced_custom_object(group, version, namespace, plural, body)
        print(api_response)
    except ApiException as e:
        print("Exception when calling CustomObjectsApi->create_namespaced_custom_object: %s\n" % e)

def getJob(k8s_client, name):
    custom_object_api = kubernetes.client.CustomObjectsApi(k8s_client)

    group = 'sparkoperator.k8s.io' # str | the custom resource's group
    version = 'v1beta2' # str | the custom resource's version
    namespace = 'spark-operator' # str | The custom resource's namespace
    plural = 'sparkapplications' # str | the custom resource's plural name. For TPRs this would be lowercase plural kind.

    job = custom_object_api.get_namespaced_custom_object(
        group=group, version=version, namespace=namespace, plural=plural, name=name
    )

    return job

def jobCompleted(k8s_client, name):
    try:
        job = getJob(k8s_client, name)
        
        # how to get the status of the spark application?
        # {"apiVersion":"sparkoperator.k8s.io/v1beta2","kind":"SparkApplication","metadata":{"annotations":{"kubectl.kubernetes.io/last-applied-configuration":"{\"apiVersion\":\"sparkoperator.k8s.io/v1beta2\",\"kind\":\"SparkApplication\",\"metadata\":{\"annotations\":{},\"name\":\"spark-pi\",\"namespace\":\"spark-operator\"},\"spec\":{\"driver\":{\"coreLimit\":\"1200m\",\"cores\":1,\"labels\":{\"version\":\"3.1.1\"},\"memory\":\"512m\",\"serviceAccount\":\"spark-release-spark\",\"volumeMounts\":[{\"mountPath\":\"/tmp\",\"name\":\"test-volume\"}]},\"executor\":{\"cores\":1,\"instances\":1,\"labels\":{\"version\":\"3.1.1\"},\"memory\":\"512m\",\"volumeMounts\":[{\"mountPath\":\"/tmp\",\"name\":\"test-volume\"}]},\"image\":\"gcr.io/spark-operator/spark:v3.1.1\",\"imagePullPolicy\":\"Always\",\"mainApplicationFile\":\"local:///opt/spark/examples/jars/spark-examples_2.12-3.1.1.jar\",\"mainClass\":\"org.apache.spark.examples.SparkPi\",\"mode\":\"cluster\",\"restartPolicy\":{\"type\":\"Never\"},\"sparkVersion\":\"3.1.1\",\"type\":\"Scala\",\"volumes\":[{\"hostPath\":{\"path\":\"/tmp\",\"type\":\"Directory\"},\"name\":\"test-volume\"}]}}\n"},"creationTimestamp":"2022-06-18T01:17:31Z","generation":1,"managedFields":[{"apiVersion":"sparkoperator.k8s.io/v1beta2","fieldsType":"FieldsV1","fieldsV1":{"f:metadata":{"f:annotations":{".":{},"f:kubectl.kubernetes.io/last-applied-configuration":{}}},"f:spec":{".":{},"f:driver":{".":{},"f:coreLimit":{},"f:cores":{},"f:labels":{".":{},"f:version":{}},"f:memory":{},"f:serviceAccount":{},"f:volumeMounts":{}},"f:executor":{".":{},"f:cores":{},"f:instances":{},"f:labels":{".":{},"f:version":{}},"f:memory":{},"f:volumeMounts":{}},"f:image":{},"f:imagePullPolicy":{},"f:mainApplicationFile":{},"f:mainClass":{},"f:mode":{},"f:restartPolicy":{".":{},"f:type":{}},"f:sparkVersion":{},"f:type":{},"f:volumes":{}}},"manager":"kubectl-client-side-apply","operation":"Update","time":"2022-06-18T01:17:31Z"},{"apiVersion":"sparkoperator.k8s.io/v1beta2","fieldsType":"FieldsV1","fieldsV1":{"f:status":{".":{},"f:applicationState":{".":{},"f:state":{}},"f:driverInfo":{".":{},"f:podName":{}},"f:executionAttempts":{},"f:executorState":{".":{},"f:spark-pi-666b698174631bbf-exec-1":{}},"f:lastSubmissionAttemptTime":{},"f:sparkApplicationId":{},"f:submissionAttempts":{},"f:submissionID":{},"f:terminationTime":{}}},"manager":"spark-operator","operation":"Update","subresource":"status","time":"2022-06-18T01:17:43Z"}],"name":"spark-pi","namespace":"spark-operator","resourceVersion":"87449162","uid":"29dc1832-ca4d-4c37-a8ed-ecc13f6d9085"},"spec":{"driver":{"coreLimit":"1200m","cores":1,"labels":{"version":"3.1.1"},"memory":"512m","serviceAccount":"spark-release-spark","volumeMounts":[{"mountPath":"/tmp","name":"test-volume"}]},"executor":{"cores":1,"instances":1,"labels":{"version":"3.1.1"},"memory":"512m","volumeMounts":[{"mountPath":"/tmp","name":"test-volume"}]},"image":"gcr.io/spark-operator/spark:v3.1.1","imagePullPolicy":"Always","mainApplicationFile":"local:///opt/spark/examples/jars/spark-examples_2.12-3.1.1.jar","mainClass":"org.apache.spark.examples.SparkPi","mode":"cluster","restartPolicy":{"type":"Never"},"sparkVersion":"3.1.1","type":"Scala","volumes":[{"hostPath":{"path":"/tmp","type":"Directory"},"name":"test-volume"}]},"status":{"applicationState":{"state":"COMPLETED"},"driverInfo":{"podName":"spark-pi-driver"},"executionAttempts":1,"executorState":{"spark-pi-666b698174631bbf-exec-1":"COMPLETED"},"lastSubmissionAttemptTime":"2022-06-18T01:17:34Z","sparkApplicationId":"spark-944143428da24030ac887a7a728af2d0","submissionAttempts":1,"submissionID":"0c4229f6-64fd-4ea7-ac9b-f28b415c7571","terminationTime":"2022-06-18T01:17:49Z"}}
        return job["status"]["applicationState"]["state"] == "COMPLETED" or \
            job["status"]["applicationState"]["state"] == "FAILED"
    except:
        return False

def deleteJob(k8s_client, name):
    custom_object_api = kubernetes.client.CustomObjectsApi(k8s_client)

    group = 'sparkoperator.k8s.io' # str | the custom resource's group
    version = 'v1beta2' # str | the custom resource's version
    namespace = 'spark-operator' # str | The custom resource's namespace
    plural = 'sparkapplications' # str | the custom resource's plural name. For TPRs this would be lowercase plural kind.

    custom_object_api.delete_namespaced_custom_object(group=group, version=version, namespace=namespace,
        plural=plural, name=name, async_req=False)

def getFileHash(text):
    return hashlib.md5(text.encode('utf-8')).hexdigest()

def writeToTempFolder(filename, content):
    absolute_path = "/tmp/" + filename
    f = open(absolute_path, "w", encoding='UTF-8')
    f.write(content)
    f.close()

    return absolute_path

def createBucketIfNotExist(MINIO_CLIENT):
    found = MINIO_CLIENT.bucket_exists(config.MINIO_BUCKET)
    if not found:
       MINIO_CLIENT.make_bucket(config.MINIO_BUCKET)
    else:
       print("Bucket already exists")

def uploadFileS3(text):
    MINIO_CLIENT = Minio(config.MINIO_API_HOST, access_key=config.MINIO_ACCESS_KEY, secret_key=config.MINIO_SECRET_KEY, secure=False)

    createBucketIfNotExist(MINIO_CLIENT)

    filename = getFileHash(text)
    path = writeToTempFolder(filename, text)

    MINIO_CLIENT.fput_object(config.MINIO_BUCKET, filename, path)
    os.remove(path)

    return "s3a://testapp/" + filename

def deleteFileS3(file):
    MINIO_CLIENT = Minio(config.MINIO_API_HOST, access_key=config.MINIO_ACCESS_KEY, secret_key=config.MINIO_SECRET_KEY, secure=False)
    MINIO_CLIENT.remove_object(config.MINIO_BUCKET, file)

text="""Don't want to be a Canadian idiot
Don't want to be some beer swillin' hockey nut
And do I look like some frost bitten hosehead
I never learned my alphabet from A to zed
They all live on donuts and moose meat
And they leave the house without packin' heat
Never even bring their guns to the mall
And you know what else is too funny
Their stupid monopoly money
Can't take 'em seriously at all"""

def testUploadFile():
    uploadFileS3(text)

def testDeleteFile():
    filename = getFileHash(text)
    deleteFileS3(filename)

def getK8sClient():
    configuration= kubernetes.client.Configuration()
    configuration.api_key['authorization'] = config.KUBERNETES_TOKEN
    configuration.api_key_prefix['authorization'] = 'Bearer'
    configuration.host = config.KUBERNETES_SERVER
    configuration.ssl_ca_cert='/var/run/secrets/kubernetes.io/serviceaccount/ca.crt'

    k8s_client = kubernetes.client.ApiClient(configuration)

    return k8s_client

def testSubmitJob():
    k8s_client = getK8sClient()
    yaml_text = createJobYaml("TEST", None)
    submitJob(k8s_client, yaml_text)
    for i in range(100):
        if jobCompleted(k8s_client, "spark-pi"):
            break
        time.sleep(1)
    deleteJob(k8s_client, "spark-pi")

def testGetJob():
    k8s_client = getK8sClient()
    for i in range(60):
        job = getJob(k8s_client, "spark-pi")
        print(job)
        time.sleep(1)



def submitWordCountJob(name, id_list):
    
    k8s_client = getK8sClient()

    argumentListStr = ",".join(id_list)

    yaml_text = yaml_text = createJobYaml("WORD_COUNT", 
        {
            "name": name, "argumentList": argumentListStr,
            "minio_api_host": config.MINIO_API_HOST, "minio_access_key": config.MINIO_ACCESS_KEY,
            "minio_secret_key": config.MINIO_SECRET_KEY,
            "db_url": config.DB_URL, "db_user": config.DB_USER, "db_password": config.DB_PASSWORD
        })
    submitJob(k8s_client, yaml_text)
    for i in range(100):
        if jobCompleted(k8s_client, name):
            break
        time.sleep(1)
    deleteJob(k8s_client, name)


def testSubmitJob2():
    name = getJobName()
    submitWordCountJob(name, ["1:s3a://testapp/input.txt"])
    

def getJobName():
    return "wordcount-"+secrets.token_urlsafe(nbytes=8).lower()

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

        #print('Received message: {}'.format())
        message = msg.value().decode('utf-8')
        colIdx = message.index(":")
        userId = message[:colIdx]
        text = message[colIdx+1:]
        path = uploadFileS3(text)

        jobName = getJobName()
        submitWordCountJob(jobName, [userId+":"+path])

        print("Processed a message")
        c.commit(asynchronous=True)

    c.close()

if __name__ == '__main__':
    if len(sys.argv) == 1:
        runConsumer()


    elif sys.argv[1] == "TEST_MINIO_UPLOAD":
        # tested local by docker
        testUploadFile()
    elif sys.argv[1] == "TEST_MINIO_DELETE":
        # tested local by docker
        testDeleteFile()
    elif sys.argv[1] == "TEST_KAFKA_CREATE_TOPIC":
        # test local by docker
        createTopic()
    elif sys.argv[1] == "TEST_KAFKA_PRODUCE":
        # test local by docker
        testKafkaProducer()
    elif sys.argv[1] == "TEST_KAFKA_CONSUME":
        # test local by docker
        testKafkaConsumer()
    elif sys.argv[1] == "TEST_SUBMIT_JOB":
        # debug on k8s
        testSubmitJob()
    elif sys.argv[1] == "TEST_SUBMIT_JOB2":
        # debug on k8s
        testSubmitJob2()
    elif sys.argv[1] == "TEST_GET_JOB":
        testGetJob()
    elif sys.argv[1] == "TEST_SUBMIT_JOB":
        # debug on k8s
        testSubmitJob()
        