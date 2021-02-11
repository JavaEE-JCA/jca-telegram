## JCA Telegram Inbound/Outbound Resource Adapter

Sample JavaEE 8 jca resource adapter for learning/testing jca connection factory 

#### build and run :     
**TomEE 8**     
`mvn clean compile install ; (cd ear-module/ ; mvn tomee:run)`

**Liberty/OpenLiberty**     
`mvn clean compile install ; (cd ear-module/ ; mvn liberty:run)`

***test :***
```java
@MessageDriven(
        name = "TelegramBotListener",
        activationConfig = {
                @ActivationConfigProperty(propertyName = "name", propertyValue = "Enter Bot Name"),
                @ActivationConfigProperty(propertyName = "token", propertyValue = "Enter Bot Token")
        }
)
public class Receiver implements TelegramBotListener {

    @Inject
    private TelegramConnection telegramConnection;

    @Override
    public void onReceive(Update update) {
        // handle received messages ....
    }
}
```