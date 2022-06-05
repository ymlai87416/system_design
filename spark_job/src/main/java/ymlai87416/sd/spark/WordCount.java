package ymlai87416.sd.spark;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.*;

import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.sql.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class WordCount {

    private static final Pattern SPACE = Pattern.compile(" ");
    private static Logger log = LogManager.getRootLogger();

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("Database driver loading failed!", e);
        }
    }

    private static void wordCount(int id, String infile) {

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaWordCount")
                .config("spark.delta.logStore.class", "org.apache.spark.sql.delta.storage.S3SingleDriverLogStore")
                .config("spark.hadoop.fs.s3a.endpoint", "http://minio1:9000")
                .config("spark.hadoop.fs.s3a.access.key", "minio")
                .config("spark.hadoop.fs.s3a.secret.key", "minio123")
                .config("spark.hadoop.fs.s3a.path.style.access", "true")
                .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .getOrCreate();

        UUID uuid = UUID.randomUUID();

        JavaRDD<String> inputFile = spark.read().textFile(infile).javaRDD();
        JavaRDD<String> wordsFromFile = inputFile.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());

        JavaPairRDD countData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1)).reduceByKey((x, y) -> (int) x + (int) y);

        StructType rfSchema = new StructType(new StructField[]{
                new StructField("user_id", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("word", DataTypes.StringType, false, Metadata.empty()),
                new StructField("count", DataTypes.IntegerType, false, Metadata.empty()),
                new StructField("batch_id", DataTypes.StringType, false, Metadata.empty())
        });

        Dataset<Row> df = spark.createDataset(JavaPairRDD.toRDD(countData),
                Encoders.tuple(Encoders.STRING(),Encoders.INT())).toDF("word","count");
        Dataset<Row> df2= df.withColumn("user_id",functions.lit(id));
        Dataset<Row> df3= df2.withColumn("batch_id",functions.lit(uuid.toString()));

        Properties connectionProperties = new Properties();
        connectionProperties.put("driver", "com.mysql.jdbc.Driver");
        connectionProperties.put("url", connectionString);
        connectionProperties.put("user", "root");
        connectionProperties.put("password", "password");

        df3.write().mode(SaveMode.Append).jdbc(connectionString, "word_count_stage", connectionProperties);

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connectionString, user, pwd);
            Statement stmt = conn.createStatement();
            String sql1 = String.format(miniBatch, uuid.toString());
            String sql2 = String.format(clearData, uuid.toString());
            stmt.executeUpdate(sql1);
            stmt.execute(sql2);
            stmt.close();
        }
        catch(Exception ex){
            log.error("Error when writing file", ex);
        }
        finally{
            try {
                if (conn != null && !conn.isClosed()) conn.close();
            }
            catch(Exception ex){ /* try my best to close */ };
        }
    }

    static String connectionString = "jdbc:mysql://db:3306/testapp?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    static String prepareStatement = "INSERT INTO wordcount\n" +
            "  (id, word, count, createAt, updatedAt)\n" +
            "VALUES\n" +
            "  (?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "  count     = count + VALUES(count),\n" +
            "  updatedAt = VALUES(updatedAt)";
    static String user = "root";
    static String pwd = "password";

    static String miniBatch = "INSERT INTO word_count (user_id, word, count)\n" +
            "select user_id, word, count from word_count_stage where batch_id='%s'\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "  count     = word_count.count + VALUES(count),\n" +
            "  updated_at = now()";
    static String clearData = "delete from word_count_stage where batch_id='%s'";


    public static void main(String[] args) {
        if (args.length < 2) {
            StringBuilder sb  = new StringBuilder();
            sb.append("Param: ");
            for(int i=0; i<args.length; ++i)
                sb.append(args[i] + " ");
            log.error(sb.toString());
            log.error("Usage: JavaWordCount <id> <in file>");
            System.exit(1);
        }

        wordCount(Integer.valueOf(args[0]), args[1]);
    }

}
