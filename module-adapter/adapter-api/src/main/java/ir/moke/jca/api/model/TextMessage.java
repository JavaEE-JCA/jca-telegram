package ir.moke.jca.api.model;

public record TextMessage(String text, String chatId) implements TMessage {

}
