/**
 * Created by jackg on 8/1/2017.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

class Emailer implements Runnable {

    private final String host = Param.EMAIL_HOST;
    private final String port = Param.EMAIL_PORT;
    private final String mailFrom = Param.EMAIL_FROM;
    private final String password = Param.EMAIL_PASSWORD;
    private String message = "";
    private List<String> mailTo;
    private String subject;
    private List<String> attachFiles = new ArrayList<String>();


    Emailer(String subject, List<String> addresses, String message){
        this.subject = subject;
        this.mailTo = addresses;
        this.message = message;
    }

    Emailer(String subject, List<String> addresses, String message,String filePath){
        this.subject = subject;
        this.mailTo = addresses;
        this.message = message;
        this.attachFiles.add(filePath);
    }

    public void run(){
        try {
            sendEmailWithAttachments(host, port, mailFrom, password, mailTo,
                    subject, message, attachFiles);
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }

    private static void sendEmailWithAttachments(String host, String port,
                                                 final String userName, final String password, List<String> toAddress,
                                                 String subject, String message, List<String> attachFiles)
            throws AddressException, MessagingException {
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);

        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        //InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        for (String toAddressCur : toAddress) {
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddressCur));
        }


        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html");

        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // adds attachments
        if (attachFiles != null && attachFiles.size() > 0) {
            for (String filePath : attachFiles) {
                MimeBodyPart attachPart = new MimeBodyPart();

                try {
                    attachPart.attachFile(filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                multipart.addBodyPart(attachPart);
            }
        }

        // sets the multi-part as e-mail's content
        msg.setContent(multipart);

        // sends the e-mail
        Transport.send(msg);

    }

}

