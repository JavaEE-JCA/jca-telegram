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

import jakarta.resource.spi.ActivationSpec;
import jakarta.resource.spi.BootstrapContext;
import jakarta.resource.spi.Connector;
import jakarta.resource.spi.ResourceAdapter;
import jakarta.resource.spi.endpoint.MessageEndpointFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.transaction.xa.XAResource;

@Connector
public class TelegramResourceAdapter implements ResourceAdapter {

    private static EndpointTarget endpointTarget;

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
        endpointTarget = new EndpointTarget(messageEndpointFactory, spec);
        endpointTarget.start();
    }

    public void endpointDeactivation(MessageEndpointFactory messageEndpointFactory, ActivationSpec activationSpec) {
        if (endpointTarget != null) {
            endpointTarget.getMessageEndpoint().release();
        }
    }

    public XAResource[] getXAResources(ActivationSpec[] activationSpecs) {
        return new XAResource[0];
    }

    public void sendMessage(final SendMessage sendMessage) {
        try {
            TelegramJCARobot telegramJCARobot = endpointTarget.getTelegramJCARobot();
            telegramJCARobot.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
}
