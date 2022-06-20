import os

def readFile(filepath):
    f = open(filepath, "r", encoding='UTF-8')
    value = f.read()
    f.close()
    return value

# Minio
MINIO_API_HOST = os.environ.get('MINIO_API_HOST')
MINIO_ACCESS_KEY = os.environ.get('MINO_ACCESS_KEY')
MINIO_SECRET_KEY = os.environ.get('MINIO_SECRET_KEY')
MINIO_BUCKET = os.environ.get('MINIO_BUCKET')
DB_URL = os.environ.get('DB_URL')
DB_USER = os.environ.get('DB_USER')
DB_PASSWORD = os.environ.get('DB_PASSWORD')

# Kafka
KAFKA_SERVER = os.environ.get('KAFKA_SERVER')
TOPIC = 'testapp-wordcount'
TEST_TOPIC = 'testapp'


# Kubernetes
KUBERNETES_SERVER = "https://" + os.environ.get('KUBERNETES_SERVICE_HOST', 'localhost') + ":" + os.environ.get('KUBERNETES_PORT_443_TCP_PORT', '443')
KUBERNETES_TOKEN = readFile("/var/run/secrets/kubernetes.io/serviceaccount/token")


TEST_JOB_YAML = """
apiVersion: "sparkoperator.k8s.io/v1beta2"
kind: SparkApplication
metadata:
  name: spark-pi
  namespace: spark-operator
spec:
  type: Scala
  mode: cluster
  image: "gcr.io/spark-operator/spark:v3.1.1"
  imagePullPolicy: Always
  mainClass: org.apache.spark.examples.SparkPi
  mainApplicationFile: "local:///opt/spark/examples/jars/spark-examples_2.12-3.1.1.jar"
  sparkVersion: "3.1.1"
  restartPolicy:
    type: Never
  volumes:
    - name: "test-volume"
      hostPath:
        path: "/tmp"
        type: Directory
  driver:
    cores: 1
    coreLimit: "1200m"
    memory: "512m"
    labels:
      version: 3.1.1
    serviceAccount: spark-release-spark
    volumeMounts:
      - name: "test-volume"
        mountPath: "/tmp"
  executor:
    cores: 1
    instances: 1
    memory: "512m"
    labels:
      version: 3.1.1
    volumeMounts:
      - name: "test-volume"
        mountPath: "/tmp"
"""

REAL_JOB_YAML = """
apiVersion: "sparkoperator.k8s.io/v1beta2"
kind: SparkApplication
metadata:
  name: {name}
  namespace: spark-operator
spec:
  type: Java
  mode: cluster
  image: "ymlai87416/sd-wordcount-spark:1.0"
  imagePullPolicy: Always
  mainClass: ymlai87416.sd.spark.WordCount
  mainApplicationFile: "local:///app/app.jar"
  arguments: 
    - "{name}"
    - "{argumentList}"
  sparkVersion: "3.2"
  restartPolicy:
    type: Never
  volumes:
    - name: "test-volume"
      hostPath:
        path: "/tmp"
        type: Directory
  driver:
    cores: 1
    coreLimit: "1200m"
    memory: "512m"
    labels:
      version: 3.1.1
    serviceAccount: spark-release-spark
    volumeMounts:
      - name: "test-volume"
        mountPath: "/tmp"
    env:
      - name: MINIO_API_HOST
        value: {minio_api_host}
      - name: MINIO_ACCESS_KEY
        value: {minio_access_key}
      - name: MINIO_SECRET_KEY
        value: {minio_secret_key}
      - name: DB_URL
        value: {db_url}
      - name: DB_USER
        value: {db_user}
      - name: DB_PASSWORD
        value: {db_password}
  executor:
    cores: 1
    instances: 1
    memory: "512m"
    labels:
      version: 3.1.1
    volumeMounts:
      - name: "test-volume"
        mountPath: "/tmp"
    env:
      - name: MINIO_API_HOST
        value: {minio_api_host}
      - name: MINIO_ACCESS_KEY
        value: {minio_access_key}
      - name: MINIO_SECRET_KEY
        value: {minio_secret_key}
      - name: DB_URL
        value: {db_url}
      - name: DB_USER
        value: {db_user}
      - name: DB_PASSWORD
        value: {db_password}
    
  deps:
    jars:
      - "local:///app/mysql-connector-java-8.0.29.jar" 
"""

