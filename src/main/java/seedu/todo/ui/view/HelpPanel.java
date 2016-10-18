package seedu.todo.ui.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.ui.util.FxViewUtil;
import seedu.todo.logic.commands.CommandSummary;
import seedu.todo.ui.UiPart;
import seedu.todo.ui.UiPartLoader;
import seedu.todo.ui.util.ViewGeneratorUtil;
import seedu.todo.ui.util.ViewStyleUtil;

import java.util.List;
import java.util.logging.Logger;

/**
 * A view that displays all the help commands in a single view.
 */
public class HelpPanel extends UiPart {

    private final Logger logger = LogsCenter.getLogger(HelpPanel.class);
    private static final String FXML = "HelpPanel.fxml";

    /*Layouts*/
    private AnchorPane placeholder;
    private VBox helpPanelView;

    @FXML
    private GridPane helpGrid;

    /**
     * Loads and initialise the feedback view element to the placeHolder
     * @param primaryStage of the application
     * @param placeholder where the view element {@link #helpPanelView} should be placed
     * @return an instance of this class
     */
    public static HelpPanel load(Stage primaryStage, AnchorPane placeholder) {
        HelpPanel helpPanel = UiPartLoader.loadUiPart(primaryStage, placeholder, new HelpPanel());
        helpPanel.addToPlaceholder();
        helpPanel.configureLayout();
        helpPanel.hideHelpPanel();
        return helpPanel;
    }

    /**
     * Adds this view element to external placeholder
     */
    private void addToPlaceholder() {
        this.placeholder.getChildren().add(helpPanelView);
    }

    /**
     * Configure the UI layout of {@link CommandErrorView}
     */
    private void configureLayout() {
        FxViewUtil.applyAnchorBoundaryParameters(helpPanelView, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(helpGrid, 0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Displays a list of commands into the helpPanelView
     */
    public void displayCommandSummaries(List<CommandSummary> commandSummaries) {
        this.showHelpPanel();
        helpGrid.getChildren().clear();
        int rowIndex = 0;
        for (CommandSummary commandSummary : commandSummaries) {
            System.out.println(rowIndex);
            appendCommandSummary(rowIndex++, commandSummary);
        }
    }

    /**
     * Add a command summary to each row of the helpGrid
     * @param rowIndex the row number to which the command summary should append to
     * @param commandSummary to be displayed
     */
    private void appendCommandSummary(int rowIndex, CommandSummary commandSummary) {
        Text commandScenario = ViewGeneratorUtil.constructText(commandSummary.scenario, ViewStyleUtil.STYLE_TEXT_4);
        Text commandName = ViewGeneratorUtil.constructText(commandSummary.command, ViewStyleUtil.STYLE_TEXT_4);
        Text commandArgument = ViewGeneratorUtil.constructText(" " + commandSummary.arguments, ViewStyleUtil.STYLE_TEXT_4);

        ViewStyleUtil.addClassStyle(commandArgument, ViewStyleUtil.STYLE_CODE);
        ViewStyleUtil.addClassStyle(commandName, ViewStyleUtil.STYLE_CODE);
        ViewStyleUtil.addClassStyle(commandName, ViewStyleUtil.STYLE_BOLDER);

        TextFlow combinedCommand = ViewGeneratorUtil.placeIntoTextFlow(commandName, commandArgument);
        helpGrid.addRow(rowIndex, commandScenario, combinedCommand);
    }

    /* Ui Methods */
    public void hideHelpPanel() {
        FxViewUtil.setCollapsed(helpPanelView, true);
    }

    private void showHelpPanel() {
        FxViewUtil.setCollapsed(helpPanelView, false);
    }


    /* Override Methods */
    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void setNode(Node node) {
        this.helpPanelView = (VBox) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}