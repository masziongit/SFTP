package gen;

import connect.FileSFTP;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Main {

    final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        try {

            String config = System.getProperty("config.file");
            if (StringUtils.isEmpty(System.getProperty("config.file"))) {
                config = "config.properties";
            }

            Properties prop = new Properties();
            prop.load(new FileInputStream(config));
            //custom log file
            if (!StringUtils.isEmpty(prop.getProperty("log.config.file"))){
                PropertyConfigurator.configure(prop.getProperty("log.config.file"));
            }

            logger.info("Load configuration file");
            logger.debug("from "+config);

            new FileSFTP(prop);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }

    private static void usage() {
        System.out.println("Usage command");
        System.out.println("\tjava -Dconfig.file=${config.properties} -jar ${CFR.jar}");
        System.out.println("\tUse -Dconfig.file=${config.properties} to get your config");
        System.out.println("\tUse -jar ${CFR.jar} to get your jarfile to run");
    }

}
