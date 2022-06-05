FROM bde2020/spark-master:3.2.0-hadoop3.2

ARG JAR_FILE1=spark-master-rest-0.0.1-SNAPSHOT.jar
ARG JAR_FILE2=spark_job-1.0-SNAPSHOT.jar
ARG JAR_FILE3=mysql-connector-java-8.0.29.jar

EXPOSE 7077
EXPOSE 8080
EXPOSE 8090

ENV CLASSPATH=/spark/jars/*

WORKDIR /spark/jars

COPY hadoop-aws-3.3.1.jar hadoop-aws-3.3.1.jar
COPY aws-java-sdk-bundle-1.11.901.jar aws-java-sdk-bundle-1.11.901.jar

RUN mkdir /jobs
WORKDIR /jobs

COPY ${JAR_FILE2} job.jar
COPY ${JAR_FILE3} mysql-connector-java.jar

WORKDIR /

COPY ${JAR_FILE1} app.jar
COPY master.sh master.sh
