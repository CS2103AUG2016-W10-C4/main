package seedu.todo.logic.commands;

import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.model.tag.Tag;
import seedu.todo.model.task.ImmutableTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.todo.testutil.TaskFactory.convertToTags;

//@@author A0135805H
/**
 * Performs integration testing for Tag Command.
 */
public class TagCommandTest extends CommandTest {

    /* Constants */
    private static final String[] TAG_NAMES = {
        "MacBook_Pro1", "MacBook_Air", "Mac_PrO2", "Surface_-Pro3", "Surface-STUDIO", "SurFACE_BoOk"
    };

    private static final String[] TAG_NAMES_0_TO_4 = {
            TAG_NAMES[0], TAG_NAMES[1], TAG_NAMES[2], TAG_NAMES[3], TAG_NAMES[4]
    };

    private static final String[] TAG_NAMES_0_TO_3 = {
            TAG_NAMES[0], TAG_NAMES[1], TAG_NAMES[2], TAG_NAMES[3]
    };

    private static final String[] TAG_NAMES_0_TO_2 = {
            TAG_NAMES[0], TAG_NAMES[1], TAG_NAMES[2]
    };

    /* Override Methods */
    @Override
    protected BaseCommand commandUnderTest() {
        return new TagCommand();
    }

    @Before
    public void setUp() throws Exception{
        //Task indexed at 5
        model.add("Task 5 With 5 Tags");
        Thread.sleep(10);

        //Task indexed at 4
        model.add("Task 4 With 3 Tags");
        Thread.sleep(10);

        //Task indexed at 3
        model.add("Task 3 With 1 Tag");
        Thread.sleep(10);

        //Task indexed at 2
        model.add("Task 2 With 1 Tag");
        Thread.sleep(10);

        //Task indexed at 1
        model.add("Task 1 With 0 Tags");
        Thread.sleep(10);

        //Add tags to dummy tasks
        model.addTagsToTask(5, TAG_NAMES_0_TO_4);
        model.addTagsToTask(4, TAG_NAMES_0_TO_2);
        model.addTagsToTask(3, TAG_NAMES[1]);
        model.addTagsToTask(2, TAG_NAMES[0]);
    }

    /* Global Tag View Test */
    @Test
    public void getTagList_tagListAtInitial() throws Exception {
        //Since at start, we have tag names from 0 to 4 inclusive assigned to tasks already.
        //So we expect the following 5 tag names.
        assertGlobalTagListCorrect(TAG_NAMES_0_TO_4);
    }

    @Test
    public void getTagList_tagListAfterDelete() throws Exception {
        //Expects the tag list to contain only the tags that are still available after a task is deleted.
        model.delete(5);

        //Since task 5 is deleted, we expect tag 0 to 2 inclusive.
        assertGlobalTagListCorrect(TAG_NAMES_0_TO_2);
    }

    /* Add Tag Test */
    @Test
    public void testAddTag_addSingleTag() throws Exception {
        //Adds a single tag to a task without tags.
        Set<Tag> expectedTags = convertToTags(TAG_NAMES[5]);
        setParameter("1 " + TAG_NAMES[5]);
        execute(true);

        ImmutableTask task = getTaskAt(1);
        assertEquals(expectedTags, task.getTags());
        assertGlobalTagListCorrect(TAG_NAMES);
    }

    @Test
    public void testAddTag_addMaxTags() throws Exception {
        //Adds 5 tags to a task without tags.
        Set<Tag> expectedTags = convertToTags(TAG_NAMES_0_TO_4);

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("1")
                .add(TAG_NAMES[0]).add(TAG_NAMES[1]).add(TAG_NAMES[2]).add(TAG_NAMES[3]).add(TAG_NAMES[4]);
        setParameter(joiner.toString());
        execute(true);

        ImmutableTask task = getTaskAt(1);
        assertEquals(expectedTags, task.getTags());
        assertGlobalTagListCorrect(TAG_NAMES_0_TO_4);
    }

    @Test
    public void testAddTag_unrestrictedSeparators() throws Exception {
        //Allows separators such as space, commas.
        Set<Tag> expectedTags = convertToTags("Pikachu", "Pichu", "Raichu");
        setParameter("1 ,  Pichu, Pikachu Raichu  , ");
        execute(true);

        ImmutableTask task = getTaskAt(1);
        assertEquals(expectedTags, task.getTags());
        assertGlobalTagListCorrect(TAG_NAMES_0_TO_4, "Pikachu", "Pichu", "Raichu");
    }

    //@@author A0135805H
    @Test (expected = ValidationException.class)
    public void testAddTag_addNoTags() throws Exception {
        //Provides no tag names.
        setParameter("1");
        execute(false);
    }

    @Test (expected = ValidationException.class)
    public void testAddTag_addTooManyTags1() throws Exception {
        //Adds 6 tags to a task without tags
        setParameter("1 tag1 tag2 tag3 tag4 tag5 tag6");
        execute(false);
    }

    @Test (expected = ValidationException.class)
    public void testAddTag_addTooManyTags2() throws Exception {
        //Adds 3 tags to a task with 3 tags
        setParameter("4 tag1 tag2 tag3");
        execute(false);
    }

    @Test (expected = ValidationException.class)
    public void testAddTag_addInvalidTag() throws Exception {
        //Adds a tag with invalid character (not alphanumeric, nor underscores, nor dashes)
        setParameter("1 invalid:)");
        execute(false);
    }

    @Test (expected = ValidationException.class)
    public void testAddTag_addTooLongTag() throws Exception {
        //Adds a tag with 21 characters
        setParameter("1 123456789012345678901");
        execute(false);
    }

    @Test (expected = ValidationException.class)
    public void testAddTag_duplicatedTagNames() throws Exception {
        //Adds tags that contains duplicated tag names
        setParameter("1 hello say hello again");
        execute(false);
    }

    /* Delete Tag From Task Test */
    @Test
    public void testDeleteTagFromTask_deleteOneTag() throws Exception {
        //Deletes one tag from task 5 with 5 tags. Expects 4 tags left.
        Set<Tag> expectedTags = convertToTags(TAG_NAMES_0_TO_3);

        setParameter("5");
        setParameter("d", TAG_NAMES[4]);
        execute(true);

        ImmutableTask task = getTaskAt(5);
        assertEquals(expectedTags, task.getTags());
        assertGlobalTagListCorrect(TAG_NAMES_0_TO_3);
    }

    @Test
    public void testDeleteTagFromTask_deleteAllTags() throws Exception {
        //Deletes all the tags from a task with 5 tags. Expects none left.
        Set<Tag> expectedTags = new HashSet<>();

        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(TAG_NAMES[0]).add(TAG_NAMES[1]).add(TAG_NAMES[2]).add(TAG_NAMES[3]).add(TAG_NAMES[4]);

        setParameter("5");
        setParameter("d", joiner.toString());
        execute(true);

        ImmutableTask task = getTaskAt(5);
        assertEquals(expectedTags, task.getTags());
        assertGlobalTagListCorrect(TAG_NAMES_0_TO_2);
    }
    
    //@@author A0135817B
    @Test
    public void testDeleteTag_caseInsensitivity() throws Exception {
        // Tagging a task with the same tag but in different case should cause the tag to be renamed
        setParameter("2");
        setParameter("d", TAG_NAMES[0].toUpperCase());
        execute(true);
        assertEquals(0, getTaskAt(2).getTags().size());
    }

    //@@author A0135805H
    @Test
    public void testDeleteTagFromTask_deleteMissing() {
        //Deletes a tag that is not found. This should result in no-op.
        Set<Tag> expectedTags = new HashSet<>(getTaskAt(5).getTags());

        setParameter("5");
        setParameter("d", TAG_NAMES[1] + " " + TAG_NAMES[5]);

        try {
            execute(false);
            assert false; //After the above line, not supposed to happen!
        } catch (ValidationException e) {
            //Okay, exception is expected. Now to check the state of the object.
            Set<Tag> outcomeTags = new HashSet<>(getTaskAt(5).getTags());
            assertEquals(expectedTags, outcomeTags);
        }
    }

    @Test (expected = ValidationException.class)
    public void testDeleteTagFromTask_deleteNoParam() throws Exception {
        //Declares no parameters to delete command.
        setParameter("1");
        setParameter("d", "   ");
        execute(false);
    }

    /* Delete Tags Globally Test */
    @Test
    public void testDeleteTagGlobally_deleteOneTag() throws Exception {
        //Deletes one tag from the list of tags. All other tags should stay intact.
        Set<Tag> expects0Tags = convertToTags();
        Set<Tag> expects1Tags = convertToTags(TAG_NAMES[1]);
        Set<Tag> expects2Tags = convertToTags(TAG_NAMES[1], TAG_NAMES[2]);
        Set<Tag> expects4Tags = convertToTags(TAG_NAMES[1], TAG_NAMES[2], TAG_NAMES[3], TAG_NAMES[4]);

        setParameter("d", TAG_NAMES[0]);
        execute(true);

        assertEquals(expects0Tags, getTaskAt(1).getTags());
        assertEquals(expects0Tags, getTaskAt(2).getTags());
        assertEquals(expects1Tags, getTaskAt(3).getTags());
        assertEquals(expects2Tags, getTaskAt(4).getTags());
        assertEquals(expects4Tags, getTaskAt(5).getTags());
        assertGlobalTagListCorrect(TAG_NAMES[1], TAG_NAMES[2], TAG_NAMES[3], TAG_NAMES[4]);
    }

    @Test
    public void testDeleteTagGlobally_deleteMoreTags() throws Exception {
        //Deletes two tags from the list of tags. All other tags should stay intact.
        Set<Tag> expects0Tags = convertToTags();
        Set<Tag> expects1Tags = convertToTags(TAG_NAMES[2]);
        Set<Tag> expects3Tags = convertToTags(TAG_NAMES[2], TAG_NAMES[3], TAG_NAMES[4]);

        setParameter("d", TAG_NAMES[0] + " " + TAG_NAMES[1]);
        execute(true);

        assertEquals(expects0Tags, getTaskAt(1).getTags());
        assertEquals(expects0Tags, getTaskAt(2).getTags());
        assertEquals(expects0Tags, getTaskAt(3).getTags());
        assertEquals(expects1Tags, getTaskAt(4).getTags());
        assertEquals(expects3Tags, getTaskAt(5).getTags());
        assertGlobalTagListCorrect(TAG_NAMES[2], TAG_NAMES[3], TAG_NAMES[4]);
    }

    @Test
    public void testDeleteTagGlobally_deleteMissingTags() {
        //Deletes a tag that does not exist. This should result in no op.
        List<Set<Tag>> listOfExpectedOutcome = model.getObservableList().stream()
                .map((Function<ImmutableTask, Set<Tag>>) task -> new HashSet<>(task.getTags()))
                .collect(Collectors.toList());

        setParameter("d", TAG_NAMES[1] + " " + TAG_NAMES[5]);

        try {
            execute(false);
            assert false; //After the above line, not supposed to happen!
        } catch (ValidationException e) {
            //Validation exception expected, now check that the tags are unmodified.
            for (int taskIndex = 1; taskIndex <= 5 ; taskIndex ++) {
                assertEquals(listOfExpectedOutcome.get(taskIndex - 1), getTaskAt(taskIndex).getTags());
            }
        }
    }

    @Test (expected = ValidationException.class)
    public void testDeleteTagGlobally_deleteNoParam() throws Exception {
        //Declares no parameters to delete command.
        setParameter("d", "   ");
        execute(false);
    }

    /* Global Rename Tag Test */
    @Test
    public void globalRenameTag_globalRenameTagSuccess() throws Exception {
        //Renames a tag successfully
        Set<Tag> expectsTask1Tag = convertToTags();
        Set<Tag> expectsTask2Tag = convertToTags(TAG_NAMES[5]);
        Set<Tag> expectsTask3Tag = convertToTags(TAG_NAMES[1]);
        Set<Tag> expectsTask4Tag = convertToTags(TAG_NAMES[5], TAG_NAMES[1], TAG_NAMES[2]);
        Set<Tag> expectsTask5Tag
                = convertToTags(TAG_NAMES[5], TAG_NAMES[1], TAG_NAMES[2], TAG_NAMES[3], TAG_NAMES[4]);

        setParameter("r", TAG_NAMES[0] + " " + TAG_NAMES[5]);
        execute(true);

        assertEquals(expectsTask1Tag, getTaskAt(1).getTags());
        assertEquals(expectsTask2Tag, getTaskAt(2).getTags());
        assertEquals(expectsTask3Tag, getTaskAt(3).getTags());
        assertEquals(expectsTask4Tag, getTaskAt(4).getTags());
        assertEquals(expectsTask5Tag, getTaskAt(5).getTags());
        assertGlobalTagListCorrect(TAG_NAMES[1], TAG_NAMES[2], TAG_NAMES[3], TAG_NAMES[4], TAG_NAMES[5]);
    }
    
    

    @Test (expected = ValidationException.class)
    public void globalRenameTag_newNameExists() throws Exception {
        //Renames to a name that already exists.
        setParameter("r", TAG_NAMES[0] + " " + TAG_NAMES[1]);
        execute(false);
    }

    @Test (expected = ValidationException.class)
    public void globalRenameTag_oldNameMissing() throws Exception {
        //Renames from a name that does not exist
        setParameter("r", TAG_NAMES[5] + " " + TAG_NAMES[1]);
        execute(false);
    }

    @Test (expected = ValidationException.class)
    public void globalRenameTag_oneTagNameOnly() throws Exception {
        //Provides one tag name for rename only. This is incorrect.
        setParameter("r", TAG_NAMES[5]);
        execute(false);
    }

    @Test (expected = ValidationException.class)
    public void globalRenameTag_renameNoParams() throws Exception {
        //Provides no tag names for renaming. This is incorrect.
        setParameter("r", "   ");
        execute(false);
    }

    /* Rename Tag Test */
    @Test
    public void renameTagFromTask_renameSuccess() throws Exception {
        Set<Tag> expectedTags = convertToTags(TAG_NAMES[5]);
        Set<Tag> task4Tags = Sets.newHashSet(getTaskAt(4).getTags());

        setParameter("2");
        setParameter("r", TAG_NAMES[0] + " " + TAG_NAMES[5]);
        execute(true);

        //Checks if the tag at task 2 is renamed successfully.
        assertEquals(expectedTags, getTaskAt(2).getTags());

        //Check that tag from other tasks are untouched.
        assertEquals(task4Tags, getTaskAt(4).getTags());

        //Check global tags list
        assertGlobalTagListCorrect(TAG_NAMES);
    }

    @Test (expected = ValidationException.class)
    public void renameTagFromTask_tagNameNotFound() throws Exception {
        setParameter("2");
        setParameter("r", TAG_NAMES[5] + " " + TAG_NAMES[2]);
        execute(false);
    }

    @Test (expected = ValidationException.class)
    public void renameTagFromTask_sameTagName() throws Exception {
        //Same old and new tag names.
        setParameter("2");
        setParameter("r", TAG_NAMES[0] + " " + TAG_NAMES[0]);
        execute(false);
    }

    @Test (expected = ValidationException.class)
    public void renameTagFromTask_newNameExist() throws Exception {
        setParameter("4");
        setParameter("r", TAG_NAMES[0] + " " + TAG_NAMES[2]);
        execute(false);
    }

    /* Helper Methods */
    /**
     * Asserts the global tag list contains the list of tags specified in {@code expectedTagNames}.
     */
    private void assertGlobalTagListCorrect(String... expectedTagNames) {
        Set<String> expectedTagSet = Arrays.stream(expectedTagNames)
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        Set<String> actualTagSet = Tag.getLowerCaseNames(model.getGlobalTagsList());
        assertEquals(expectedTagSet, actualTagSet);
    }

    /**
     * Asserts the global tag list contains the list of tags specified in {@code expectedTagNames}.
     */
    private void assertGlobalTagListCorrect(String[] expectedTagNameArray, String... expectedTagNames) {
        List<String> tagNames = new ArrayList<>();
        Arrays.stream(expectedTagNameArray).forEach(tagNames::add);
        Arrays.stream(expectedTagNames).forEach(tagNames::add);
        assertGlobalTagListCorrect(tagNames.toArray(new String[0]));
    }
}
