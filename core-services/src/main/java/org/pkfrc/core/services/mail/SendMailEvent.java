package org.pkfrc.core.services.mail;

import org.springframework.context.ApplicationEvent;

public class SendMailEvent extends ApplicationEvent {

  String recipient;
  String subject;
  String content;

  public SendMailEvent(Object source, String recipient, String subject, String content) {
    super(source);
    this.recipient = recipient;
    this.subject = subject;
    this.content = content;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setRecipient(String recipient) {
    this.recipient = recipient;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
