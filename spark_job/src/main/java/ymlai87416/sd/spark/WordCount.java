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
import java.util.*;
import java.util.regex.Pattern;

public class WordCount {

    private static final Pattern SPACE = Pattern.compile(" ");
    private static Logger log = LogManager.getRootLogger();

    private static String minioEndpoint = System.getenv("MINIO_API_HOST");
    private static String minioAccess = System.getenv("MINIO_ACCESS_KEY");
    private static String minioSecret = System.getenv("MINIO_SECRET_KEY");
    private static String connectionString = System.getenv("DB_URL");
    private static String dbUser = System.getenv("DB_USER");
    private static String dbPassword = System.getenv("DB_PASSWORD");
    private static String sleep = System.getenv("SLEEP");
        //"jdbc:mysql://db:3306/testapp?useUnicode=true&characterEncoding=UTF-8&useSSL=false";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            log.error("Database driver loading failed!", e);
        }
    }

    private static void wordCount(String jobId, Map<Integer, String> data) {

        if(sleep !=null)
            try {
                Thread.sleep(1000*24*60*60); //sleep for a day
            } catch (InterruptedException e) {
                log.error("Thread sleep failed!", e);
            }
        /* 
        System.out.println("Environment variable");
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n", envName, env.get(envName));
        }
        */
        /*
        System.out.println("MINIO_API_HOST " + minioEndpoint);
        System.out.println("MINIO_ACCESS_KEY "+ minioAccess);
        //log.info("MINIO_SECRET_KEY " + minioSecret);
        System.out.println("DB_URL " + connectionString);
        System.out.println("DB_USER "+ dbUser);
        //log.info("DB_PASSWORD "+ dbPassword);

         */

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaWordCount")
                .config("spark.delta.logStore.class", "org.apache.spark.sql.delta.storage.S3SingleDriverLogStore")
                .config("spark.hadoop.fs.s3a.endpoint", minioEndpoint)
                .config("spark.hadoop.fs.s3a.access.key", minioAccess)
                .config("spark.hadoop.fs.s3a.secret.key", minioSecret)
                .config("spark.hadoop.fs.s3a.path.style.access", "true")
                .config("spark.hadoop.fs.s3a.connection.ssl.enabled", "false")
                .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .getOrCreate();

        for(Map.Entry<Integer, String> entry: data.entrySet()){
            int userId = entry.getKey();
            String infile = entry.getValue();

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
            Dataset<Row> df2= df.withColumn("user_id",functions.lit(userId));
            Dataset<Row> df3= df2.withColumn("batch_id",functions.lit(jobId));

            Properties connectionProperties = new Properties();
            connectionProperties.put("driver", "com.mysql.jdbc.Driver");
            connectionProperties.put("url", connectionString);
            connectionProperties.put("user", dbUser);
            connectionProperties.put("password", dbPassword);

            df3.write().mode(SaveMode.Append).jdbc(connectionString, "word_count_stage", connectionProperties);

            Connection conn = null;
            try {
                conn = DriverManager.getConnection(connectionString, dbUser, dbPassword);
                Statement stmt = conn.createStatement();
                String sql1 = String.format(miniBatch, jobId);
                String sql2 = String.format(clearData, jobId);
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

        spark.close();
    }

    static String miniBatch = "INSERT INTO word_count (user_id, word, count)\n" +
            "select user_id, word, count from word_count_stage where batch_id='%s'\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "  count     = word_count.count + VALUES(count),\n" +
            "  updated_at = now()";
    static String clearData = "delete from word_count_stage where batch_id='%s'";

    public static Map<Integer, String> parseParam(String in){
        String[] records = in.split(",");
        HashMap<Integer, String> result = new HashMap<>();
        for(int i=0; i<records.length; ++i){
            int colIdx = records[i].indexOf(':');
            if(colIdx != -1) {
                try {
                    int userId = Integer.valueOf(records[i].substring(0, colIdx));
                    String s3Path = records[i].substring(colIdx + 1);
                    result.put(userId, s3Path);
                }
                catch(Exception ex){
                    log.warn("Invalid format, ignore record: " + records[i]);
                }
            }
            else{
                log.warn("Invalid format, ignore record: " + records[i]);
            }
        }
        
        return result;
    }
    public static void main(String[] args) {
        if (args.length < 1) {
            StringBuilder sb  = new StringBuilder();
            sb.append("Param: ");
            for(int i=0; i<args.length; ++i)
                sb.append(args[i] + " ");
            log.error(sb.toString());
            log.error("Usage: JavaWordCount <job-id> [userid:s3path,userid:s3path,...]");
            System.exit(1);
        }

        Map<Integer, String> data = parseParam(args[1]);

        wordCount(args[0], data);
    }

}
