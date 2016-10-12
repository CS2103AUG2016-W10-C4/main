package seedu.todo.logic;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.logic.commands.BaseCommand;
import seedu.todo.logic.commands.CommandResult;
import seedu.todo.logic.parser.ParseResult;
import seedu.todo.logic.parser.Parser;
import seedu.todo.model.ErrorBag;
import seedu.todo.model.TodoModel;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TodoLogicTest {
    private static final String INPUT = "input";
    
    private Parser parser = mock(Parser.class); 
    private Dispatcher dispatcher = mock(Dispatcher.class);
    private ParseResult parseResult = mock(ParseResult.class);
    private TodoModel model = mock(TodoModel.class);
    private BaseCommand command = mock(BaseCommand.class);
    private Logic logic = new TodoLogic(parser, model, dispatcher);

    @Before
    public void setUp() throws Exception {
        // Wire up some default behavior 
        when(parser.parse(TodoLogicTest.INPUT))
            .thenReturn(parseResult);
        when(dispatcher.dispatch(parseResult))
            .thenReturn(command);
    }
    
    private CommandResult execute() throws Exception {
        CommandResult r = logic.execute(TodoLogicTest.INPUT);

        verify(parser).parse(TodoLogicTest.INPUT);
        verify(dispatcher).dispatch(parseResult);
        
        return r;
    }
    
    @Test
    public void testExecute() throws Exception {
        execute();
        
        verify(command).setModel(model);
        verify(command).setArguments(parseResult);
        verify(command).execute();
        
        // Logic should not touch model directly 
        verifyZeroInteractions(model);
    }
    
    @Test
    public void testArgumentError() throws Exception {
        // Create a stub exception for setArguments to throw
        ValidationException e = mock(ValidationException.class);
        ErrorBag errors = mock(ErrorBag.class);
        
        when(e.getErrors()).thenReturn(errors);
        doThrow(e).when(command).setArguments(parseResult);
        
        CommandResult r = execute();
        
        assertFalse(r.isSuccessful());

        verifyZeroInteractions(model);
    }
    
    // TODO: Create a test for exceptions thrown on BaseCommand.execute()
}
