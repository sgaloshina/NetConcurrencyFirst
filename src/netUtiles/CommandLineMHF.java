package netUtiles;

/**
 * Created by svetlana on 13.05.17.
 */
public class CommandLineMHF implements MessageHandlerFactory {
    @Override
    public MessageHandler createMessageHandler() {
        CommandLineMH commandLineMH = new CommandLineMH();
        return commandLineMH;
    }
}
