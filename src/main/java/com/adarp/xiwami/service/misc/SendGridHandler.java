package com.adarp.xiwami.service.misc;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SendGridHandler extends HttpServlet {

  private String sentFrom;
  private String sentTo;
  private String subject;
  private String emailText;
	
  public SendGridHandler(String sentFrom,String sentTo,String subject,String emailText) {
	  this.sentFrom = sentFrom;
	  this.sentTo = sentTo;
	  this.subject = subject;
	  this.emailText = emailText;
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
    Transport transport = mailSession.getTransport();

    MimeMessage message = new MimeMessage(mailSession);

    Multipart multipart = new MimeMultipart("alternative");

    // Sets up the contents of the email message
    BodyPart part1 = new MimeBodyPart();
    part1.setText(emailText);

    multipart.addBodyPart(part1);

    message.setContent(multipart);
    message.setFrom(new InternetAddress(sentFrom));
    message.setSubject(subject);
    message.addRecipient(
        Message.RecipientType.TO, new InternetAddress(sentTo));

    // Sends the email
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
