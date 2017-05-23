package netUtiles;

/**
 * Created by svetlana on 14.05.17.
 */
public class PrintMessageHF implements MessageHandlerFactory {
    @Override
    public MessageHandler createMessageHandler() {
        PrintMessageHandler printMessageHandler = new PrintMessageHandler();
        return printMessageHandler;
    }
}
