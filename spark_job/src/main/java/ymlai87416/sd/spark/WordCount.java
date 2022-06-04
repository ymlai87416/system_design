package ymlai87416.sd.spark;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

import java.sql.*;
import java.util.Arrays;
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
                .getOrCreate();

        JavaRDD<String> inputFile = spark.read().textFile(infile).javaRDD();
        JavaRDD<String> wordsFromFile = inputFile.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());

        JavaPairRDD countData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1)).reduceByKey((x, y) -> (int) x + (int) y);

        try {
            writeResultToDB(id, countData);
        }
        catch(Exception ex){
            log.error("Error when writing file", ex);
        }
    }

    static String connectionString = "jdbc:mysql://db:3306/testapp&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    static String prepareStatement = "INSERT INTO wordcount\n" +
            "  (id, word, count, createAt, updatedAt)\n" +
            "VALUES\n" +
            "  (?, ?, ?, ?, ?)\n" +
            "ON DUPLICATE KEY UPDATE\n" +
            "  count     = count + VALUES(count),\n" +
            "  updatedAt = VALUES(updatedAt)";
    static String user = "root";
    static String pwd = "password";

    private static void writeResultToDB(int id, JavaPairRDD resultRDD) throws SQLException{
        final Connection conn= DriverManager.getConnection(connectionString, user, pwd);
        try {

            resultRDD.foreach(new VoidFunction<Tuple2<String, Integer>>() {
                @Override
                public void call(Tuple2<String, Integer> o) throws Exception {
                    String word = o._1();
                    Integer newcount = o._2();
                    long jsbh = System.currentTimeMillis();
                    PreparedStatement psmt = conn.prepareStatement(prepareStatement);
                    psmt.setInt(1, id);
                    psmt.setString(2, word);
                    psmt.setInt(3, newcount);
                    psmt.setTimestamp(4, new Timestamp(jsbh));
                    psmt.setTimestamp(4, new Timestamp(jsbh));
                    psmt.executeUpdate();
                    psmt.close();
                }
            });

            conn.close();
        }
        catch(Exception ex){
            log.error("Error when writing file", ex);
        }
        finally{
            if(conn != null && !conn.isClosed()) conn.close();
        }
    }

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
