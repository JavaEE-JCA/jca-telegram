package ir.moke.jca.api.model;

import java.util.List;

public record MenuMessage(String text, List<MenuRow> menuRowList, String chatId) implements TMessage {

}
