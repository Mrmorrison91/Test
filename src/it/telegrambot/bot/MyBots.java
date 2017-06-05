package it.telegrambot.bot;

import java.util.logging.Logger;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import it.telegrambot.mailbomber.MailBomber;

public class MyBots extends TelegramLongPollingBot {

	private static final Logger logger = Logger.getLogger(MyBots.class.getName());

	public void onUpdateReceived(Update update) {
		
		boolean comandoErrato = true;

		// Controllo se il messaggio inviato dall'utente è un messaggio e che contiene testo
		if (update.hasMessage() && update.getMessage().hasText()) { 
			SendMessage message = new SendMessage();
			//Prendo l'ip del messaggio
			Long sender_id = update.getMessage().getChatId(); 
			
			logger.info("ID MESSAGE: " + sender_id);
			//Prendo il testo del messaggio inviato dall'utente
			String received_text = update.getMessage().getText().trim();

			logger.info("MESSAGE RICEVUTO: " + received_text);

			String text_to_send = "";

			if (received_text.equalsIgnoreCase("ciao") || received_text.equalsIgnoreCase("/start")) {
				
				comandoErrato = false;

				//Controllo se l'utente ha impostato il suo nome su Telegram
				if (update.getMessage().getChat().getFirstName() != null) {
					
					String utente = update.getMessage().getChat().getFirstName();
					text_to_send = "Ciao" + " " + utente + " sono TucciaBot. Scrivi HELP per sapere cosa posso fare. ";
				} else {
					text_to_send = "Ciao sono TucciaBot. Scrivi HELP per sapere cosa posso fare. ";
				}
				message.setChatId(sender_id); // Settiamo l'Id della chat
				message.setText(text_to_send); // Settiamo il messaggio

				try {
					sendMessage(message); // Inviamo il messaggio
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}

			}

			if (received_text.equalsIgnoreCase("help")) {
			
				comandoErrato = false;
				text_to_send = comandi();
			
				message.setChatId(sender_id); // Settiamo l'Id della chat
				message.setText(text_to_send); // Settiamo il messaggio

				try {
					sendMessage(message); // Inviamo il messaggio
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}

			}

			if (received_text.length() > 10 && received_text.substring(0, 10).equalsIgnoreCase("mailbomber")) {

				comandoErrato = false;
				String datiDellUtente[] = received_text.split("\\s");
				String to = datiDellUtente[1];
				String textMessage = datiDellUtente[2];
				int n = Integer
						.parseInt(datiDellUtente[3] != "" || datiDellUtente[3] != null ? datiDellUtente[3] : "0");

				if (to != null && text_to_send != null) {

					if (to.contains("@") && to.contains(".") && n != 0) {

						logger.info("Email della vittima: " + to);
						logger.info("Testo dell'email: " + textMessage);
						logger.info("Numero dell'invio: " + n);

						message.setChatId(sender_id);
						message.setText("EMAIL DELLA VITTIMA: " + to + "\n" + "TESTO DELL'EMAIL: " + textMessage + "\n"
								+ "NUMERO EMAIL " + n + "\n" + "Attendi la risposta...");

						try {
							sendMessage(message); // metodo che invia il
													// messaggio all'utente
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}

						MailBomber mailBomber = new MailBomber();

						boolean resultEmail = mailBomber.sendMail(to, textMessage, n);

						if (resultEmail) {

							message.setChatId(sender_id);
							message.setText("L'email sono state spedite con successo alla vittima: " + to);
							try {
								sendMessage(message);
							} catch (TelegramApiException e) {
								e.printStackTrace();
							}

						} else {

							message.setChatId(sender_id);
							message.setText("L'email non sono state spedite alla vittima: " + to);
							try {
								sendMessage(message);
							} catch (TelegramApiException e) {
								e.printStackTrace();
							}

						}
					} else {
						text_to_send = "Email errata.";
						if (n <= 0) {
							text_to_send = text_to_send + " " + "Impossibile inviare " + n + " email. RIPROVA";
						}
						message.setChatId(sender_id);
						message.setText(text_to_send);
						try {
							sendMessage(message);
						} catch (TelegramApiException e) {
							e.printStackTrace();
						}
					}
				} else {
					text_to_send = "I dati che hai inserito sono errati. RIPROVA";
					message.setChatId(sender_id);
					message.setText(text_to_send);
					try {
						sendMessage(message);
					} catch (TelegramApiException e) {
						e.printStackTrace();
					}
				}
			}

		}
		

		// Entra in questo if se l'utente manda la sua posizione, e restituisce
		// le coordinate geografiche.
		if (update.hasMessage() && update.getMessage().hasLocation()) {
			posizione(update);
			logger.info("L'utente ha inviato una posizione");
			SendMessage messageLocation = posizione(update);
			try {
				sendMessage(messageLocation);
			} catch (TelegramApiException e) {

				e.printStackTrace();
			}
		}
		
		if(comandoErrato){
			SendMessage message = new SendMessage();
			Long idSender = update.getMessage().getChatId();
			
			message.setText("Comando errato. Digita HELP per sapere cosa posso fare.");
			message.setChatId(idSender); // Settiamo l'Id della chat
	

			try {
				sendMessage(message); // Inviamo il messaggio
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
			
		}

	}

	public SendMessage posizione(Update update) {
		SendMessage message = new SendMessage();
		
		Double latitudine = null;
		Double longitudine = null;

		if (update.hasMessage() && update.getMessage().hasLocation()) {

			Long idLocation = update.getMessage().getChatId();
			latitudine = update.getMessage().getLocation().getLatitude();
			longitudine = update.getMessage().getLocation().getLongitude();
			logger.info("L'id Location è:_" + idLocation);
			message.setChatId(idLocation);
		}

		return message.setText("La tua latitudine e: " + String.valueOf(latitudine) + " La tua longitudine è: "
				+ String.valueOf(longitudine));
	}

	public String getBotUsername() {
		return "TucciaBot"; // Nome del bot
	}

	public String getBotToken() {
		return "394874277:AAGx6beZgGIkkSxMQScCUSrGxvVoMKmrx5Q"; // Token del bot
																// per lo
																// sviluppo
	}

	public String comandi() {
		return "I comandi sono: MAILBOMBER Es: mailbomber vittima@email.com testo_email 5";
	}

}
