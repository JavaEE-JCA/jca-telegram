package ir.moke.jca.application;

import ir.moke.jca.api.TelegramConnection;
import ir.moke.jca.api.TelegramConnectionFactory;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.resource.ResourceException;

@ApplicationScoped
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
