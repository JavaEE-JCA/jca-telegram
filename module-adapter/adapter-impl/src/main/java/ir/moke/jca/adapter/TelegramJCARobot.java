package ir.moke.jca.adapter;

import ir.moke.jca.api.TelegramBotListener;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.resource.spi.endpoint.MessageEndpoint;

public class TelegramJCARobot extends TelegramLongPollingBot {

    private final String token;
    private final String botName;
    private final MessageEndpoint messageEndpoint;

    public TelegramJCARobot(String token, String botName, MessageEndpoint messageEndpoint) {
        System.out.println("+--------------------------------+");
        System.out.println("|    Telegram JCA Bot Started    |");
        System.out.println("+--------------------------------+");
        this.token = token;
        this.botName = botName;
        this.messageEndpoint = messageEndpoint;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            TelegramBotListener telegramBotListener = (TelegramBotListener) messageEndpoint;
            telegramBotListener.onReceive(update);
        }
    }
}
