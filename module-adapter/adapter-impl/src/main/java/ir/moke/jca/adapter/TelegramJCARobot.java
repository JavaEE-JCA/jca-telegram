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
package ir.moke.jca.adapter;

import ir.moke.jca.api.model.TextMessage;
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
            listener.onReceive(new TextMessage(update.getMessage().getText(), String.valueOf(update.getMessage().getChatId())));
        }
    }
}
