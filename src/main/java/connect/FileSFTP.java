package connect;

import com.jcraft.jsch.*;
import org.apache.log4j.Logger;
import util.Constant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * @author javagists.com
 */
public class FileSFTP {

    final static org.apache.log4j.Logger logger = Logger.getLogger(FileSFTP.class);

    public FileSFTP(Properties prop) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(prop.getProperty("sftp.backup.dateformat"));
        String dateStr = dateFormat.format(new Date());

        Session session = null;
        try {

            session = getSession(prop);
            ChannelSftp sftpChannel = getChanel(session);
            logger.info("Download file..");
            sftpChannel.cd(prop.getProperty("sftp.src.path"));
            String backUpPath = prop.getProperty("sftp.backup.path")+dateStr;
            try {
                sftpChannel.mkdir(backUpPath);
            }catch (Exception e){
                logger.error("Path is exist");
            }

            logger.debug("From : "+sftpChannel.pwd());
            logger.debug("To : "+prop.getProperty("sftp.dest.path"));
            recursiveDL(prop,sftpChannel,backUpPath);
            logger.info("Download file complete!!");
            sftpChannel.exit();

        } catch (JSchException e) {
            e.printStackTrace();
            logger.error(e);
        } catch (SftpException e) {
            e.printStackTrace();
            logger.error(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            logger.info("Disconnect from SFTP");
            logger.debug(session.getHost());
            session.disconnect();
        }

    }

    private void recursiveDL(Properties prop, ChannelSftp sftpChannel, String backUpPath) throws SftpException, InterruptedException {

        Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(prop.getProperty("sftp.ls.path"));

        list.stream().filter(v->!v.getAttrs().isDir()).forEach(v->{
            String fileName = prop.getProperty("sftp.dest.path")+v.getFilename();
                logger.debug(v.getFilename());
                try {
                    sftpChannel.get(v.getFilename(),fileName);
//                if (Boolean.parseBoolean(prop.getProperty("sftp.delete.file"))){
                    sftpChannel.rename(v.getFilename(),backUpPath+"/"+v.getFilename());
                    logger.info("Success Download backup file : "+backUpPath+"/"+v.getFilename());
//                }
                } catch (SftpException e) {
                    logger.error(e);
                }
        });
        Vector<ChannelSftp.LsEntry> numlist = sftpChannel.ls(prop.getProperty("sftp.ls.path"));

        int size = numlist.stream().filter(v->!v.getAttrs().isDir()).collect(Collectors.toList()).size();

        if (size <= 0){
            logger.info("File ls "+prop.getProperty("sftp.ls.path")+" in folder is empty");
            return;
        }else {
            if (Boolean.parseBoolean(prop.getProperty("sftp.recursive.download.file"))) {
                Thread.sleep(1000);
                recursiveDL(prop, sftpChannel, backUpPath);
            }
        }
    }

    private Session getSession(Properties prop) throws JSchException {

        JSch jsch = new JSch();
        logger.debug("Identity by key " + prop.getProperty("sftp.ssh.keyfile") +
                " passphrase is " + prop.getProperty("sftp.ssh.passphrase"));
        jsch.addIdentity(prop.getProperty("sftp.ssh.keyfile"), prop.getProperty("sftp.ssh.passphrase"));
        logger.info("Start Connection to SFTP");

        Session session = jsch.getSession(prop.getProperty("sftp.user")
                , prop.getProperty("sftp.host"), Integer.valueOf(prop.getProperty("sftp.port")));
//        session.setPassword(prop.getProperty("sftp.pass"));

        Properties config = new java.util.Properties();

        config.put("PreferredAuthentications", "publickey,keyboard-interactive,password");
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
//        session.setPassword(prop.getProperty("sftp.password"));

        session.connect();

        logger.info("Connection to SFTP host");
        logger.debug(session.getHost());

        return session;
    }

    private ChannelSftp getChanel(Session session) throws JSchException {

        logger.debug("Session openChannel " + Constant.Session.OPEN_CHANNEL);
        Channel channel = session.openChannel(Constant.Session.OPEN_CHANNEL);
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        logger.debug("OpenChannel complete!!");
        return sftpChannel;
    }

}