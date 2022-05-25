package cz.mff.resler.java.issue_tracking_system.controllers;

import cz.mff.resler.java.issue_tracking_system.model.IssueModel;
import cz.mff.resler.java.issue_tracking_system.model.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Class that handles the creation of a new issue.
 */
public class CreateIssueController implements Initializable {

    private final static IssueModel issueModel = new IssueModel();
    private final static UserModel userModel = new UserModel();

    @FXML
    private Label usernameLabel;

    @FXML
    private ChoiceBox<String> priorityChoiceBox;

    @FXML
    private TextField summaryTextField;

    @FXML
    private TextArea descriptionTextArea;

    public Label getUsernameLabel() {
        return usernameLabel;
    }

    /**
     * Initializes the CreateIssueController after loading its scene.
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
     * Creates a new issue with the given values from the form.
     *
     * @param event source event.
     */
    public void createIssueButtonClicked(ActionEvent event) {
        String priority = priorityChoiceBox.getValue();
        String summary = summaryTextField.getText();
        String description = descriptionTextArea.getText();

//      Check correctness of the input values
        if (newIssueValuesAreCorrect(priority, summary, description)) {
            int priorityInt = switch (priority)
                    {
                        case "Lowest" -> 1;
                        case "Low" -> 2;
                        case "Medium" -> 3;
                        case "High" -> 4;
                        case "Highest" -> 5;
                        default -> throw new IllegalStateException("Unexpected value: " + priority);
                    };

//          Date created
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            LocalDateTime now = LocalDateTime.now();
            String currentDateAndTime = dtf.format(now);

//          Created by
            String creator = usernameLabel.getText();
            int createdBy = userModel.getUserID(creator);

            issueModel.addNewIssue(createdBy, priorityInt, currentDateAndTime, summary, description);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Issue was created.", ButtonType.OK);
            alert.setTitle("Issue created");
            alert.setHeaderText(null);
            alert.showAndWait();

//          Go to dashboard
            ((Node) event.getSource()).getScene().getWindow().hide();
        }
    }

    /**
     * Check whether the given values for a new issue are valid.
     *
     * @param priority cannot be null.
     * @param summary cannot be empty.
     * @param description cannot be empty.
     * @return true if the given values are valid.
     */
    public static boolean newIssueValuesAreCorrect(String priority, String summary, String description) {
        if (priority == null || summary.equals("") || description.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "All fields are mandatory.", ButtonType.OK);
            alert.setTitle("Empty field");
            alert.setHeaderText(null);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    /**
     * Hides the (create new issue) scene.
     *
     * @param event source event.
     */
    public void cancelButtonClicked(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
}

