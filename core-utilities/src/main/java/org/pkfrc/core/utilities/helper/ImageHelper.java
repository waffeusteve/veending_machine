//package org.pkfrc.core.utilities.helper;
//
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.RenderingHints;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//
//import sun.misc.BASE64Encoder;
//import sun.misc.BASE64Decoder;
//
//import javax.imageio.ImageIO;
//
//public class ImageHelper {
//
////	public static void main(String[] args) {
////		try {
////			// URL url = new URL("http://www.google.com/intl/en_ALL/images/logo.gif");
////			InputStream input = new FileInputStream(
////					new File("D:\\photos\\20180720\\DCIM\\sag\\2018-07-20_20-14-30.jpg"));
////			BufferedImage image = ImageIO.read(input);
////			String type = "jpg";
////			String imageString = encodeToString(image, type);
////			System.out.println(imageString.replaceAll("\n", ""));
////			// write("D:\\test", type, "logo200W", decodeToImage(imageString,
////			// "D:\\test\\logo.gif"));
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////	}
//
//	public static void write(String path, String type, String name, int width, int height, String imageBase64)
//			throws Exception {
//		BufferedImage resizedImage = (BufferedImage) scaleImage(decodeToImage(imageBase64), width, height);
//		FileIOHelper.writeToDisk(resizedImage, path, type, name);
//	}
//
//	public static void write(String path, String type, String name, int width, int height, BufferedImage image)
//			throws Exception {
//		BufferedImage resizedImage = (BufferedImage) scaleImage(image, width, height);
//		FileIOHelper.writeToDisk(resizedImage, path, type, name);
//	}
//
//	public static BufferedImage decodeToImage(String imageBase64, String pathWatermark) throws Exception {
//		BufferedImage image = decodeToImage(imageBase64);
////		return image;
//		return addWatermarkToImage(image, pathWatermark);
//	}
//
//	private static BufferedImage addWatermarkToImage(BufferedImage image, String pathWatermark) throws Exception {
//
//		BufferedImage watermark = ImageIO.read(new File(pathWatermark));
//
//		BufferedImage combined = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
//
//		// paint both images, preserving the alpha channels
//		Graphics graphics = combined.getGraphics();
//		graphics.drawImage(image, 0, 0, null);
//
//		// adds water math to bottom left
//		graphics.drawImage(watermark, image.getWidth() - watermark.getWidth(),
//				image.getHeight() - watermark.getHeight(), null);
//
//		graphics.dispose();
//
//		return combined;
//	}
//
//	public static BufferedImage decodeToImage(String imageBase64) throws Exception {
//
//		BufferedImage image = null;
//		byte[] imageByte;
//		BASE64Decoder decoder = new BASE64Decoder();
//		imageByte = decoder.decodeBuffer(imageBase64);
//		ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
//		image = ImageIO.read(bis);
//		bis.close();
//		return image;
//	}
//
//	public static void write(String path, String type, String name, BufferedImage image) throws Exception {
//		FileIOHelper.writeToDisk(image, path, type, name);
//	}
//
//	public static void write(String path, String type, String name, String imageBase64) throws Exception {
//		FileIOHelper.writeToDisk(decodeToImage(imageBase64), path, type, name);
//	}
//
//	public static String encodeToString(BufferedImage image, String type) {
//		String imageString = null;
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//		try {
//			ImageIO.write(image, type, bos);
//			byte[] imageBytes = bos.toByteArray();
//
//			BASE64Encoder encoder = new BASE64Encoder();
//			imageString = encoder.encode(imageBytes);
//
//			bos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return imageString;
//	}
//
//	/**
//	 * Permet de lire un fichier stocker sur disque et d'en retourner une image
//	 * 
//	 * @param path
//	 *            Nom complet du fichier image que l'on veut lire
//	 * @return Une image
//	 * @throws Exception
//	 */
//	public static Image read(String path) throws Exception {
//		File file = new File(path);
//		if (!file.exists()) {
//			// Fichier inexistant
//			throw new Exception("Fichier inexistant");
//		}
//		return ImageIO.read(file);
//	}
//
//	/**
//	 * Permet de lire un fichier stocker sur disque et d'en retourner un buffer
//	 * 
//	 * @param path
//	 *            Nom complet du fichier image que l'on veut lire
//	 * @return Un buffer de l'image
//	 * @throws Exception
//	 */
//	public static BufferedImage readBuffer(String path) throws Exception {
//		File file = new File(path);
//		if (!file.exists()) {
//			// Fichier inexistant
//			throw new Exception("Fichier inexistant");
//		}
//		return ImageIO.read(file);
//	}
//
//	public static Image write(String path, Image image) throws Exception {
//		File file = new File(path);
//		return ImageIO.read(file);
//	}
//
//	public static Image write(String path, BufferedImage image) throws Exception {
//		File file = new File(path);
//		return ImageIO.read(file);
//	}
//
//	public static byte[] getByte(Image image) throws Exception {
//		return (getByte((BufferedImage) image));
//	}
//
//	public static byte[] getByte(BufferedImage buffer) throws Exception {
//		byte[] bytes = new byte[0];
//		if (buffer != null) {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ImageIO.write(buffer, "jpg", baos);
//			baos.flush();
//			bytes = baos.toByteArray();
//			baos.close();
//		}
//		return bytes;
//	}
//
//	public static Image getImage(byte[] bytes) throws Exception {
//		InputStream in = new ByteArrayInputStream(bytes);
//		return ImageIO.read(in);
//	}
//
//	public static Image scaleImage(Image source, int width, int height) {
//		if (source == null) {
//			return null;
//		}
//		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
//		Graphics2D g = (Graphics2D) img.getGraphics();
//		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//		g.drawImage(source, 0, 0, width, height, null);
//		g.dispose();
//		return img;
//	}
//}
