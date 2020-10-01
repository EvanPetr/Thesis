package gr.aetos.speechtocommand;


import java.util.List;
import java.util.Objects;

public class CommandBuilder implements CommandBuilderI {
    private String regex;
    private int flags = 0;
    private CommandExecutor commandExecutor;
    private List<String> phrases;

    @Override
    public CommandBuilder setRegex(String regex) {
        this.regex = regex;
        return this;
    }

    @Override
    public CommandBuilder setFlags(int flags) {
        this.flags = flags;
        return this;
    }

    @Override
    public CommandBuilder setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
        return this;
    }

    @Override
    public CommandBuilder setPhrases(List<String> phrases) {
        this.phrases = phrases;
        return this;
    }

    @Override
    public Command build() {
        Objects.requireNonNull(regex,"Regex attribute should be set via setRegex() method");
        Objects.requireNonNull(commandExecutor,"CommandExecutor attribute should be set via setCommandExecutor() method");
        return new Command(regex, commandExecutor, flags, phrases);
    }
}
