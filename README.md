## JCA Telegram Inbound/Outbound Resource Adapter

Sample JavaEE 8 jca resource adapter for learning/testing jca connection factory

#### build and run :

**TomEE 8** `mvn clean compile install ; (cd module-ear/ ; mvn tomee:run)`

**Liberty/OpenLiberty** `mvn clean compile install ; (cd module-ear/ ; mvn liberty:run)`

***test :***

```java

@MessageDriven(
        name = "TelegramBotListener", activationConfig = {
        @ActivationConfigProperty(propertyName = "name", propertyValue = "Enter Bot Name"),
        @ActivationConfigProperty(propertyName = "token", propertyValue = "Enter Bot Token")
})
public class Receiver implements TelegramBotListener {

    @Inject
    private TelegramConnection telegramConnection;

    @Override
    public void onReceive(TextMessage message) {
        String text = message.text();
        String chatId = message.chatId();
        System.out.println("Receive message " + text);
        String[] parts = text.split(" ");
        if ((parts[0]).startsWith("/")) {
            String command = parts[0];
            switch (command) {
                case "/list" -> System.out.println("Hey, I receive 'list' command");
                case "/help" -> System.out.println("Please enter /list");
            }
        }

        System.out.println(chatId);
        telegramConnection.sendMessage(new TextMessage("receive: " + text + " from: " + chatId, chatId));
    }
}
```   

***Maven :***
```xml
<dependency>
    <groupId>ir.moke.jca</groupId>
    <artifactId>telegram-adapter-api</artifactId>
    <version>1.1</version>
    <scope>provided</scope>
</dependency>
```

**and rar archive:**   
```xml
<dependency>
    <groupId>ir.moke.jca</groupId>
    <artifactId>telegram-adapter-rar</artifactId>
    <version>1.1</version>
    <type>rar</type>
</dependency>
```

***Activation config properties :***    
|Parameter | description                         
|--------------|-------------------------------       
|name |The name of bot                     
|token |The token of bot                 
|useProxy |Active proxy connection on bot  
|proxyType |Proxy connection type [SOCKS4,SOCKS5,HTTP]  
|proxyHost |Proxy host address ex: x.y.z.w
|proxyPort |Proxy host port ex: 1080
