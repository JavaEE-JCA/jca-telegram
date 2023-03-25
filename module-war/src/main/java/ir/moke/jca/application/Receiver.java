/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ir.moke.jca.application;


import ir.moke.jca.api.TelegramBotListener;
import ir.moke.jca.api.TelegramConnection;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.inject.Inject;

@MessageDriven(
        name = "TelegramBotListener",
        activationConfig = {
                @ActivationConfigProperty(propertyName = "name", propertyValue = "roboexchange_bot"),
                @ActivationConfigProperty(propertyName = "token", propertyValue = "AAEH4Oh84xJOxli0roUy3fMGSftJu3CLNvY")
        }
)
public class Receiver implements TelegramBotListener {

    @Inject
    private TelegramConnection telegramConnection;

    @Override
    public void onReceive(Update update) {
        String text = update.getMessage().getText();
        String[] parts = text.split(" ");
        if ((parts[0]).startsWith("/")) {
            String command = parts[0];
            switch (command) {
                case "/list":
                    System.out.println("Hey, I receive 'list' command");
                    break;
                case "/help":
                    System.out.println("Please enter /list");
                    break;
            }
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(update.getMessage().getChatId()));
        message.setText("Hello !!!");
        telegramConnection.sendMessage(message);
    }
}
