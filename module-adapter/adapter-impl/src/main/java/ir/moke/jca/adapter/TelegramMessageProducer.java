package ir.moke.jca.adapter;

import ir.moke.jca.api.model.*;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramMessageProducer {
    public static void sendMessage(TelegramJCARobot telegramJCARobot, TMessage tMessage) {
        try {
            if (tMessage instanceof TextMessage textMessage) {
                sendTextMessage(telegramJCARobot, textMessage);
            } else if (tMessage instanceof MenuMessage menuMessage) {
                sendMenuMessage(telegramJCARobot, menuMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendMenuMessage(TelegramJCARobot telegramJCARobot, MenuMessage menuMessage) throws TelegramApiException {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        for (MenuRow menuRow : menuMessage.menuRowList()) {
            KeyboardRow row = new KeyboardRow();
            for (Menu menu : menuRow.menuList()) {
                KeyboardButton button = new KeyboardButton();
                button.setText(menu.text());
                if (menu.requestContact()) button.setRequestContact(true);
                if (menu.requestLocation()) button.setRequestLocation(true);
                row.add(button);
            }
            keyboardRowList.add(row);
        }
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(menuMessage.text());
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(menuMessage.chatId());
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        telegramJCARobot.execute(sendMessage);
    }

    private static void sendTextMessage(TelegramJCARobot telegramJCARobot, TextMessage textMessage) throws TelegramApiException {
        SendMessage send = new SendMessage();
        send.setChatId(textMessage.chatId());
        send.setText(textMessage.text());
        telegramJCARobot.execute(send);
    }
}
