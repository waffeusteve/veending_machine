package org.pkfrc.core.services.mail;

public interface IMailSender {
  void sendMail(String recipient, String subject, String content);
}
