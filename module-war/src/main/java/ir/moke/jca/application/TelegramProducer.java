package ir.moke.jca.application;

import ir.moke.jca.api.TelegramConnection;
import ir.moke.jca.api.TelegramConnectionFactory;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.resource.ResourceException;

@Singleton
public class TelegramProducer {

    @Resource(name = "jca/Sender")
    private TelegramConnectionFactory telegramConnectionFactory;

    @Produces
    public TelegramConnection sendMessage(InjectionPoint ip) {
        try {
            return telegramConnectionFactory.getConnection();
        } catch (ResourceException e) {
            e.printStackTrace();
        }
        return null;
    }
}
