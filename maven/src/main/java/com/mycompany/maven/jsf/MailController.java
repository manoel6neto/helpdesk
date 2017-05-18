package com.mycompany.maven.jsf;

import java.io.Serializable;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author Manoel
 */
@Named
@ApplicationScoped
public class MailController implements Serializable {

    @Resource(lookup = "mail/maven_web")
    //private Session mailSession;

    public void sendEmail(String to, String subject, String body) {

//        MimeMessage message = new MimeMessage(mailSession);
//        try {
//            message.setFrom(new InternetAddress(mailSession.getProperty("mail.from"), Utils.SYSTEM_NAME));
//        } catch (UnsupportedEncodingException ex) {
//            message.setFrom(new InternetAddress(mailSession.getProperty("mail.from")));
//        }
//
//        message.setRecipients(Message.RecipientType.TO, to);
//        message.setSubject(subject);
//        message.setSentDate(new Date());
//        message.setContent(body.toString(), "text/html; charset=utf-8");
//
//        Transport.send(message);
    }
}
