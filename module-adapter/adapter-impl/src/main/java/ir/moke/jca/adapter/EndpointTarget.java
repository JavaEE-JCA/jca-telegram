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
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class EndpointTarget extends Thread {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EndpointTarget.class);
    private final MessageEndpointFactory messageEndpointFactory;
    private MessageEndpoint messageEndpoint;
    private final TelegramActivationSpec spec;
    private TelegramJCARobot telegramJCARobot;
    private static final DefaultBotOptions botOptions = new DefaultBotOptions();

    public EndpointTarget(MessageEndpointFactory messageEndpointFactory, TelegramActivationSpec spec) {
        this.messageEndpointFactory = messageEndpointFactory;
        this.spec = spec;
    }

    private void createEndpoint() {
        try {
            logger.info("Create telegram jca endpoint");
            messageEndpoint = messageEndpointFactory.createEndpoint(null);

            String token = spec.getToken();
            String botName = spec.getName();
            boolean useProxy = spec.isUseProxy();
            String proxyTypeStr = spec.getProxyType();
            String proxyHost = spec.getProxyHost();
            Integer proxyPort = spec.getProxyPort();

            logger.info("Telegram name:" + botName + " token:" + token);
            logger.info("Telegram proxy use:" + useProxy + " type:" + proxyTypeStr + " host:" + proxyHost + " port:" + proxyPort);

            if (useProxy) {
                DefaultBotOptions.ProxyType proxyType = (proxyTypeStr != null && !proxyTypeStr.isEmpty()) ? DefaultBotOptions.ProxyType.valueOf(proxyTypeStr) : DefaultBotOptions.ProxyType.SOCKS5;
                logger.info(String.format("Telegram configured to use %s proxy", proxyType));

                if (proxyHost == null || proxyHost.isEmpty() || proxyPort == null || proxyPort <= 0) {
                    logger.error(String.format("Invalid proxy connection parameters host:%s port%s", proxyHost, proxyPort));
                } else {
                    botOptions.setProxyType(proxyType);
                    botOptions.setProxyHost(proxyHost);
                    botOptions.setProxyPort(proxyPort);
                }
            }

            telegramJCARobot = new TelegramJCARobot(botOptions, token, botName, messageEndpoint);
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
