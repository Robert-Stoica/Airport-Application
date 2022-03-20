package org.comp2211;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
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
    private static final Logger logger = LogManager.getLogger(ObstacleInput.class);

    @FXML
    public void sendEmail() {
        final String from = "vabbit81@gmail.com";
        final String username = "vabbit81@gmail.com";
        final String password = "TaTasiMaMa123";

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
                return new PasswordAuthentication(username, password);
            }
        });

        sender.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String text = receiver.getText();
                String text1 = subject.getText();
                String text2 = area.getText();

                try {
                    // Create a default MimeMessage object.
                    Message message = new MimeMessage(session);

                    // Set From: header field of the header.
                    message.setFrom(new InternetAddress(from));

                    // Set To: header field of the header.
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(text));

                    // Set Subject: header field
                    message.setSubject(text1);

                    // Now set the actual message
                    message.setText(text2);

                    // Send message
                    Transport.send(message);
                    System.out.println("Sent message successfully.");
                } catch (MessagingException e) {
                    System.out.println("Sent message failed.");
                    e.printStackTrace();
                }
            }
        });
    }


}
