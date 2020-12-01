package SoftwareIIProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**
 * Controller for Main UI
 */
public class MainUIController {

    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();

    //Configure the Customer Table
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerAddressColumn;
    @FXML private TableColumn<Customer, String> customerPostalCodeColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;
    @FXML private TableColumn<Customer, String> customerStateColumn;
    @FXML private TableColumn<Customer, String> customerCountryColumn;

    @FXML private Label customersLabel;
    @FXML private Label upcomingAppointmentLabel;
    @FXML private Label upcomingAppointmentIDLabel;
    @FXML private Label upcomingAppointmentDateTimeLabel;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button appointmentsButton;
    @FXML private Button reportsButton;
    @FXML private Button exitButton;
    @FXML private Label errorLabel;

    private Connection conn;
    private Integer userID;

    /**
     * Passes database Connection, lists, and User ID to CustomerAdd/Modify scene
     * @param event add Button Pushed
     * @throws IOException Throws Exception
     */
    public void addButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CustomerAddModifyUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        CustomerAddModifyController customerAddModifyController = loader.getController();
        customerAddModifyController.setDatabaseConnectionAdd(conn, userID, customerList, appointmentsList);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * Passes database Connection, lists, User ID, and selected Customer to CustomerAdd/Modify scene
     * @param event modify Button Pushed
     * @throws IOException Throws Exception
     */
    public void updateButtonPushed(ActionEvent event) throws IOException {

        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        if(customerTableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("CustomerAddModifyUI.fxml"));
            Parent tableViewParent = loader.load();

            Scene tableViewScene = new Scene(tableViewParent);


            Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

            CustomerAddModifyController customerAddModifyController = loader.getController();
            customerAddModifyController.setDatabaseConnectionModify(conn, userID, customerList, appointmentsList, selectedCustomer);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        }
        else{errorLabel.setText(rb.getString("NoItem"));}

    }

    /**
     * Passes Customer list, selected Customer and database Connection to confirm delete scene
     * @throws IOException Throws Exception
     */
    public void deleteButtonPushed() throws  IOException {

        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        if(customerTableView.getSelectionModel().getSelectedItem() != null) {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ConfirmCustomerDelete.fxml"));
            Parent tableViewParent = loader.load();

            Scene tableViewScene = new Scene(tableViewParent);
            Stage window = new Stage();

            Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

            ConfirmCustomerDeleteController confirmCustomerDeleteController = loader.getController();
            confirmCustomerDeleteController.setSelectedCustomer(selectedCustomer, conn, customerList, appointmentsList);
            window.setScene(tableViewScene);
            window.show();
        }
        else{errorLabel.setText(rb.getString("NoItem"));}
    }

    /**
     * Passes Lists, userID and database connection to Appointment scene
     * @param event appointment button pushed
     * @throws IOException Throws exception
     */
    public void appointmentsButtonPushed(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AppointmentsUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        AppointmentsUIController appointmentsUIController = loader.getController();
        appointmentsUIController.setDatabaseConnection(conn, userID, customerList, appointmentsList);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * Passes Lists, userID and database connection to Report scene
     * @param event reports button pushed
     * @throws IOException Throws exception
     */
    public void reportsButtonPushed(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ReportsUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        ReportUIController reportUIController = loader.getController();
        reportUIController.setDatabaseConnection(conn, userID, customerList, appointmentsList);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * Sets lists, User ID, database Connection and Customer table.
     * Also checks database for upcoming appointments
     * @param loginUI Database Connection
     * @param user user ID
     * @param customer Customer List
     * @param appointments Appointment List
     */
    public void setDatabaseConnection(Connection loginUI, Integer user, ObservableList<Customer> customer, ObservableList<Appointments> appointments){
        conn = loginUI;
        userID = user;
        customerList = customer;
        appointmentsList = appointments;

        customerTableView.setItems(customerList);
        checkUpcomingAppointments();

    }

    /**
     * Checks database for appointments with 15 mins of User's logon and displays appropriate message
     */
    private void checkUpcomingAppointments(){

        try {
            String checkAppointmentSql = "SELECT * FROM appointments";
            Statement checkAppointmentStmt = conn.createStatement();
            ResultSet checkAppointmentResults = checkAppointmentStmt.executeQuery(checkAppointmentSql);

            Locale locale = Locale.getDefault();
            var rb = ResourceBundle.getBundle("translation",locale);

            Calendar calendar = Calendar.getInstance();
            TimeZone localtimeZone = calendar.getTimeZone();
            LocalDateTime timeNow = LocalDateTime.now();
            ZonedDateTime currentZone = timeNow.atZone(localtimeZone.toZoneId());
            ZonedDateTime databaseZone = currentZone.withZoneSameInstant(ZoneId.of("UTC"));

            LocalDateTime currentTime = currentZone.toLocalDateTime();
            LocalDateTime targetTime = databaseZone.toLocalDateTime();


            int value = 0;
            if (currentTime.isBefore(targetTime))
            {
                while(!currentTime.equals(targetTime))
                {
                    currentTime = currentTime.plusHours(1);
                    value++;
                }
                timeNow = timeNow.plusHours(value);
            }

            if (currentTime.isAfter(targetTime))
            {
                while(!targetTime.equals(currentTime))
                {
                    currentTime = currentTime.plusHours(1);
                    value++;
                }
                timeNow = timeNow.minusHours(value);

            }


            String appointmentTime = "";


            while(checkAppointmentResults.next())
            {
                if(        checkAppointmentResults.getTimestamp("Start", calendar).toLocalDateTime().isAfter(timeNow)
                        && checkAppointmentResults.getTimestamp("Start", calendar).toLocalDateTime().isBefore(timeNow.plusMinutes(15)))
                {
                    int appointmentResult = checkAppointmentResults.getInt("Appointment_ID");
                    for (Appointments appointments : appointmentsList) {
                        if (appointmentResult == appointments.getAppointmentID()) {
                            appointmentTime = appointments.getStart().toString();
                        }
                    }
                    upcomingAppointmentLabel.setText(rb.getString("AppointmentUpcoming"));
                    upcomingAppointmentIDLabel.setText(rb.getString("AppointmentID") + ": " + checkAppointmentResults.getString("Appointment_ID"));
                    upcomingAppointmentDateTimeLabel.setText(rb.getString("Time") + ": " + appointmentTime );

                }
            }
        }
        catch (Exception ex){System.out.println(ex.getMessage());}
    }

    /**
     * Sets Customer Table
     * Sets Labels and Buttons for User's language (English or French)
     */
    public void initialize()
    {
        //Set up Customer table Columns
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerStateColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

        //Set up Language
        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        customersLabel.setText(rb.getString("Customer"));
        upcomingAppointmentLabel.setText(rb.getString("NoUpcomingAppointments"));

        customerIDColumn.setText(rb.getString("ID"));
        customerNameColumn.setText(rb.getString("Name"));
        customerAddressColumn.setText(rb.getString("Address"));
        customerPostalCodeColumn.setText(rb.getString("PostalCode"));
        customerPhoneColumn.setText(rb.getString("Phone"));
        customerStateColumn.setText(rb.getString("State"));
        customerCountryColumn.setText(rb.getString("Country"));

        addButton.setText(rb.getString("Add"));
        updateButton.setText(rb.getString("Update"));
        deleteButton.setText(rb.getString("Delete"));
        appointmentsButton.setText(rb.getString("Appointments"));
        reportsButton.setText(rb.getString("Reports"));
        exitButton.setText(rb.getString("Exit"));


    }

    /**
     * Exits program and closes database Connection
     * @throws SQLException Throws Exception
     */
    public void exitButtonPushed() throws SQLException {
        conn.close();
        System.exit(0);
    }

}
