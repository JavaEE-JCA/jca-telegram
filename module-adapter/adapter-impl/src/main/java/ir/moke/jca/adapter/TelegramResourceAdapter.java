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

import ir.moke.jca.api.TMessage;
import jakarta.resource.spi.ActivationSpec;
import jakarta.resource.spi.BootstrapContext;
import jakarta.resource.spi.Connector;
import jakarta.resource.spi.ResourceAdapter;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.transaction.xa.XAResource;

@Connector
public class TelegramResourceAdapter implements ResourceAdapter {
    private static final Logger logger = LoggerFactory.getLogger(TelegramResourceAdapter.class);
    private TelegramJCARobot telegramJCARobot;
    private MessageEndpointFactory messageEndpointFactory;

    public TelegramResourceAdapter() {
        System.out.println("+-------------------------------+");
        System.out.println("|    TelegramResourceAdapter    |");
        System.out.println("+-------------------------------+");
    }

    public void start(BootstrapContext bootstrapContext) {
    }

    public void stop() {
    }

    public void endpointActivation(final MessageEndpointFactory messageEndpointFactory, final ActivationSpec activationSpec) {
        TelegramActivationSpec spec = (TelegramActivationSpec) activationSpec;
        this.messageEndpointFactory = messageEndpointFactory;

        final DefaultBotOptions botOptions = new DefaultBotOptions();
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

        try {
            telegramJCARobot = new TelegramJCARobot(botOptions, token, botName);
            new EndpointTarget(this).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) {
        //TODO , Implement me
    }

    public XAResource[] getXAResources(ActivationSpec[] activationSpecs) {
        return new XAResource[0];
    }

    public void sendMessage(final TMessage message) {
        try {
            SendMessage send = new SendMessage();
            send.setChatId(message.chatId());
            send.setText(message.text());
            telegramJCARobot.execute(send);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public TelegramJCARobot getTelegramJCARobot() {
        return telegramJCARobot;
    }

    public MessageEndpointFactory getMessageEndpointFactory() {
        return messageEndpointFactory;
    }
}
