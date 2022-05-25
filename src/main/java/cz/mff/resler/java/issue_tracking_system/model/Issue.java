package cz.mff.resler.java.issue_tracking_system.model;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 * Class representing an issue in the application.
 */
public class Issue {
    private final int id;
    private final SimpleStringProperty idString;

    private final int createdByUserId;
    private final SimpleStringProperty createdByUserString;

    private final int priorityInt;
    private final SimpleStringProperty priorityString;

    private final SimpleStringProperty dateCreated;
    private final SimpleStringProperty summary;
    private final SimpleStringProperty description;
    private final SimpleStringProperty dateClosed;

    private int closedByUserId;
    private final SimpleStringProperty closedByUserString;

    private Button detailButtonOpenIssue;
    private Button detailButtonClosedIssue;
    private Button editButton;
    private Button closeButton;

    public Issue(int id, String createdByUserString, int createdByUserId,
                 String priorityString, int priorityInt, String dateCreated,
                 String summary, String description, int closedBy, String closedByString, String dateClosed) {
        this.id = id;
        this.idString = new SimpleStringProperty("#" + id);
        this.createdByUserString = new SimpleStringProperty(createdByUserString);
        this.createdByUserId = createdByUserId;
        this.priorityString = new SimpleStringProperty(priorityString);
        this.priorityInt = priorityInt;
        this.dateCreated = new SimpleStringProperty(dateCreated);
        this.summary = new SimpleStringProperty(summary);
        this.description = new SimpleStringProperty(description);
        this.closedByUserId = closedBy;
        this.closedByUserString = new SimpleStringProperty(closedByString);
        this.dateClosed = new SimpleStringProperty(dateClosed);
    }

    public int getId() {
        return id;
    }

    public String getIdString() {
        return idString.get();
    }

    public SimpleStringProperty idStringProperty() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString.set(idString);
    }

    public String getCreatedByUserString() {
        return createdByUserString.get();
    }

    public SimpleStringProperty createdByUserStringProperty() {
        return createdByUserString;
    }

    public void setCreatedByUserString(String createdByUserString) {
        this.createdByUserString.set(createdByUserString);
    }

    public int getCreatedByUserId() {
        return createdByUserId;
    }

    public String getPriorityString() {
        return priorityString.get();
    }

    public SimpleStringProperty priorityStringProperty() {
        return priorityString;
    }

    public void setPriorityString(String priorityString) {
        this.priorityString.set(priorityString);
    }

    public int getPriorityInt() {
        return priorityInt;
    }

    public String getDateCreated() {
        return dateCreated.get();
    }

    public SimpleStringProperty dateCreatedProperty() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated.set(dateCreated);
    }

    public String getSummary() {
        return summary.get();
    }

    public SimpleStringProperty summaryProperty() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary.set(summary);
    }

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public int getClosedByUserId() {
        return closedByUserId;
    }

    public void setClosedByUserId(int closedByUserId) {
        this.closedByUserId = closedByUserId;
    }

    public String getClosedByUserString() {
        return closedByUserString.get();
    }

    public SimpleStringProperty closedByUserStringProperty() {
        return closedByUserString;
    }

    public void setClosedByUserString(String closedByUserString) {
        this.closedByUserString.set(closedByUserString);
    }

    public String getDateClosed() {
        return dateClosed.get();
    }

    public SimpleStringProperty dateClosedProperty() {
        return dateClosed;
    }

    public void setDateClosed(String dateClosed) {
        this.dateClosed.set(dateClosed);
    }

    public Button getDetailButtonOpenIssue() {
        return detailButtonOpenIssue;
    }

    public void setDetailButtonOpenIssue(Button detailButtonOpenIssue) {
        this.detailButtonOpenIssue = detailButtonOpenIssue;
    }

    public Button getDetailButtonClosedIssue() {
        return detailButtonClosedIssue;
    }

    public void setDetailButtonClosedIssue(Button detailButtonClosedIssue) {
        this.detailButtonClosedIssue = detailButtonClosedIssue;
    }

    public Button getEditButton() {
        return editButton;
    }

    public void setEditButton(Button editButton) {
        this.editButton = editButton;
    }

    public Button getCloseButton() {
        return closeButton;
    }

    public void setCloseButton(Button closeButton) {
        this.closeButton = closeButton;
    }
}
