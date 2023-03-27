package ir.moke.jca.adapter;

import ir.moke.jca.api.TelegramBotListener;
import jakarta.resource.spi.endpoint.MessageEndpoint;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramJCARobot extends TelegramLongPollingBot {

    private final String token;
    private final String botName;
    private final MessageEndpoint messageEndpoint;

    public TelegramJCARobot(DefaultBotOptions options, String token, String botName, MessageEndpoint messageEndpoint) {
        super(options, token);
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
