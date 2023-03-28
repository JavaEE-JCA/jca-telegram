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

import ir.moke.jca.api.TelegramBotListener;
import jakarta.resource.spi.endpoint.MessageEndpoint;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class EndpointTarget extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(EndpointTarget.class);
    private final MessageEndpointFactory messageEndpointFactory;
    private final TelegramJCARobot telegramJCARobot;
    private TelegramBotsApi telegramBotsApi;

    public EndpointTarget(TelegramResourceAdapter resourceAdapter) {
        this.messageEndpointFactory = resourceAdapter.getMessageEndpointFactory();
        this.telegramJCARobot = resourceAdapter.getTelegramJCARobot();
        try {
            this.telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            MessageEndpoint messageEndpoint = messageEndpointFactory.createEndpoint(null);
            this.telegramJCARobot.setListener((TelegramBotListener) messageEndpoint);
            telegramBotsApi.registerBot(telegramJCARobot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
