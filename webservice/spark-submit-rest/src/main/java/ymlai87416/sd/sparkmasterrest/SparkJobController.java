package ymlai87416.sd.sparkmasterrest;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;
import org.apache.spark.launcher.SparkLauncher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
public class SparkJobController {

    @Value("${MINIO_URL:http://localhost:9001}") public String minioUrl;
    @Value("${MINIO_ACCESS:minio}") public String minioAccessKey;
    @Value("${MINIO_SECRET:minio123}") public String minioSecretKey;

    Logger logger = LoggerFactory.getLogger(SparkJobController.class);

    @RequestMapping(method= RequestMethod.POST, value = "/wordcount", consumes = "text/plain")
    public void uploadMessage(@RequestBody String postPayload){
        int cIndex = postPayload.indexOf(':');
        int id = Integer.valueOf(postPayload.substring(0, cIndex));
        String content = postPayload.substring(cIndex+1);

        //now hash the content and upload to minio
        String s3path = uploadMinio(content);
        if(s3path == null) return;

        //upload complete, trigger the job
        triggerWordCountJob(id, s3path);
    }

    private String uploadMinio(String input){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            String filename = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();

            //now save it in tmp folder
            //write
            try {
                FileOutputStream outputStream = new FileOutputStream("/tmp" + filename);
                byte[] strToBytes = input.getBytes();
                outputStream.write(strToBytes);
                outputStream.close();
            } catch (IOException e) {
                logger.error("An error occurred when writing to temp file", e);

                return null;
            }

            try {
                // Create a minioClient with the MinIO server playground, its access key and secret key.
                MinioClient minioClient =
                        MinioClient.builder()
                                .endpoint(minioUrl)
                                .credentials(minioAccessKey, minioSecretKey)
                                .build();

                // Make 'asiatrip' bucket if not exist.
                boolean found =
                        minioClient.bucketExists(BucketExistsArgs.builder().bucket("testapp").build());
                if (!found) {
                    // Make a new bucket called 'asiatrip'.
                    minioClient.makeBucket(MakeBucketArgs.builder().bucket("testapp").build());
                } else {
                    logger.info("Bucket 'testapp' already exists.");
                }

                minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket("testapp")
                                .object(filename)
                                .filename("/tmp"+filename)
                                .build());
                logger.info(
                        String.format("'%s' is successfully uploaded as "
                                + "object '%s' to bucket '%s'.\n", filename, "input.txt", "testapp"));

                //delete
                File tempFile = new File("/tmp" + filename);
                tempFile.delete();
            } catch (MinioException e) {
                logger.error("Error occurred: " + e);
                logger.error("HTTP trace: " + e.httpTrace());
            }

            return "s3a://testapp/" + filename;
        }
        catch(Exception ex){
            logger.error("Error occurred: ",ex);
            return null;
        }
    }

    private void triggerWordCountJob(int userid, String s3path){
        logger.info(String.format("Triggering job %d, %s", userid, s3path));

        final String[] appArgs = new String[]{
                String.valueOf(userid),
                s3path
        };

        String appResource = "/jobs/job.jar";
        String mainClass = "ymlai87416.sd.spark.WordCount";
        String sparkHome = "/spark";
        String jdbcJar = "/jobs/mysql-connector-java.jar";

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

        try {
            spark.launch();
        } catch (IOException e) {
            logger.error("Error occurred: ",e);
        }
    }

}
