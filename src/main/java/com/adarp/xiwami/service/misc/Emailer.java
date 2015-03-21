package com.adarp.xiwami.service.misc;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.adarp.xiwami.domain.Email;


public class Emailer{

  private String sentFrom;
  private List<String> sentTo;
  private String subject;
  private String emailText;
	
  public Emailer(Email email) {
	  this.sentFrom = email.getSentFrom();
	  this.sentTo = email.getSentTo();
	  this.subject = email.getSubject();
	  this.emailText = email.getEmailText();
  }

  private static final String SMTP_HOST_NAME = "smtp.sendgrid.net";
  private static final String SMTP_AUTH_USER = "kuentingshiu";
  private static final String SMTP_AUTH_PWD = "siwami1234";
  private static final int SMTP_PORT = 2525;

  public void send() throws Exception {
    Properties props = new Properties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");

    Authenticator auth = new SMTPAuthenticator();
    Session mailSession = Session.getDefaultInstance(props, auth);

    MimeMessage message = new MimeMessage(mailSession);
    Multipart multipart = new MimeMultipart();

    // Sets up the contents of the email message
    MimeBodyPart part1 = new MimeBodyPart();
    part1.setText(emailText, "utf-8");
/*    MimeBodyPart part2 = new MimeBodyPart();
    part2.setContent(
    	    "<p>Hello,</p>" 
    	    + "<p>Your Siwami order has <b>shipped</b>.</p>"
    	    + "<p>Thank you,<br>Siwami.com</br></p>",
    	    "text/html; charset=utf-8");*/
    multipart.addBodyPart(part1);
 //   multipart.addBodyPart(part2);
    message.setContent(multipart);
    message.setFrom(new InternetAddress(sentFrom));
    message.setSubject(subject);
    for (String recipent : sentTo) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipent));
    }


    // Sends the email
    Transport transport = mailSession.getTransport();
    transport.connect(SMTP_HOST_NAME, SMTP_PORT, SMTP_AUTH_USER, SMTP_AUTH_PWD);
    transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
    transport.close();
  }

  // Authenticates to SendGrid
  private class SMTPAuthenticator extends javax.mail.Authenticator {
    @Override
    public PasswordAuthentication getPasswordAuthentication() {
      String username = SMTP_AUTH_USER;
      String password = SMTP_AUTH_PWD;
      return new PasswordAuthentication(username, password);
    }
  }
}
