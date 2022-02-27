package org.pkfrc.core.utilities.helper;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailHelper {

	private static final String IMAP_PROTOCOL = "imap";
	private static final String FOLDER_NAME = "Ebanking";

	private String host;
	private String port;
	private String username;
	private String password;
	private Session session;
	private Address sender;

	public MailHelper() {
	}

	public MailHelper(String host, String port, String username, String password) {
		// new MailHelper("mail.papamail.net", "587",
		// "afrilandfirstbank.ci@afrilandfirstbankci.com", "2017afbci");
		this.host = host.trim(); // "mail.papamail.net"; //
		this.port = port.trim(); // "587"; //
		this.username = username.trim(); // "afrilandfirstbank.ci@afrilandfirstbankci.com";//
//		this.username = "digital-notification@afrilandfirstbank.com";
		this.password = password.trim(); // "2017afbci";//
//		this.password = "AfbFin18";
		this.session = getSession();
		try {
			this.sender = (Address) new InternetAddress(username);
		} catch (AddressException e) {
			this.sender = null;
		}
	}

	public void sendMail(String recepient, String subject, String content) {
		sendMail(recepient, null, subject, content);
	}

	public void sendMail(String recepient, String copyTo, String subject, String content) {
		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(sender);
			message.setSender(sender);

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepient.trim()));

			if (copyTo != null && copyTo.trim().isEmpty()) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(copyTo.trim()));
			}
			// Add the subject
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setContent(content, "text/html; charset=utf-8");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Add the content to the message
			message.setContent(multipart);

			// Envoi du message
			sendMessage(message);
		} catch (MessagingException e) {
			throw new RuntimeException((Throwable) e);
		} catch (Exception e) {
			throw new RuntimeException((Throwable) e);
		}
	}

	public void sendMail(List<String> recepients, String subject, String content) {
		sendMail(recepients, null, subject, content);
	}

	public void sendMail(List<String> recepients, List<String> copiesTo, String subject, String content) {
		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(sender);
			message.setSender(sender);

			for (String recipient : recepients) {
				if (recipient==null) {
					recepients.remove(recipient);
				}				
			}
			
			for (String recipient : recepients) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.trim()));		
			}

			if (copiesTo != null && !copiesTo.isEmpty()) {
				for (String copyTo : copiesTo) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(copyTo.trim()));
				}
			}
			// Add the subject
			message.setSubject(subject);
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setContent(content, "text/html; charset=utf-8");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Add the content to the message
			message.setContent(multipart);

			// Envoi du message
			sendMessage(message);
		} catch (MessagingException e) {
			throw new RuntimeException((Throwable) e);
		} catch (Exception e) {
			throw new RuntimeException((Throwable) e);
		}
	}

	public void sendMailWithAttachment(String recepientEmail, String subject, String content, File file) {
		sendMailWithAttachment(recepientEmail, null, subject, content, file);
	}

	public void sendMailWithAttachment(String recepientEmail, String copyTo, String subject, String content,
			File file) {
		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(sender);
			message.setSender(sender);

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recepientEmail));

			if (copyTo != null && copyTo.trim().isEmpty()) {
				message.addRecipient(Message.RecipientType.CC, new InternetAddress(copyTo.trim()));
			}

			// Add the subject
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setContent(content, "text/html; charset=utf-8");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Add attachment if necessary
			if (file != null) {
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(file);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(file.getName().split("\\.")[0]);
				multipart.addBodyPart(messageBodyPart);
			}

			// Add the content to the message
			message.setContent(multipart);

			// Envoi du message
			sendMessage(message);
		} catch (MessagingException e) {
			throw new RuntimeException((Throwable) e);
		} catch (Exception e) {
			throw new RuntimeException((Throwable) e);
		}
	}

	public void sendMailWithAttachment(List<String> recepients, String subject, String content, List<File> files) {
		sendMailWithAttachment(recepients, null, subject, content, files);
	}

	public void sendMailWithAttachment(List<String> recepients, List<String> copiesTo, String subject, String content,
			List<File> files) {
		try {
			MimeMessage message = new MimeMessage(session);

			message.setFrom(sender);
			message.setSender(sender);

			for (String recipient : recepients) {
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient.trim()));
			}

			if (copiesTo != null && !copiesTo.isEmpty()) {
				for (String copyTo : copiesTo) {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(copyTo.trim()));
				}
			}

			// Add the subject
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setContent(content, "text/html; charset=utf-8");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Add attachment if necessary
			for (File file : files) {
				messageBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(file);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(file.getName().split("\\.")[0]);
				multipart.addBodyPart(messageBodyPart);
			}

			// Add the content to the message
			message.setContent(multipart);

			// Envoi du message
			sendMessage(message);
		} catch (MessagingException e) {
			throw new RuntimeException((Throwable) e);
		} catch (Exception e) {
			throw new RuntimeException((Throwable) e);
		}
	}

	private Session getSession() {
		Properties properties = System.getProperties();

		properties.put("mail.smtp.host", this.host);
		properties.put("mail.smtp.port", this.port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.starttls.required", "true");
		properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		Session session = Session.getDefaultInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		session.setDebug(false);		
		return session;
	}

	private void sendMessage(MimeMessage message) throws Exception {

		// Initialisation de la session avec le provider
		Transport transport = session.getTransport("smtp");
		
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		
		
        message.setFrom(new InternetAddress("no-reply@gmail.com"));      
        

		// Connection sur le host
		transport.connect(host, username, password);

//		 transport.connect(host, Integer.parseInt(port), username, password);

		// Envoi du message
		transport.sendMessage(message, message.getAllRecipients());

		// Fermeture
		transport.close();

		copyToSendFolder(message);
	}

	private void copyToSendFolder(MimeMessage message) throws Exception {

		// Copy message to "Sent Items" folder as read
		Store store = session.getStore(IMAP_PROTOCOL);

		store.connect(host, username, password);
		Folder folder = store.getFolder(FOLDER_NAME);
		folder.open(Folder.READ_WRITE);
		message.setFlag(Flag.SEEN, false);
		folder.appendMessages(new Message[] { message });
		store.close();
	}

//	public static void main(String args[]) {
//
//		try {
//			// MailHelper helper = new MailHelper("smtp.mail.yahoo.com", "587",
//			// "ebankingafbci@yahoo.com", "mailci2016");
//			MailHelper helper = new MailHelper("mail.papamail.net", "587",
//					"afrilandfirstbank.ci@afrilandfirstbankci.com", "2017afbci");
//			helper.sendMail("isaac_kotto@afrilandfirstbank.com", "Pour le Test", "Pour le Test");
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

}
