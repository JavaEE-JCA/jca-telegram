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

import jakarta.resource.spi.endpoint.MessageEndpoint;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class EndpointTarget extends Thread {
    private final MessageEndpointFactory messageEndpointFactory;
    private MessageEndpoint messageEndpoint;
    private final TelegramActivationSpec spec;
    private TelegramJCARobot telegramJCARobot;

    public EndpointTarget(MessageEndpointFactory messageEndpointFactory, TelegramActivationSpec spec) {
        this.messageEndpointFactory = messageEndpointFactory;
        this.spec = spec;
    }

    private void createEndpoint() {
        try {
            messageEndpoint = messageEndpointFactory.createEndpoint(null);

            String token = spec.getToken();
            String botName = spec.getName();

            telegramJCARobot = new TelegramJCARobot(token, botName, messageEndpoint);
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(telegramJCARobot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        /*
         * Liberty does not allow create endpoint during resourceAdapter activation .
         * so need to create this on another thread .
         * */
        createEndpoint();
    }

    public MessageEndpoint getMessageEndpoint() {
        return messageEndpoint;
    }

    public TelegramJCARobot getTelegramJCARobot() {
        return telegramJCARobot;
    }
}
