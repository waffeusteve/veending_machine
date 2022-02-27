package org.pkfrc.core.services.mail;

import org.pkfrc.core.entities.config.ServerConfig;
import org.pkfrc.core.repo.config.ServerConfigRepository;
import org.pkfrc.core.utilities.exceptions.SmartTechException;
import org.pkfrc.core.utilities.helper.MailHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailSenderImpl implements IMailSender, ApplicationListener<SendMailEvent> {


    @Autowired
    ServerConfigRepository configRepository;


    private JavaMailSenderImpl getMailSender() {
        ServerConfig config = configRepository.getMailServerConfig();
        if (config == null) {
            throw new SmartTechException("Mail.Config.Not.Configured");
        }
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(config.getHost());
        mailSender.setPort(config.getPort());
        mailSender.setUsername(config.getUsername());
        mailSender.setPassword(config.getPassword());
        return mailSender;
    }

    @Async
    @Override
    public void sendMail(String recipient, String subject, String content) {
        ServerConfig config = configRepository.getMailServerConfig();
        if (config == null) {
            throw new SmartTechException("Mail.Config.Not.Configured");
        }
        MailHelper helper = new MailHelper(config.getHost(), String.valueOf(config.getPort()),
                config.getUsername(), config.getPassword());

        helper.sendMail(recipient, subject, content);
    }

    @Override
    public void onApplicationEvent(SendMailEvent event) {
        sendMail(event.getRecipient(), event.getSubject(), event.getContent());
    }

}
