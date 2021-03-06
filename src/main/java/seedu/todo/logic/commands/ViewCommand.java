package seedu.todo.logic.commands;

import com.google.common.collect.ImmutableList;
import seedu.todo.model.property.TaskViewFilter;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.logic.arguments.Argument;
import seedu.todo.logic.arguments.Parameter;
import seedu.todo.logic.arguments.StringArgument;

import java.util.List;

//@@author A0092382A
public class ViewCommand extends BaseCommand {
    
    private Argument<String> view = new StringArgument("view").required();
    
    private TaskViewFilter viewSpecified;
    
    @Override
    protected Parameter[] getArguments() {
        return new Parameter[]{ view };
    }

    @Override
    public String getCommandName() {
        return "view";
    }

    @Override
    public List<CommandSummary> getCommandSummary() {
        return ImmutableList.of(new CommandSummary("Switch tabs", getCommandName(), getArgumentSummary()));
    }
    
    @Override
    protected void validateArguments() {
        if (!view.hasBoundValue()) {
            return;
        }
        
        TaskViewFilter[] viewArray = TaskViewFilter.all();
        String viewSpecified = view.getValue().trim().toLowerCase();
        
        for (TaskViewFilter filter : viewArray) {
            String viewName = filter.name;
            char shortcut = viewName.charAt(filter.shortcutCharPosition);
            boolean matchesShortcut = viewSpecified.length() == 1 && viewSpecified.charAt(0) == shortcut;
            
            if (viewName.contentEquals(viewSpecified) || matchesShortcut) {
                this.viewSpecified = filter;
                return;
            }
        }
        
        String error = String.format("The view %s does not exist", view.getValue());
        errors.put("view", error);
    }

    @Override
    public CommandResult execute() throws ValidationException {
        //dismisses find if present
        model.find(null);
        model.view(viewSpecified);
        return new CommandResult();
    }

}
