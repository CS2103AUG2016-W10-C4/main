package seedu.todo.testutil;

import seedu.todo.model.task.ImmutableTask;

import java.util.StringJoiner;

/**
 * Generates the correct input command for the GUI.
 */
public class CommandGeneratorUtil {

    /**
     * Given an {@link ImmutableTask}, generate an add command text.
     * @param task The task to generate add command from.
     * @return The add command text.
     */
    public static String generateAddCommand(ImmutableTask task) {
        StringJoiner commandJoiner = new StringJoiner(" ");
        commandJoiner.add("add").add(task.getTitle());

        if (task.getStartTime().isPresent() && task.getEndTime().isPresent()) {
            commandJoiner.add("/d");
            commandJoiner.add(TimeUtil.getDateTimeText(task.getStartTime().get()));
            commandJoiner.add(TimeUtil.getDateTimeText(task.getEndTime().get()));
        } else if (task.getEndTime().isPresent()) {
            commandJoiner.add("/d");
            commandJoiner.add(TimeUtil.getDateTimeText(task.getEndTime().get()));
        }

        if (task.getDescription().isPresent()) {
            commandJoiner.add("/m").add(task.getDescription().get());
        }

        if (task.getLocation().isPresent()) {
            commandJoiner.add("/l").add(task.getLocation().get());
        }

        if (task.isPinned()) {
            commandJoiner.add("/p");
        }

        return commandJoiner.toString();
    }

}
