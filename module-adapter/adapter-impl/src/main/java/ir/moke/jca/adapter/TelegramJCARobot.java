package ir.moke.jca.adapter;

import ir.moke.jca.api.TMessage;
import ir.moke.jca.api.TelegramBotListener;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class TelegramJCARobot extends TelegramLongPollingBot {

    private final String botName;
    private TelegramBotListener listener;

    public TelegramJCARobot(DefaultBotOptions options, String token, String botName) {
        super(options, token);
        this.botName = botName;
    }

    public void setListener(TelegramBotListener listener) {
        this.listener = listener;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            listener.onReceive(new TMessage(update.getMessage().getText(), String.valueOf(update.getMessage().getChatId())));
        }
    }
}
