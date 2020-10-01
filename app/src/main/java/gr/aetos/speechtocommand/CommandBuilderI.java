package gr.aetos.speechtocommand;


import java.util.List;

public interface CommandBuilderI {
    CommandBuilder setRegex(String regex);
    CommandBuilder setFlags(int flags);
    CommandBuilder setCommandExecutor(CommandExecutor commandExecutor);
    CommandBuilder setPhrases(List<String> phrases);
    Command build();
}
