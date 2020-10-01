package gr.aetos.speechtocommand;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Command {
    public Pattern pattern;
    private Matcher matcher;
    private CommandExecutor commandExecutor;
    private List<String> phrases;

    public Command(String regex, CommandExecutor commandExecutor, int flags, List<String> phrases){
        this.commandExecutor = commandExecutor;
        this.phrases = phrases;
        try{
            pattern = Pattern.compile(regex, flags);
        }
        catch(PatternSyntaxException pSE){
            System.out.println("Incorrect Regular Expression: " + pSE.getMessage());
        }
    }

    public Boolean match(String text){
        matcher = pattern.matcher(text);
        return matcher.matches();
    }

    public Boolean find(String text){
        matcher = pattern.matcher(text);
        return matcher.find();
    }

    public void execute(List<String> list){
        commandExecutor.execute(list);
    }

    public List<String> groups(){
        List<String> groups = new ArrayList<>();
        if(matcher != null){
            int noGroups = matcher.groupCount();
            for(int i=0; i <= noGroups; i++) {
                groups.add(matcher.group(i));
            }
        }
        else{
            System.out.println("Couldn't match!");
        }
        return groups;
    }

    public List<String> getPhrases(){
        if(phrases == null){
            phrases = new ArrayList<>();
        }
        return phrases;
    }
}
