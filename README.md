## JCA Telegram Inbound/Outbound Resource Adapter

Sample JavaEE 8 jca resource adapter for learning/testing jca connection factory

#### build and run :
**TomEE 8** `mvn clean compile install ; (cd module-ear/ ; mvn tomee:run)`

**Liberty/OpenLiberty** `mvn clean compile install ; (cd module-ear/ ; mvn liberty:run)`

***test :***
```java  
@MessageDriven(  
 name = "TelegramBotListener", activationConfig = { @ActivationConfigProperty(propertyName = "name", propertyValue = "Enter Bot Name"), @ActivationConfigProperty(propertyName = "token", propertyValue = "Enter Bot Token") })  
public class Receiver implements TelegramBotListener {  
  
 @Inject private TelegramConnection telegramConnection;  
 @Override public void onReceive(Update update) { // handle received messages .... }}  
```  

***Activation config properties :***    
|Parameter     | description                         
|--------------|-------------------------------       
|name          |The name of bot                     
|token         |The token of bot                 
|useProxy      |Active proxy connection on bot  
|proxyType     |Proxy connection type [SOCKS4,SOCKS5,HTTP]  
|proxyHost     |Proxy host address ex: x.y.z.w
|proxyPort     |Proxy host port    ex: 1080
