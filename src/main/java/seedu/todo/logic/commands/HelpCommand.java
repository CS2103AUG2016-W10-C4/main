package seedu.todo.logic.commands;

import com.google.common.collect.ImmutableList;
import seedu.todo.commons.core.EventsCenter;
import seedu.todo.commons.events.ui.ShowHelpEvent;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.logic.arguments.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Shows the help 
 */
public class HelpCommand extends BaseCommand {
    private final static String HELP_MESSAGE = "Showing help...";
    
    private static List<CommandSummary> commandSummaries;

    @Override
    public CommandResult execute() throws ValidationException {
        if (commandSummaries == null) {
            commandSummaries = collectCommandSummaries();
        }
        
        EventsCenter.getInstance().post(new ShowHelpEvent(commandSummaries));
        return new CommandResult(HelpCommand.HELP_MESSAGE);
    }

    @Override
    protected Parameter[] getArguments() {
        return new Parameter[]{};
    }

    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public List<CommandSummary> getCommandSummary() {
        return ImmutableList.of(new CommandSummary("Get summary of commands", getCommandName()));
    }
    
    private List<CommandSummary> collectCommandSummaries() {
        List<CommandSummary> summaries = new ArrayList<>();
        for (String key : CommandMap.getCommandMap().keySet()) {
            summaries.addAll(CommandMap.getCommand(key).getCommandSummary());
        }
        return summaries;
    }
}