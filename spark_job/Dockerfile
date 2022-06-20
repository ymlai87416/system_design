FROM ymlai87416/spark:java-1.0
ARG JAR_FILE3=lib/mysql-connector-java-8.0.29.jar

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar
COPY ${JAR_FILE3} mysql-connector-java-8.0.29.jar
