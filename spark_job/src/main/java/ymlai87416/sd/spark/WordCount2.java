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

public class WordCount2 {
    private static final Pattern SPACE = Pattern.compile(" ");
    private static Logger log = LogManager.getRootLogger();

    private static void wordCount(int id, String infile) {

        SparkSession spark = SparkSession
                .builder()
                .appName("JavaWordCount")
                .getOrCreate();

        JavaRDD<String> inputFile = spark.read().textFile(infile).javaRDD();
        JavaRDD<String> wordsFromFile = inputFile.flatMap(s -> Arrays.asList(SPACE.split(s)).iterator());

        JavaPairRDD countData = wordsFromFile.mapToPair(t -> new Tuple2(t, 1)).reduceByKey((x, y) -> (int) x + (int) y);

        try {
            countData.saveAsTextFile("s3a://testapp/output.txt");
        }
        catch(Exception ex){
            log.error("Error when writing file", ex);
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
