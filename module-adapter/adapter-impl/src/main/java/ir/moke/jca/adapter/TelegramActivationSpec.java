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

import jakarta.resource.ResourceException;
import jakarta.resource.spi.Activation;
import jakarta.resource.spi.ActivationSpec;
import jakarta.resource.spi.InvalidPropertyException;
import jakarta.resource.spi.ResourceAdapter;

@Activation(messageListeners = TelegramBotListener.class)
public class TelegramActivationSpec implements ActivationSpec {

    private ResourceAdapter resourceAdapter;
    private Class<?> beanClass;
    private String token;
    private String name;

    public TelegramActivationSpec() {
        System.out.println("+------------------------------+");
        System.out.println("|    TelegramActivationSpec    |");
        System.out.println("+------------------------------+");
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void validate() throws InvalidPropertyException {
    }

    @Override
    public ResourceAdapter getResourceAdapter() {
        return resourceAdapter;
    }

    @Override
    public void setResourceAdapter(ResourceAdapter ra) throws ResourceException {
        this.resourceAdapter = ra;
    }
}
