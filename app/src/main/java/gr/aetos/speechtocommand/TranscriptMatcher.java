package gr.aetos.speechtocommand;

import java.util.List;

public class TranscriptMatcher {
    public List<Command> commands;

    public TranscriptMatcher(List<Command> commands){
        this.commands = commands;
    }

    public void match(String transcript){
        for(Command command : commands){
            if(command.match(transcript)){
                command.execute(command.groups());
                break;
            }
        }
    }
}
