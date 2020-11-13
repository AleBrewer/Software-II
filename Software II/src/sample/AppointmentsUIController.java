package sample;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;


public class AppointmentsUIController {

    ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();

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


    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("MM-dd-yy hh:mm a");
    Connection conn;
    private Integer userID;


    public void backButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);
        MainUIController mainUIController = loader.getController();
        mainUIController.setDatabaseConnection(conn, userID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }



    private void populateAppointmentsTable()
    {
        try{

            String sqlStatement = "SELECT * FROM appointments";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);
            result.next();

            do{

                String contactSql = "SELECT * FROM contacts WHERE Contact_ID = '" + result.getString("Contact_ID") + "'";
                Statement contactStmt = conn.createStatement();
                ResultSet contactResult = contactStmt.executeQuery(contactSql);
                contactResult.next();

                String customerSql = "SELECT * FROM customers WHERE Customer_ID = '" + result.getString("Customer_ID") + "'";
                Statement customerStmt = conn.createStatement();
                ResultSet customerResult = customerStmt.executeQuery(customerSql);
                customerResult.next();

                appointmentsList.add(new Appointments(
                        result.getInt("Appointment_ID"),
                        result.getString("Title"),
                        result.getString("Description"),
                        result.getString("Location"),
                        result.getString("Type"),
                        result.getTimestamp("Start").toLocalDateTime(),
                        result.getTimestamp("End").toLocalDateTime(),
                        result.getTimestamp("Create_Date").toLocalDateTime(),
                        result.getString("Created_By"),
                        result.getTimestamp("Last_Update").toLocalDateTime(),
                        result.getString("Last_Updated_By"),
                        result.getInt("Customer_ID"),
                        result.getInt("User_ID"),
                        result.getInt("Contact_ID"),
                        contactResult.getString("Contact_Name"),
                        customerResult.getString("Customer_Name")
                        )
                );

            }while(result.next());
            appointmentsTableView.setItems(appointmentsList);
        }
        catch(Exception ex) {System.out.println(ex.getMessage());}
    }


    public void setDatabaseConnection(Connection customerUI, Integer user)
    {
        conn = customerUI;
        userID = user;
        populateAppointmentsTable();

    }

    public void addAppointmentsButton(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AppointmentsAddModifyUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        AppointmentsAddModifyController appointmentsAddModifyController = loader.getController();
        appointmentsAddModifyController.setDatabaseConnectionAdd(conn, userID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();

    }

    public void updateAppointments(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AppointmentsAddModifyUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        Appointments selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        AppointmentsAddModifyController appointmentsAddModifyController = loader.getController();
        appointmentsAddModifyController.setDatabaseConnectionModify(conn, userID,selectedAppointment);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    public void deleteButtonPushed() throws  IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ConfirmAppointmentsDelete.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = new Stage();

        Appointments selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        ConfirmAppointmentsDeleteController confirmAppointmentsDeleteController = loader.getController();
        confirmAppointmentsDeleteController.setSelectedCustomer(selectedAppointment, conn, appointmentsList);

        window.setScene(tableViewScene);
        window.show();

    }


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

    }


}
