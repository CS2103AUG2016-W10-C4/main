package seedu.todo.logic.commands;

import com.google.common.collect.ImmutableList;

import javafx.application.Platform;
import seedu.todo.commons.events.ui.HighlightTaskEvent;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.logic.arguments.Argument;
import seedu.todo.logic.arguments.IntArgument;
import seedu.todo.logic.arguments.Parameter;
import seedu.todo.model.task.ImmutableTask;

import java.util.List;

//@@author A0092382A
public class PinCommand extends BaseCommand {

    private Argument<Integer> index = new IntArgument("index").required();

    @Override
    protected Parameter[] getArguments() {
        return new Parameter[]{ index };
    }

    @Override
    public String getCommandName() {
        return "pin";
    }

    @Override
    public List<CommandSummary> getCommandSummary() {
        return ImmutableList.of(new CommandSummary("Pin task to top of list", getCommandName(), 
            getArgumentSummary()));
    }

    @Override
    public CommandResult execute() throws ValidationException {
        ImmutableTask task = this.model.update(index.getValue(), t -> t.setPinned(!t.isPinned()));
        
        // Run the event later to prevent highlight event race condition 
        Platform.runLater(() -> {
            eventBus.post(new HighlightTaskEvent(task));
        });
        return new CommandResult();
    }

}
