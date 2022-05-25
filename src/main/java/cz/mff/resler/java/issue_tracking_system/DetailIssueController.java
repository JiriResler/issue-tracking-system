package cz.mff.resler.java.issue_tracking_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class that handles the issue detail scene
 */
public class DetailIssueController implements Initializable {
    @FXML
    Label issueIDLabel;

    @FXML
    Label createdByLabel;

    @FXML
    Label priorityLabel;

    @FXML
    Text summaryText;

    @FXML
    Text descriptionText;

    /**
     * Initializes the DetailIssueController after loading its scene.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Hides the issue detail scene.
     *
     * @param event source event.
     */
    public void closeDetailButtonClicked(ActionEvent event) {
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
}
