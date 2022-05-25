package cz.mff.resler.java.issue_tracking_system.controllers;

import cz.mff.resler.java.issue_tracking_system.model.IssueModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class that handles the issue editing scene
 */
public class EditIssueController implements Initializable {
    private final static IssueModel issueModel = new IssueModel();
    private int issueId;

    @FXML
    private Label edit_Issue_Id_Label;

    @FXML
    private Label creatorLabel;

    @FXML
    private ChoiceBox priorityChoiceBox;

    @FXML
    private TextField summaryTextField;

    @FXML
    private TextArea descriptionTextArea;

    public void setIssueId(int id) {
        this.issueId = id;
    }

    public Label getEdit_Issue_Id_Label() {
        return edit_Issue_Id_Label;
    }

    public Label getCreatorLabel() {
        return creatorLabel;
    }

    public ChoiceBox getPriorityChoiceBox() {
        return priorityChoiceBox;
    }

    public TextField getSummaryTextField() {
        return summaryTextField;
    }

    public TextArea getDescriptionTextArea() {
        return descriptionTextArea;
    }

    /**
     * Initializes the EditIssueController after loading its scene.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        priorityChoiceBox.getItems().add("Lowest");
        priorityChoiceBox.getItems().add("Low");
        priorityChoiceBox.getItems().add("Medium");
        priorityChoiceBox.getItems().add("High");
        priorityChoiceBox.getItems().add("Highest");
    }

    /**
     * Updates the source issue with the provided values from the form.
     *
     * @param event source event.
     */
    public void UpdateIssueButtonClicked(ActionEvent event) {
        String priority = priorityChoiceBox.getValue().toString();
        String summary = summaryTextField.getText();
        String description = descriptionTextArea.getText();

//      Reusing input values checking from the creation of an issue
        if (CreateIssueController.newIssueValuesAreCorrect(priority, summary, description)) {
            int priorityInt = switch (priority)
                    {
                        case "Lowest" -> 1;
                        case "Low" -> 2;
                        case "Medium" -> 3;
                        case "High" -> 4;
                        case "Highest" -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + priority);
                    };

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "The date of creation will " +
                            "be set to current date and time.",
                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Update date of creation");
            alert.setHeaderText("Do you also wish to update the " +
                    "date of creation for this issue?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                LocalDateTime now = LocalDateTime.now();
                String currentDateAndTime = dtf.format(now);

                issueModel.updateIssueWithDate(issueId, priorityInt, currentDateAndTime, summary, description);

                ((Node) event.getSource()).getScene().getWindow().hide();
            } else if (result.get() == ButtonType.NO) {
                issueModel.updateIssueWithoutDate(issueId, priorityInt, summary, description);

                ((Node) event.getSource()).getScene().getWindow().hide();
            }
//          Cancel button was pressed, nothing happens
        }
    }

    /**
     * Hides the edit issue scene.
     *
     * @param event source event.
     */
    public void cancelButtonClicked(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
}
