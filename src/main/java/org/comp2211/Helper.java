package org.comp2211;

import java.util.Properties;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Helper class to facilitate certain useful functions.
 *
 * @author MGhee
 */
public class Helper {

  private static final Logger logger = LogManager.getLogger(Helper.class);
  /** The recipient of the email. */
  @FXML private TextField receiver;
  /** The subject line of the email. */
  @FXML private TextField subject;
  /** Button to send the email. */
  @FXML private Button sender;
  /** The body of the email. */
  @FXML private TextArea area;

  public static void infoBox(String infoMessage, String titleBar) {
    JOptionPane.showMessageDialog(
        null, infoMessage, "InfoBox: " + titleBar, JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Sends an email using the member variables as parameters. Emails will be sent from the address
   * "andreiesteban54@gmail.com". This is not totally secure (as is obvious from the first three
   * lines) but is alright as long as we are closed source.
   *
   * @see #receiver
   * @see #subject
   * @see #sender
   * @see #area
   */
  @FXML
  public void sendEmail() throws MessagingException {
    final String send = "andreiesteban54@gmail.com";
    final String username = "andreiesteban54@gmail.com";
    final String password = "Comp2211-Seg";

    Properties props = System.getProperties();
    props.put("mail.smtp.starttls.enable", true);
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.user", "username");
    props.put("mail.smtp.password", "password");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", true);

    Session session =
        Session.getInstance(
            props,
            new javax.mail.Authenticator() {
              @Override
              protected PasswordAuthentication getPasswordAuthentication() {
                logger.info("Checked the email");
                return new PasswordAuthentication(username, password);
              }
            });
    String text = receiver.getText();
    String text1 = subject.getText();
    String text2 = area.getText();

    Message msg = new MimeMessage(session);

    msg.setFrom(new InternetAddress(send));

    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(text));

    msg.setSubject(text1);

    msg.setText(text2);

    Transport.send(msg);
    infoBox("You just sent an email", "Email");
    logger.info("Sent message successfully.");
    App.stg.close();
  }
}
