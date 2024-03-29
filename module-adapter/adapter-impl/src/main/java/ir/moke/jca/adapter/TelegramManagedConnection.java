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

import ir.moke.jca.api.TelegramConnection;
import ir.moke.jca.api.model.TMessage;
import jakarta.resource.NotSupportedException;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.*;

import javax.security.auth.Subject;
import javax.transaction.xa.XAResource;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class TelegramManagedConnection implements ManagedConnection {

    private static Logger log = Logger.getLogger(TelegramManagedConnection.class.getName());

    private PrintWriter logwriter;

    private TelegramManagedConnectionFactory mcf;

    private List<ConnectionEventListener> listeners;

    private TelegramConnectionImpl connection;

    public TelegramManagedConnection(TelegramManagedConnectionFactory mcf) {
        this.mcf = mcf;
        this.logwriter = null;
        this.listeners = Collections.synchronizedList(new ArrayList<ConnectionEventListener>(1));
        this.connection = null;
    }

    public Object getConnection(Subject subject,
                                ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        log.finest("getConnection()");
        connection = new TelegramConnectionImpl(this, mcf);
        return connection;
    }

    public void associateConnection(Object connection) throws ResourceException {
        log.finest("associateConnection()");

        if (connection == null)
            throw new ResourceException("Null connection handle");

        if (!(connection instanceof TelegramConnectionImpl))
            throw new ResourceException("Wrong connection handle");

        this.connection = (TelegramConnectionImpl) connection;
    }

    public void cleanup() throws ResourceException {
        log.finest("cleanup()");
    }

    public void destroy() throws ResourceException {
        log.finest("destroy()");
    }

    public void addConnectionEventListener(ConnectionEventListener listener) {
        log.finest("addConnectionEventListener()");

        if (listener == null) {
            throw new IllegalArgumentException("Listener is null");
        }

        listeners.add(listener);
    }

    public void removeConnectionEventListener(ConnectionEventListener listener) {
        log.finest("removeConnectionEventListener()");
        if (listener == null)
            throw new IllegalArgumentException("Listener is null");
        listeners.remove(listener);
    }

    void closeHandle(TelegramConnection handle) {
        ConnectionEvent event = new ConnectionEvent(this, ConnectionEvent.CONNECTION_CLOSED);
        event.setConnectionHandle(handle);
        for (ConnectionEventListener cel : listeners) {
            cel.connectionClosed(event);
        }
    }

    public PrintWriter getLogWriter() throws ResourceException {
        log.finest("getLogWriter()");
        return logwriter;
    }

    public void setLogWriter(PrintWriter out) throws ResourceException {
        log.finest("setLogWriter()");
        logwriter = out;
    }

    public LocalTransaction getLocalTransaction() throws ResourceException {
        throw new NotSupportedException("getLocalTransaction() not supported");
    }

    public XAResource getXAResource() throws ResourceException {
        throw new NotSupportedException("getXAResource() not supported");
    }

    public ManagedConnectionMetaData getMetaData() throws ResourceException {
        log.finest("getMetaData()");
        return new TelegramManagedConnectionMetaData();
    }

    void sendMessage(TMessage tMessage) {
        final TelegramResourceAdapter resourceAdapter = (TelegramResourceAdapter) mcf.getResourceAdapter();
        resourceAdapter.sendMessage(tMessage);
    }
}
