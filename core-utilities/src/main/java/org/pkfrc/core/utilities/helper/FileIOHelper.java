package org.pkfrc.core.utilities.helper;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileIOHelper {
	static Logger logger = LoggerFactory.getLogger(FileIOHelper.class);

	private static final String SEPERATOR = "/";

	public static boolean fileExists(String path) {
		File f = new File(path);
		return f.exists() && !f.isDirectory();
	}

	public static void mkDirs(File root, List<String> dirs, int depth) {
		if (depth == 0)
			return;
		for (String s : dirs) {
			File subdir = new File(root, s);
			subdir.mkdir();
			mkDirs(subdir, dirs, depth - 1);
		}
	}

	public static void deleteFile(String file) {
		FileUtils.deleteQuietly(new File(file));
	}

	public static void writeToDisk(InputStream input, String directory, String filename) throws IOException {

		logger.info("CREATING DIRECTORY : " + directory);
		File root = new File(directory);
		FileUtils.forceMkdir(root);
		boolean result = root.mkdir();
		logger.info("DIRECTION CREATION RESULT : " + result);
		OutputStream output = new FileOutputStream(new File(directory, filename));
		try {

			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	public static void writeToDisk(BufferedImage input, String directory, String type, String name) throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(input, type, os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		writeToDisk(is, directory, name + "." + type);

	}

	public static InputStream readFileFromDisk(String filename) throws IOException {
		InputStream input = new FileInputStream(new File(filename));
		return input;

	}

	public static boolean deleteDirectory(File dir) {
		if (dir.isDirectory()) {
			File[] children = dir.listFiles();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(children[i]);
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static void tranferFile(String servername, int port, String username, String password,
			List<String> filenames) {

		FTPClient ftpClient = null;
		try {
			ftpClient = ftpConnect(servername, port, username, password);
			for (String file : filenames) {

				File sourceFile = new File(file);
				InputStream inputStream = new FileInputStream(sourceFile);

				ftpClient.storeFile(sourceFile.getName(), inputStream);

				inputStream.close();
			}

		} catch (IOException e) {
		} finally {
			try {
				ftpDisconnect(ftpClient);
			} catch (IOException e) {
				System.out.println("Exception occured while ftp logout/disconnect : " + e);
			}
		}

	}

	public static void tranferFile(String servername, int port, String username, String password, String dirTree,
			String filename) {
		// 14147 bankcareer
		FTPClient ftpClient = null;
		try {
			ftpClient = ftpConnect(servername, port, username, password);

			ftpCreateDirectoryTree(ftpClient, dirTree);

			File sourceFile = new File(filename);
			InputStream inputStream = new FileInputStream(sourceFile);

			ftpClient.storeFile(sourceFile.getName(), inputStream);
			inputStream.close();

		} catch (IOException e) {
		} finally {
			try {
				ftpDisconnect(ftpClient);
			} catch (IOException e) {
				System.out.println("Exception occured while ftp logout/disconnect : " + e);
			}
		}
	}

	private static FTPClient ftpConnect(String servername, int port, String username, String password)
			throws SocketException, IOException		{

		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(servername, port);
		ftpClient.login(username, password);
		ftpClient.enterLocalPassiveMode();

		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

		return ftpClient;
	}

	private static void ftpDisconnect(FTPClient ftpClient) throws IOException {
		if (ftpClient != null && ftpClient.isConnected()) {
			ftpClient.logout();
			ftpClient.disconnect();
		}
	}

	private static void ftpCreateDirectoryTree(FTPClient ftpClient, String dirTree) throws IOException {
		String[] directories = dirTree.split("/");

		boolean dirExists = true;
		for (String dir : directories) {
			if (!dir.isEmpty()) {
				if (dirExists) {
					dirExists = ftpClient.changeWorkingDirectory(dir);
				}
				if (!dirExists) {
					if (!ftpClient.makeDirectory(dir)) {
						throw new IOException("Unable to create remote directory '" + dir + "'.  error='"
								+ ftpClient.getReplyString() + "'");
					}
					if (!ftpClient.changeWorkingDirectory(dir)) {
						throw new IOException("Unable to change into newly created remote directory '" + dir
								+ "'.  error='" + ftpClient.getReplyString() + "'");
					}
				}
			}
		}
	}

	/**
	 *
	 * @param servername
	 * @param port
	 * @param username
	 * @param password
	 * @param localFolder
	 * @param remoteFile
	 * @return
	 * @throws Exception
	 */
	public static String downloadFile(String servername, int port, String username, String password, String localFolder,
			String remoteFile) throws Exception {
		FTPClient ftpClient = null;
		String file = null;
		try {
			ftpClient = ftpConnect(servername, port, username, password);

			// Activate the passivate mode in order to avoid the connexion being
			// block by the firewall
			ftpClient.enterLocalPassiveMode();
			// Set the file type to make sure any file will be send
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// Transfert du fichier
			file = retreiveFile(ftpClient, localFolder, remoteFile);
		} catch (IOException e) {
			System.out.println("Exception occured while ftp logout/disconnect : " + e);
		} finally {
			try {
				ftpDisconnect(ftpClient);
			} catch (IOException e) {
				System.out.println("Exception occured while ftp logout/disconnect : " + e);
			}
		}
		return file;

	}

	private static String retreiveFile(FTPClient ftpClient, String localFolder, String remoteFile) throws Exception {
		File localFile = new File(localFolder);
		if (!localFile.exists()) {
			localFile.mkdirs();
		}

		// Build the nom of the file in local;
		localFolder = localFolder + SEPERATOR + buildFileName(remoteFile);
		File file = new File(localFolder);
		OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

		try {
			ftpClient.retrieveFile(remoteFile, outputStream);
		} finally {
			outputStream.close();
		}
		return localFolder;
	}

	public static String buildFileName(String fileName) {
		String[] count = fileName.split(SEPERATOR);
		return count[count.length - 1];
	}

	public static void deleteRemoteFile(String servername, int port, String username, String password, String fileName)
			throws Exception {
		FTPClient ftpClient = null;
		try {
			ftpClient = ftpConnect(servername, port, username, password);

			// Activate the passivate mode in order to avoid the connexion being
			// block by the firewall
			ftpClient.enterLocalPassiveMode();
			// Set the file type to make sure any file will be send
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			// Transfert du fichier
			ftpClient.deleteFile(fileName);
		} catch (IOException e) {
		} finally {
			try {
				ftpDisconnect(ftpClient);
			} catch (IOException e) {
				System.out.println("Exception occured while ftp logout/disconnect : " + e);
			}
		}
	}

	public static final String ENCODAGE = "UTF-8";

	public static List<String> getLignesFichier(String nom, String encodage) throws Exception {
		File fichier = new File(nom);
		List<String> lines = FileUtils.readLines(fichier, encodage);
		return lines;
	}

	public static List<String> getLignesFichier(String nom) throws Exception {
		return getLignesFichier(nom, ENCODAGE);
	}

	public static List<String[]> getTableauFichier(String nom, String sep, String encodage) throws Exception {
		List<String> lines = getLignesFichier(nom, encodage);
		List<String[]> tableau = new ArrayList<>(0);
		for (String line : lines) {
			tableau.add(line.split(sep));
		}
		return tableau;
	}

	public static List<String[]> getTableauFichier(String nom, String sep) throws Exception {
		return getTableauFichier(nom, sep, ENCODAGE);
	}

/*	public static Workbook getExcelSheet(boolean xlsx, List<String> headers) {
		Workbook workbook;
		Sheet sheet;
		if (!xlsx) {
			workbook = new HSSFWorkbook();
		} else {
			workbook = new XSSFWorkbook();
		}
		sheet = workbook.createSheet();

		Row row = sheet.createRow(0);
		int iter = 0;
		for (String header : headers) {
			row.createCell(iter, Cell.CELL_TYPE_STRING);
			row.getCell(iter).setCellValue(header);
			iter++;
		}

		return workbook;
	}*/
/*
	public static List<String[]> getExcelColomns(String nom) throws Exception {
		FileInputStream excelFile = new FileInputStream(new File(nom));

		Workbook workbook;
		if (FilenameUtils.getExtension(nom).equalsIgnoreCase("xls")) {
			workbook = new HSSFWorkbook(excelFile);
		} else {
			workbook = new XSSFWorkbook(excelFile);
		}
		Sheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();

		List<String[]> tableau = new ArrayList<>(0);

		while (iterator.hasNext()) {

			Row currentRow = iterator.next();
			Iterator<Cell> cellIterator = currentRow.iterator();

			List<String> line = new ArrayList<>(0);
			while (cellIterator.hasNext()) {

				Cell cell = cellIterator.next();
				line.add(getCellValue(cell).toString());
				// getCellTypeEnum shown as deprecated for version 3.15
				// getCellTypeEnum ill be renamed to getCellType starting from version 4.0
				// if (currentCell.getCellType() == CellFormatType.TEXT) {
				// System.out.print(currentCell.getStringCellValue() + "--");
				// } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
				// System.out.print(currentCell.getNumericCellValue() + "--");
				// }

			}
			tableau.add(line.toArray(new String[line.size()]));
			System.out.println();

		}
		return tableau;
	}*/
/*

	private static Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case Cell.CELL_TYPE_BLANK:
			return cell.getDateCellValue();
		case Cell.CELL_TYPE_ERROR:
			return cell.getErrorCellValue();
		case Cell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		default:
			return cell.getDateCellValue();
		}
	}
*/

}
