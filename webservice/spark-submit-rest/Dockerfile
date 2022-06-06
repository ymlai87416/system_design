FROM bde2020/spark-submit:3.2.0-hadoop3.2

ARG JAR_FILE1=target/*.jar
ARG JAR_FILE2=lib/spark_job-1.0-SNAPSHOT.jar
ARG JAR_FILE3=lib/mysql-connector-java-8.0.29.jar

EXPOSE 8080

ENV CLASSPATH=/spark/jars/*

RUN mkdir /jobs
WORKDIR /jobs

COPY ${JAR_FILE2} job.jar
COPY ${JAR_FILE3} mysql-connector-java.jar

WORKDIR /

COPY ${JAR_FILE1} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]
