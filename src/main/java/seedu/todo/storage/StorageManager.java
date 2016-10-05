package seedu.todo.storage;

import com.google.common.eventbus.Subscribe;

import seedu.todo.commons.core.ComponentManager;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.commons.events.model.AddressBookChangedEvent;
import seedu.todo.commons.events.storage.DataSavingExceptionEvent;
import seedu.todo.commons.exceptions.DataConversionException;
import seedu.todo.model.ReadOnlyAddressBook;
import seedu.todo.model.ImmutableTodoList;
import seedu.todo.model.UserPrefs;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private AddressBookStorage addressBookStorage;
    private TodoListStorage todoListStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(AddressBookStorage addressBookStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.addressBookStorage = addressBookStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String addressBookFilePath, String userPrefsFilePath) {
        this(new XmlAddressBookStorage(addressBookFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public String getAddressBookFilePath() {
        return addressBookStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException {
        return readAddressBook(addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return addressBookStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, addressBookStorage.getAddressBookFilePath());
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        addressBookStorage.saveAddressBook(addressBook, filePath);
    }


    @Override
    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveAddressBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }
    
    // ================ AddressBook methods ==============================

	@Override
	public String getTodoListFilePath() {
		return todoListStorage.getTodoListFilePath();
	}

	@Override
	public Optional<ImmutableTodoList> readTodoList() throws DataConversionException, IOException {
        return readTodoList(todoListStorage.getTodoListFilePath());
	}

	@Override
	public Optional<ImmutableTodoList> readTodoList(String filePath) throws DataConversionException, IOException {
		logger.fine("Attempting to read data from file: " + filePath);
        return todoListStorage.readTodoList(filePath);
	}

	@Override
	public void saveTodoList(ImmutableTodoList todoList) throws IOException {
		todoListStorage.saveTodoList(todoList);		
	}

	@Override
	public void saveTodoList(ImmutableTodoList todoList, String filePath) throws IOException {
		logger.fine("Attempting to write to data file: " + filePath);
        todoListStorage.saveTodoList(todoList, filePath);
	}

	@Override
	public void updateTodoListStorage(ImmutableTodoList todoList) throws IOException {
		todoListStorage.saveTodoList(todoList);
	}

}