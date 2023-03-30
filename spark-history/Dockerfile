FROM gcr.io/spark-operator/spark:v3.1.1-hadoop3

USER root

RUN groupadd -g 185 spark && \
    useradd -u 185 -g 185 spark

USER 185
ENTRYPOINT bash /opt/spark/sbin/spark-daemon.sh start org.apache.spark.deploy.history.HistoryServer 1