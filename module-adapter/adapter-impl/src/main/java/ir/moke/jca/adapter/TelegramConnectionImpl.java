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
import ir.moke.jca.api.TelegramConnection;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.logging.Logger;

public class TelegramConnectionImpl implements TelegramConnection {
    private static Logger log = Logger.getLogger(TelegramConnectionImpl.class.getName());

    private TelegramManagedConnection mc;

    private TelegramManagedConnectionFactory mcf;

    public TelegramConnectionImpl(TelegramManagedConnection mc, TelegramManagedConnectionFactory mcf) {
        this.mc = mc;
        this.mcf = mcf;
    }

    public void close() {
        mc.closeHandle(this);
    }

    @Override
    public void sendMessage(final TMessage message) {
        mc.sendMessage(message);
    }
}
