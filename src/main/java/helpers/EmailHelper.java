package helpers;

import base.TestDataBase.TestDataSetBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

public class EmailHelper extends TestDataSetBase {

    static DateHelper date = new DateHelper();
    protected final static Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    public static void sendEmail(String recipients, String attachmentPath, String platform) {

        String to = recipients;
        String from = sender;
        final String username = from;
        final String password = mailPassword;
        String host = smtpHost;
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", smtpAuth);
        // prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", smtpPort);
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(platform + " Regression Test");

            Multipart multipart = new MimeMultipart();
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachmentPath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(platform + " Regression Test.html");
            multipart.addBodyPart(messageBodyPart);
            MimeBodyPart contentBodyPart = new MimeBodyPart();
            contentBodyPart.setContent("<p style='font-family:calibri'>Salam, <br><br>This is an auto generated email to notify you with the automation test result for <b><font color='blue'>"
                    + platform + "</font></b>, you will be able to find the report in the attachment" +
                    "<br><br><b>Thank you,</b><br>Test Automation Team</p>", "text/html; charset=\"utf-8\"");
            multipart.addBodyPart(contentBodyPart);
            message.setContent(multipart);
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException e) {
            LOGGER.error("Couldn't send the email!");
            throw new RuntimeException(e);
        }
    }

    public static void sendEmailWithReportPath(String recipients, String platform, String reportPath) {
        String to = recipients;
        String from = sender;
        final String username = from;
        final String password = mailPassword;
        String host = smtpHost;
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", smtpAuth);
        // prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", smtpPort);
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(platform + " Regression Test");
            message.setContent("<p style='font-family:calibri'>Salam, <br><br>This is an auto generated email to notify you with the automation test result for <b><font color='blue'>"
                    + platform + "</font></b>, you will be able to find the report inside the below shared folder...<br><br>" +
                    reportPath + "<br><br>" +
                    "<b>Note:</b> You should have access to the shared folder to open the report ... <br><br>" +
                    "<b>Note:</b> All reports categorized by execution date ... <br><br>" +
                    "<b>Thank you,</b><br>Test Automation Team</p>", "text/html; charset=\"utf-8\"");
            Transport.send(message);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException e) {
            LOGGER.error("Couldn't send the email!");
            throw new RuntimeException(e);
        }
    }

    public static void emailReceiver(String host, String username, String password) {

        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        try {
            // Create a session with the IMAP server
            Session session = Session.getDefaultInstance(properties, null);
            Store store = session.getStore("imaps");
            store.connect(host, username, password);

            // Open the inbox folder
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Get the messages in the inbox
            Message[] messages = inbox.getMessages();

            // Loop through the messages and print their subject
            for (Message message : messages) {
                System.out.println("Subject: " + message.getSubject());
            }

            // Close the inbox and store
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
