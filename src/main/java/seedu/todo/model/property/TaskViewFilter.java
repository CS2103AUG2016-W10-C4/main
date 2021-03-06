package seedu.todo.model.property;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

import seedu.todo.commons.util.TimeUtil;
import seedu.todo.model.task.ImmutableTask;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.function.Predicate;

//@@author A0092382A
public class TaskViewFilter {
    private static TimeUtil timeUtil = new TimeUtil();
    private static final Comparator<ImmutableTask> CHRONOLOGICAL = (a, b) -> ComparisonChain.start()
            .compare(a.getEndTime().orElse(null), b.getEndTime().orElse(null), Ordering.natural().nullsLast())
            .result();
    private static final Comparator<ImmutableTask> CHRONOLOGICAL_EVENT = (a, b) -> ComparisonChain.start()
            //completed events are below
            .compareFalseFirst(a.isCompleted(), b.isCompleted())
            //then followed by events which have passed/ tasks which are overdue 
            .compareFalseFirst(timeUtil.isOverdue(a.getEndTime().orElse(LocalDateTime.now())), 
                               timeUtil.isOverdue(b.getEndTime().orElse(LocalDateTime.now())))
            //then by chronological order
            .compare(a.getEndTime().orElse(null), b.getEndTime().orElse(null), Ordering.natural().nullsLast())
            .result();
    
    private static final Comparator<ImmutableTask> LAST_UPDATED = (a, b) -> 
        b.getCreatedAt().compareTo(a.getCreatedAt());
    
    public static final TaskViewFilter DEFAULT = new TaskViewFilter("all",
        null, LAST_UPDATED);
    
    public static final TaskViewFilter INCOMPLETE = new TaskViewFilter("incomplete",
        task -> !task.isCompleted(), CHRONOLOGICAL);
    
    public static final TaskViewFilter DUE_SOON = new TaskViewFilter("due soon", 
        task -> !task.isCompleted() && !task.isEvent() && task.getEndTime().isPresent(), CHRONOLOGICAL);
    
    public static final TaskViewFilter EVENTS = new TaskViewFilter("events",
        ImmutableTask::isEvent, CHRONOLOGICAL_EVENT);
    
    public static final TaskViewFilter COMPLETED = new TaskViewFilter("completed",
        ImmutableTask::isCompleted, LAST_UPDATED);
    
    public static final TaskViewFilter TODAY = new TaskViewFilter("today",
            task -> timeUtil.isToday(task) , CHRONOLOGICAL_EVENT);

    public final String name;
    
    public final Predicate<ImmutableTask> filter;
    
    public final Comparator<ImmutableTask> sort;
    
    public final int shortcutCharPosition;

    public TaskViewFilter(String name, Predicate<ImmutableTask> filter, Comparator<ImmutableTask> sort) {
        this(name, filter, sort, 0);
    }

    public TaskViewFilter(String name, Predicate<ImmutableTask> filter, Comparator<ImmutableTask> sort, int underlineCharPosition) {
        this.name = name;
        this.filter = filter;
        this.sort = sort;
        this.shortcutCharPosition = underlineCharPosition;
    }
    
    public static TaskViewFilter[] all() {
        return new TaskViewFilter[]{
            DEFAULT, TODAY, DUE_SOON, EVENTS, INCOMPLETE, COMPLETED
        };
    }

    @Override
    public String toString() {
        return name;
    }
}
