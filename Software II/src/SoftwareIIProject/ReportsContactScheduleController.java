package SoftwareIIProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for Contact's Schedule Report
 */
public class ReportsContactScheduleController {

    private ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private Connection conn;
    private Integer userID;

    @FXML TableView<Appointments> appointmentsTableView;
    @FXML private TableColumn<Appointments, Integer> appointmentIDColumn;
    @FXML private TableColumn<Appointments, String> titleColumn;
    @FXML private TableColumn<Appointments, String> descriptionColumn;
    @FXML private TableColumn<Appointments, String> locationColumn;
    @FXML private TableColumn<Appointments, String> contactNameColumn;
    @FXML private TableColumn<Appointments, String> typeColumn;
    @FXML private TableColumn<Appointments, Date> startColumn;
    @FXML private TableColumn<Appointments, Date> endColumn;
    @FXML private TableColumn<Appointments, Integer> customerIDColumn;
    @FXML private TableColumn<Appointments, String> customerNameColumn;

    @FXML private ComboBox<String> contactComboBox;

    @FXML private Label viewAppointmentsForLabel;
    @FXML private Button backButton;

    /**
     * Updates tables based on Contact selected in ComboBox
     */
    public void updateTableView()
    {
        ObservableList<Appointments> updateList = FXCollections.observableArrayList();

        for (Appointments appointments : appointmentsList) {
            if (appointments.getContactName().equals(contactComboBox.getValue())) {
                updateList.add(appointments);
            }
        }
        appointmentsTableView.setItems(updateList);

    }

    /**
     * Returns user to Report UI passes lists, User ID, and database Connection
     * @param event back button pushed
     * @throws IOException throws exception
     */
    public void backToAppointments(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ReportsUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        ReportUIController reportUIControllerUIController = loader.getController();
        reportUIControllerUIController.setDatabaseConnection(conn, userID, customerList, appointmentsList);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * Sets Lists, Connection, User ID for the Scene
     * Sets Contact ComboBox and sets appointmentTableView
     * @param customerUI database Connection
     * @param user User ID
     * @param customer Customer list
     * @param appointments Appointments list
     */
    public void setDatabaseConnection(Connection customerUI, Integer user, ObservableList<Customer> customer, ObservableList<Appointments> appointments)
    {
        conn = customerUI;
        userID = user;
        customerList = customer;
        appointmentsList = appointments;
        setContactComboBox();
        appointmentsTableView.setItems(appointmentsList);
    }

    /**
     * Sets Contact ComboBox
     */
    private void setContactComboBox() {
        try {
            String sqlStatement = "SELECT * FROM contacts";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            while (result.next()) {
                contactComboBox.getItems().add(result.getString("Contact_Name"));
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Set Table View and Language based off User's default Language (English or French)
     */
    public void initialize()
    {

        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactNameColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        appointmentIDColumn.setText(rb.getString("AppointmentID"));
        titleColumn.setText(rb.getString("Title"));
        descriptionColumn.setText(rb.getString("Description"));
        locationColumn.setText(rb.getString("Location"));
        contactNameColumn.setText(rb.getString("ContactName"));
        typeColumn.setText(rb.getString("Type"));
        startColumn.setText(rb.getString("Start"));
        endColumn.setText(rb.getString("End"));
        customerIDColumn.setText(rb.getString("CustomerID"));
        customerNameColumn.setText(rb.getString("CustomerName"));

        viewAppointmentsForLabel.setText(rb.getString("ViewAppointmentsFor"));
        backButton.setText(rb.getString("Back"));




    }


}
