package smshandler;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import android.util.Log;

public class MailClient {

	String host = "smtp.gmail.com";
	String address = "y.b.n.udara@gmail.com";
	String from = "y.b.n.udara@gmail.com";
	String pass = "";
	String to = "y.b.n.udara@gmail.com";

	Multipart multipart;
	String finalString = "";

	public void SendMail(String incomingNumber, String messageBody)
			throws AddressException, MessagingException {

		Log.i("code come here", "");
		Properties prop = System.getProperties(); // get Properties object
													// instance
		prop.put("mail.smtp.starttls.enable", "true"); // add key value faires
		prop.put("mail.smtp.host", host);
		prop.put("mail.smtp.user", address);
		prop.put("mail.smtp.password", pass);
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		Log.i("check", "done pops");

		Session session = Session.getDefaultInstance(prop, null);
		DataHandler handler = new DataHandler(new ByteArrayDataSource(
				finalString.getBytes(), "text/plain"));
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setDataHandler(handler);
		Log.i("check", "done session");

		multipart = new MimeMultipart();
		InternetAddress toAddress = new InternetAddress(to);
		message.addRecipient(Message.RecipientType.TO, toAddress);
		Log.i("check", "add recepient");
		message.setSubject("send auto mail");
		message.setContent(multipart);
		message.setText("From : " + incomingNumber + " \n Message:  "
				+ messageBody);

		Log.i("check", "transport");
		Transport transport = session.getTransport("smtp");
		Log.i("check", "connecting");

		Log.v("", "From : " + incomingNumber + " \n Message:  " + messageBody);
		try {
			transport.connect(host, address, pass);

			Log.i("check", "wanna send");
			transport.sendMessage(message, message.getAllRecipients());
		} catch (Exception e) {
			Log.v("error", "occured =" + e.toString());
		}
		transport.close();

		Log.i("check", "sent");

	}

}
