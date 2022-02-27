package org.pkfrc.core.utilities.helper;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.logging.Logger;

/**
 * Created by Paul Antoine on 4/12/2021
 */
public class FTPFileHelperProjurise {

    private Logger logger = Logger.getLogger(FTPFileHelperProjurise.class.getName());

    private final String server;
    private final int port;
    private final String userName;
    private final String passWord;
    private final String shareFolder;

    private FTPClient ftpClient;

    public FTPFileHelperProjurise(String server, int port, String userName, String passWord, String shareFolder) {
        super();
        this.server = server;
        this.port = port;
        this.userName = userName;
        this.passWord = passWord;
        this.shareFolder = "/" + (shareFolder == null ? "" : shareFolder.trim());
    }

    public FTPFileHelperProjurise(String server, int port, String userName, String passWord) {
        this(server, port, userName, passWord, "");
    }

    public FTPFileHelperProjurise(String server, String userName, String passWord, String shareFolder) {
        this(server, 21, userName, passWord, shareFolder);
    }

    public FTPFileHelperProjurise(String server, String userName, String passWord) {
        this(server, 21, userName, passWord, "");
    }

    private void connect() throws Exception {
        logger.info("Initialisation de la connexion au serveur " + server);

        ftpClient = new FTPClient();
        ftpClient.connect(server, port);

        int replyCode = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(replyCode)) {
            logger.info("Connect failed.\nHost:" + server + "\nPort:" + port);
            throw new Exception("Connect failed");
        }

        boolean success = ftpClient.login(userName, passWord);
        if (!success) {
            logger.info(
                    "Could not login to the server.\nServer:" + server + "\nPort:" + port + "\nUsername:" + userName);
            // + StringHelper.masquerChaine(passWord, 1, passWord.length() - 1)
            throw new Exception("Could not login to the server");
        }
        logger.info("Connection success");
    }

    public void disconnect() {
        logger.info("Disconnection");
        try {
            if (ftpClient != null && ftpClient.isConnected()) {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param directoryName
     *            file to be upload
     * @throws Exception
     */
    public void uploadDeleteDirectory(String directoryName) throws Exception {
        uploadDirectory(directoryName, true);
    }

    /**
     *
     * @param directoryName
     *            file to be upload
     * @throws Exception
     */
    public void uploadDirectory(String directoryName) throws Exception {
        uploadDirectory(directoryName, false);
    }

    /**
     *
     * @param directoryName
     *            file to be upload
     * @param delete
     * @throws Exception
     */
    public void uploadDirectory(String directoryName, boolean delete) throws Exception {
        connect();

        // Activate the passivate mode in order to avoid the connexion being
        // block by the firewall
        ftpClient.enterLocalPassiveMode();

        // Set the file type to make sure any file will be send
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // Transfert du fichier
        logger.info("Upload of the directory " + directoryName);
        sendDirectory(ftpClient, shareFolder, directoryName);
        if (delete) {
            deleteLocaleFile(new File(directoryName));
        }
        disconnect();
    }

    public boolean makeDirectory(String newDirectory) throws Exception {
        connect();

        String remoteDir = StringHelper.isEmpty(newDirectory) ? shareFolder : shareFolder + "/" + newDirectory;

        // Activate the passivate mode in order to avoid the connexion being
        // block by the firewall
        ftpClient.enterLocalPassiveMode();

        // Set the file type to make sure any file will be send

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        boolean done = false;
        if (remoteDir != null) {
            done = ftpClient.makeDirectory(remoteDir);
        }

        logger.info("Creation of the directory " + remoteDir);
        if (done) {
            logger.info("Directory creation success " + remoteDir);
        }
        if (!done) {
            logger.info("Directory creation failure " + remoteDir);
        }
        disconnect();
        return done;
    }

    private void sendDirectory(FTPClient ftpClient, String remoteParentDir, String localDirectoryName)
            throws Exception {
        File localDir = new File(localDirectoryName);

        remoteParentDir = (StringHelper.isEmpty(remoteParentDir) ? shareFolder : remoteParentDir) + "/"
                + localDir.getName();
        // if (StringHelper.isEmpty(remoteParentDir)) {
        // remoteParentDir = shareFolder + "/" + localDir.getName();
        // } else {
        // remoteParentDir = remoteParentDir + "/" + localDir.getName();
        // }
        logger.info("Creation of the directory " + remoteParentDir);
        boolean done = ftpClient.makeDirectory(remoteParentDir);
        if (done) {
            logger.info("Directory " + remoteParentDir + " created");
        } else {
            logger.info("Could not create directory " + remoteParentDir);
        }

        File[] subFiles = localDir.listFiles();
        if (subFiles == null || subFiles.length == 0) {
            return;
        }

        for (File item : subFiles) {
            String fileName = item.getAbsolutePath();
            if (item.isFile()) {
                // Upload of the file
                logger.info("Upload of the file " + fileName);
                if (sendFile(ftpClient, remoteParentDir, fileName)) {
                    logger.info("File " + fileName + " uploaded");
                } else {
                    logger.info("Could not upload file " + fileName);
                }
            } else {
                sendDirectory(ftpClient, remoteParentDir, fileName);
            }
        }
    }

    /**
     *
     * @param fileName
     *            file to be upload
     * @return
     * @throws Exception
     */
    public void uploadDeleteFile(String fileName) throws Exception {
        uploadFile(fileName, true);
    }

    /**
     *
     * @param fileName
     *            file to be upload
     * @return
     * @throws Exception
     */
    public void uploadFile(String fileName) throws Exception {
        uploadFile(fileName, false);
    }

    /**
     *
     * @param fileName
     *            file to be upload
     * @return
     * @throws Exception
     */
    private void uploadFile(String fileName, boolean delete) throws Exception {
        connect();

        // Activate the passivate mode in order to avoid the connexion being
        // block by the firewall
        ftpClient.enterLocalPassiveMode();

        // Set the file type to make sure any file will be send
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // Transfert du fichier
        logger.info("Upload of the file " + fileName);
        if (sendFile(ftpClient, shareFolder, fileName)) {
            logger.info("File " + fileName + " uploaded");
            if (delete) {
                deleteLocaleFile(new File(fileName));
            }
        }

        disconnect();
    }

    private boolean sendFile(FTPClient ftpClient, String shareFolder, String fileName) throws Exception {
        File localFile = new File(fileName);

        String remoteFile = shareFolder + "/" + localFile.getName();
        InputStream inputStream = new FileInputStream(localFile);

        try {
            return ftpClient.storeFile(remoteFile, inputStream);
        } finally {
            inputStream.close();
        }
    }

    private void deleteLocaleFile(File file) {
        if (file.isDirectory()) {
            File[] items = file.listFiles();
            for (File item : items) {
                deleteLocaleFile(item);
            }
        }
        file.delete();
    }

    /**
     * @param localFolder
     * @param remoteFile
     *            file to be upload
     * @return
     * @throws Exception
     */
    public void downloadDeleteDirectory(String localFolder, String remoteFile) throws Exception {
        downloadDirectory(localFolder, remoteFile, true);
    }

    /**
     * @param localFolder
     * @param remoteFile
     *            file to be upload
     * @param deleteParent
     * @return
     * @throws Exception
     */
    public void downloadDeleteDirectory(String localFolder, String remoteFile, boolean deleteParent) throws Exception {
        downloadDirectory(localFolder, remoteFile, true, deleteParent);
    }

    /**
     * @param localFolder
     * @param remoteFile
     *            file to be upload
     * @return
     * @throws Exception
     */
    public void downloadDirectory(String localFolder, String remoteFile) throws Exception {
        downloadDirectory(localFolder, remoteFile, false);
    }

    /**
     * @param localFolder
     * @param remoteFile
     *            file to be upload
     * @param deleteParent
     * @return
     * @throws Exception
     */
    public void downloadDirectory(String localFolder, String remoteFile, boolean deleteParent) throws Exception {
        downloadDirectory(localFolder, remoteFile, false, deleteParent);
    }

    /**
     * @param localFolder
     * @param remoteFile
     *            file to be upload
     * @param delete
     * @param deleteParent
     * @return
     * @throws Exception
     */
    private void downloadDirectory(String localFolder, String remoteFile, boolean delete, boolean deleteParent)
            throws Exception {
        connect();

        // Activate the passivate mode in order to avoid the connexion being
        // block by the firewall
        ftpClient.enterLocalPassiveMode();

        // Set the file type to make sure any file will be send
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // Transfert du fichier
        logger.info("Downlaod of the file " + remoteFile);
        retreiveDirectory(ftpClient, localFolder, remoteFile);
        logger.info("File " + remoteFile + " downlaoded");
        if (delete) {
            deleteRemoteDirectory(ftpClient, remoteFile, deleteParent);
        }
        disconnect();
    }

    private void retreiveDirectory(FTPClient ftpClient, String localFolder, String remoteDirectory) throws Exception {
        // Je cr�e le r�pertoire en question en local
        localFolder = localFolder + File.separator + buildFileName(remoteDirectory);
        File localFile = new File(localFolder);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }

        FTPFile[] subFiles = ftpClient.listFiles(remoteDirectory);
        if (subFiles != null && subFiles.length != 0) {
            // Process the subfiles
            String fileName;
            String toBeDownloaded;
            for (FTPFile item : subFiles) {
                fileName = item.getName();
                if (fileName.equals(".") || fileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                toBeDownloaded = remoteDirectory + "/" + fileName;
                if (item.isDirectory()) {
                    // Download the remove the subdirectory
                    retreiveDirectory(ftpClient, localFolder, toBeDownloaded);
                } else {
                    // Download the file
                    retreiveFile(ftpClient, localFolder, toBeDownloaded);
                }
            }
        }
    }

    /**
     * @param localFolder
     * @param remoteFile
     *            file to be upload
     * @return
     * @throws Exception
     */
    public void downloadDeleteFile(String localFolder, String remoteFile) throws Exception {
        downloadFile(localFolder, remoteFile, true);
    }

    /**
     * @param localFolder
     * @param remoteFile
     *            file to be upload
     * @return
     * @throws Exception
     */
    public void downloadFile(String localFolder, String remoteFile) throws Exception {
        downloadFile(localFolder, remoteFile, false);
    }

    /**
     * @param localFolder
     * @param remoteFile
     *            file to be upload
     * @param delete
     * @return
     * @throws Exception
     */
    private void downloadFile(String localFolder, String remoteFile, boolean delete) throws Exception {
        connect();

        // Activate the passivate mode in order to avoid the connexion being
        // block by the firewall
        ftpClient.enterLocalPassiveMode();

        // Set the file type to make sure any file will be send
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        // Transfert du fichier
        logger.info("Downlaod of the file " + remoteFile);
        if (retreiveFile(ftpClient, localFolder, remoteFile)) {
            logger.info("File " + remoteFile + " downlaoded");
            if (delete) {
                deleteRemoteFile(ftpClient, remoteFile);
            }
        }

        disconnect();
    }

    private boolean retreiveFile(FTPClient ftpClient, String localFolder, String remoteFile) throws Exception {
        File localFile = new File(localFolder);
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
        localFolder = localFolder + File.separator + buildFileName(remoteFile);
        File file = new File(localFolder);
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
        if (StringHelper.isNotEmpty(shareFolder)) {
            remoteFile = shareFolder + File.separator + remoteFile;
        }
        try {
            return ftpClient.retrieveFile(remoteFile, outputStream);
        } finally {
            outputStream.close();
        }
    }

    private String buildFileName(String fileName) {
        String[] count = fileName.split("/");
        return count[count.length - 1];
    }

    /**
     * @param directoryName
     * @return
     * @throws Exception
     */
    public void deleteRemoteDirectory(String directoryName) throws Exception {
        deleteRemoteDirectory(ftpClient, directoryName, false);
    }

    /**
     * @param directoryName
     * @param deleteParent
     * @return
     * @throws Exception
     */
    public void deleteRemoteDirectory(String directoryName, boolean deleteParent) throws Exception {
        connect();

        // Activate the passivate mode in order to avoid the connexion being
        // block by the firewall
        ftpClient.enterLocalPassiveMode();

        deleteRemoteDirectory(ftpClient, directoryName, deleteParent);

        disconnect();
    }

    private void deleteRemoteDirectory(FTPClient ftpClient, String directoryName, boolean deleteParent)
            throws Exception {
        FTPFile[] subFiles = ftpClient.listFiles(directoryName);
        if (subFiles != null && subFiles.length != 0) {
            // Process the subfiles
            String fileName;
            String toBeRemoved;
            for (FTPFile item : subFiles) {
                fileName = item.getName();
                if (fileName.equals(".") || fileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                toBeRemoved = directoryName + "/" + fileName;
                if (item.isDirectory()) {
                    // Remove the subdirectory
                    deleteRemoteDirectory(ftpClient, toBeRemoved, true);
                } else {
                    // Delete de file
                    ftpClient.deleteFile(toBeRemoved);
                }
            }
        }
        if (deleteParent) {
            ftpClient.removeDirectory(directoryName);
        }
    }

    /**
     * @param fileName
     * @return
     * @throws Exception
     */
    public void deleteRemoteFile(String fileName) throws Exception {
        connect();

        // Activate the passivate mode in order to avoid the connexion being
        // block by the firewall
        ftpClient.enterLocalPassiveMode();

        deleteRemoteFile(ftpClient, fileName);

        disconnect();
    }

    private void deleteRemoteFile(FTPClient ftpClient, String fileName) throws Exception {
        // Delete de file
        ftpClient.deleteFile(fileName);
    }

//	public static void main(String[] args) {
//
//		FTPFileHelper ftp = new FTPFileHelper("localhost", 21, "projurise", "123456789", "2");
//		try {
//
//			// ftp.uploadFile("D:\\localStorage\\4\\file004.xls");
//			ftp.downloadFile("D:\\localStorage", "filesssssssssssss.xls");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}