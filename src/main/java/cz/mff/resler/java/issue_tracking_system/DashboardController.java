package cz.mff.resler.java.issue_tracking_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Class that handles the dashboard/overview functionality
 */
public class DashboardController implements Initializable {
    private static final UserModel userModel = new UserModel();
    private final static IssueModel issueModel = new IssueModel();

    private final static ObservableList<Issue> openIssuesList = FXCollections.observableArrayList();
    private final static ObservableList<Issue> closedIssuesList = FXCollections.observableArrayList();

    String loggedInUsername;

    @FXML
    Label usernameLabel;

    @FXML
    TableView openIssuesTableView, closedIssuesTableView;

    @FXML
    TableColumn idColOpenIssues, createdByColOpenIssues, priorityColOpenIssues,
            dateCreatedColOpenIssues, summaryColOpenIssues, detailButtonColOpenIssues,
            editButtonColOpenIssues, closeButtonColOpenIssues;

    @FXML
    TableColumn idColClosedIssues, createdByColClosedIssues, priorityColClosedIssues,
            dateCreatedColClosedIssues, summaryColClosedIssues, closedByColClosedIssues,
            dateClosedColClosedIssues, detailButtonColClosedIssues;

    @FXML
    Button refreshButtonOpenIssues;

    /**
     * Initializes the DashboardController after loading its scene.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//      Open issues table
        idColOpenIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("idString"));
        createdByColOpenIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("createdByUserString"));
        priorityColOpenIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("priorityString"));
        dateCreatedColOpenIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("dateCreated"));
        summaryColOpenIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("summary"));
        detailButtonColOpenIssues.setCellValueFactory(new PropertyValueFactory<Issue, Button>("detailButtonOpenIssue"));
        editButtonColOpenIssues.setCellValueFactory(new PropertyValueFactory<Issue, Button>("editButton"));
        closeButtonColOpenIssues.setCellValueFactory(new PropertyValueFactory<Issue, Button>("closeButton"));

//      Closed issues table
        idColClosedIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("idString"));
        createdByColClosedIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("createdByUserString"));
        priorityColClosedIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("priorityString"));
        dateCreatedColClosedIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("dateCreated"));
        summaryColClosedIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("summary"));
        closedByColClosedIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("closedByUserString"));
        dateClosedColClosedIssues.setCellValueFactory(new PropertyValueFactory<Issue, String>("dateClosed"));
        detailButtonColClosedIssues.setCellValueFactory(new PropertyValueFactory<Issue, Button>("detailButtonClosedIssue"));
    }

    /**
     * Fetches open issues from the database and puts them into the dashboard scene.
     */
    public void refreshOpenIssuesTable() {
        openIssuesList.clear();

        LinkedList<Issue> allOpenIssues = issueModel.getAllOpenIssues();

        for (Issue issue : allOpenIssues) {
            createDetailButton(issue, true);
            createEditButton(issue);
            createCloseButton(issue);
            openIssuesList.add(issue);
        }

        openIssuesTableView.setItems(openIssuesList);
    }

    /**
     * Fetches closed issues from the database and puts them into the dashboard scene.
     */
    public void refreshClosedIssuesTable() {
        closedIssuesList.clear();

        LinkedList<Issue> allClosedIssues = issueModel.getAllClosedIssues();

        for (Issue issue : allClosedIssues) {
            createDetailButton(issue, false);
            closedIssuesList.add(issue);
        }

        closedIssuesTableView.setItems(closedIssuesList);
    }

    /**
     * Creates a new detail button and sets its onAction to load the issue detail scene.
     *
     * @param issue open or closed issue which will have this button assigned to its corresponding field.
     * @param openIssue true if issue is an open one, false otherwise.
     */
    public void createDetailButton(Issue issue, boolean openIssue) {
        Button detailButton = new Button();
        detailButton.setText("Detail");

        detailButton.setOnAction(actionEvent -> {
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(Objects.requireNonNull(getClass().getResource("detail_issue_scene.fxml")).openStream());

                DetailIssueController detailIssueController = loader.getController();

                detailIssueController.issueIDLabel.setText("Issue " + issue.getIdString());
                detailIssueController.createdByLabel.setText(issue.getCreatedByUserString());
                detailIssueController.priorityLabel.setText(issue.getPriorityString());
                detailIssueController.summaryText.setText(issue.getSummary());
                detailIssueController.descriptionText.setText(issue.getDescription());

                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (openIssue) {
            issue.setDetailButtonOpenIssue(detailButton);
        } else {
            issue.setDetailButtonClosedIssue(detailButton);
        }
    }

    /**
     * Creates a new edit button and sets its onAction to load the edit issue scene.
     *
     * @param issue open or closed issue which will have this button assigned to its corresponding field.
     */
    public void createEditButton(Issue issue) {
        Button editButton = new Button();
        editButton.setText("Edit");

        editButton.setOnAction(actionEvent -> {
            try {
                Stage primaryStage = new Stage();
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(Objects.requireNonNull(getClass().getResource("edit_issue_scene.fxml")).openStream());

                EditIssueController editIssueController = loader.getController();
                editIssueController.edit_Issue_Id_Label.setText("Editing issue #" + issue.getId());
                editIssueController.creatorLabel.setText(issue.getCreatedByUserString());
                editIssueController.priorityChoiceBox.setValue(issue.getPriorityString());
                editIssueController.summaryTextField.setText(issue.getSummary());
                editIssueController.descriptionTextArea.setText(issue.getDescription());
                editIssueController.issueId = issue.getId();

                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.showAndWait();

                refreshOpenIssuesTable();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (!issue.getCreatedByUserString().equals(loggedInUsername)) {
            editButton.setDisable(true);
        }

        issue.setEditButton(editButton);
    }

    /**
     * Creates a new close button and sets its onAction to close the given issue.
     *
     * @param issue open or closed issue which will have this button assigned to its corresponding field.
     */
    public void createCloseButton(Issue issue) {
        Button closeButton = new Button();
        closeButton.setText("Close");

        closeButton.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to close this issue?", ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Closing issue " + issue.getIdString());
            alert.setHeaderText(null);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.YES) {
                int closedBy = userModel.getUserID(loggedInUsername);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                LocalDateTime now = LocalDateTime.now();
                String dateClosed = dtf.format(now);

                issue.setClosedByUserId(closedBy);
                issue.setDateClosed(dateClosed);

                issueModel.closeIssue(issue);

                refreshOpenIssuesTable();
                refreshClosedIssuesTable();
            }
        });

        int creatorId = issue.getCreatedByUserId();
        int loggedInUserId = userModel.getUserID(loggedInUsername);

        int roleCreator = userModel.getUserRole(creatorId);
        int roleLoggedInUser = userModel.getUserRole(loggedInUserId);

        if (roleLoggedInUser < roleCreator) {
            closeButton.setDisable(true);
        }

        issue.setCloseButton(closeButton);
    }

    /**
     * Loads the create new issue scene.
     *
     * @param event source event.
     */
    public void createNewIssueButtonClicked(ActionEvent event) {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(Objects.requireNonNull(getClass().getResource("create_issue_scene.fxml")).openStream());
            CreateIssueController createIssueController = loader.getController();
//          Pass name of logged-in user
            createIssueController.usernameLabel.setText(usernameLabel.getText().substring(15));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.showAndWait();

            refreshOpenIssuesTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the login scene.
     *
     * @param event source event.
     */
    public void logOutButtonClicked(ActionEvent event) {
        try {
            ((Node) event.getSource()).getScene().getWindow().hide();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("log_in_scene.fxml")));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
