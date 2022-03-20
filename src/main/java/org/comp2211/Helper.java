package org.comp2211;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;


public class Helper {

    @FXML
    private TextField receiver;
    @FXML
    private TextField subject;
    @FXML
    private Button sender;
    @FXML
    private TextArea area;
    private static final Logger logger = LogManager.getLogger(Helper.class);

    @FXML
    public void sendEmail() {
        final String send = "vabbit81@gmail.com";
        final String username = "vabbit81@gmail.com";
        final String password = "funswkoxjfhidjla";

        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", "username");
        props.put("mail.smtp.password", "password");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", true);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                logger.info("Checked the email");
                return new PasswordAuthentication(username, password);
            }
        });

        sender.setOnAction(event -> {
            String text = receiver.getText();
            String text1 = subject.getText();
            String text2 = area.getText();

            try {
                Message msg = new MimeMessage(session);

                msg.setFrom(new InternetAddress(send));

                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(text));

                msg.setSubject(text1);

                msg.setText(text2);

                Transport.send(msg);
                logger.info("Sent message successfully.");
                App.stg.close();
            } catch (MessagingException e) {
                logger.warn("Sent message failed.");
                e.printStackTrace();
            }
        });
    }


}
