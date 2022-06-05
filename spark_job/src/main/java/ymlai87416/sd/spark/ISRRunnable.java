package ymlai87416.sd.spark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ISRRunnable implements Runnable {
    private final BufferedReader reader;
    public static Logger logger = LoggerFactory.getLogger("ISRRunnable");

    private ISRRunnable(BufferedReader reader) {
        this.reader = reader;
    }

    public ISRRunnable(InputStream inputStream) {
        this(new BufferedReader(new InputStreamReader(inputStream)));
    }

    public void run() {
        String line = null;
        try {
            line = reader.readLine();
            while (line != null) {
                logger.info(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            logger.error("There is a exception when getting log: ",e);
            throw new RuntimeException("There is a exception when getting log.");
        }
    }
}