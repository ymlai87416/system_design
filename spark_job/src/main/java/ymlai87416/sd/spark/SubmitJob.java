package ymlai87416.sd.spark;
import org.apache.spark.launcher.SparkLauncher;

import java.io.IOException;

public class SubmitJob {

    public static void main(String[] args) throws IOException, InterruptedException {
        String appResource = "/jobs/job.jar";
        String mainClass = "ymlai87416.sd.spark.WordCount";
        String sparkHome = "/spark";
        String jdbcJar = "/jobs/mysql-connector-java.jar";

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
                .addJar(jdbcJar)
                .addJar(appResource)
                .addAppArgs(appArgs);

        Process proc = spark.launch();
        new Thread(new ISRRunnable(proc.getErrorStream())).start();
        int exitCode = proc.waitFor();
        System.out.format("Finished! Exit code is %d\n" , exitCode);
    }
}
