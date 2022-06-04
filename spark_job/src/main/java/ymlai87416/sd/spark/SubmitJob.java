package ymlai87416.sd.spark;
import org.apache.spark.launcher.SparkLauncher;

import java.io.IOException;

public class SubmitJob {

    public static void main(String[] args) throws IOException, InterruptedException {
        String appResource = "/jobs/job.jar";
        String mainClass = "ymlai87416.sd.spark.WordCount";
        String sparkHome = "/spark";

        final String[] appArgs = new String[]{
                "1",
                "s3a://testapp/input.txt",
        };


        SparkLauncher spark = new SparkLauncher()
                .setVerbose(true)
                //.setJavaHome(javaHome)
                .setSparkHome(sparkHome)
                .setAppResource(appResource)    // "/my/app.jar"
                .setMainClass(mainClass)        // "my.spark.app.Main"
                .setMaster("spark://spark-master:7077")
                .setConf("spark.delta.logStore.class", "org.apache.spark.sql.delta.storage.S3SingleDriverLogStore")
                .setConf("spark.hadoop.fs.s3a.endpoint", "http://minio1:9000")
                .setConf("spark.hadoop.fs.s3a.access.key", "minio")
                .setConf("spark.hadoop.fs.s3a.secret.key", "minio123")
                .setConf("spark.hadoop.fs.s3a.path.style.access", "true")
                .setConf("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .addAppArgs(appArgs);

        Process proc = spark.launch();

        proc.waitFor();
    }
}
