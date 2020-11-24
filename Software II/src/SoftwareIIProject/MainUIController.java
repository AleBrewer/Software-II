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
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;


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
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button appointmentsButton;
    @FXML private Button reportsButton;
    @FXML private Button exitButton;
    @FXML private Label errorLabel;


    private Connection conn;
    private Integer userID;

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
            confirmCustomerDeleteController.setSelectedCustomer(selectedCustomer, conn, customerList);
            window.setScene(tableViewScene);
            window.show();
        }
        else{errorLabel.setText(rb.getString("NoItem"));}
    }

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


    public void setDatabaseConnection(Connection loginUI, Integer user, ObservableList<Customer> customer, ObservableList<Appointments> appointments){
        conn = loginUI;
        userID = user;
        customerList = customer;
        appointmentsList = appointments;

        customerTableView.setItems(customerList);
        checkUpcomingAppointments();

    }

    private void checkUpcomingAppointments(){

        try {
            String checkAppointmentSql = "SELECT * FROM appointments";
            Statement checkAppointmentStmt = conn.createStatement();
            ResultSet checkAppointmentResults = checkAppointmentStmt.executeQuery(checkAppointmentSql);

            Locale locale = Locale.getDefault();
            var rb = ResourceBundle.getBundle("translation",locale);
            LocalDateTime timeNow = LocalDateTime.now();
            Calendar calendar = Calendar.getInstance();

            while(checkAppointmentResults.next())
            {
                if(        checkAppointmentResults.getTimestamp("Start", calendar).toLocalDateTime().isAfter(timeNow)
                        && checkAppointmentResults.getTimestamp("Start", calendar).toLocalDateTime().isBefore(timeNow.plusMinutes(15)))
                {
                    upcomingAppointmentLabel.setText(rb.getString("AppointmentUpcoming"));
                }
            }
        }
        catch (Exception ex){System.out.println(ex.getMessage());}
    }


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

    public void exitButtonPushed() throws SQLException {
        conn.close();
        System.exit(0);
    }


}
