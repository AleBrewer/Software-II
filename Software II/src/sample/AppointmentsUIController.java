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
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

public class AppointmentsUIController {

    ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();

    @FXML TableView<Appointments> appointmentsTableView;
    @FXML private TableColumn<Appointments, Integer> applicationIDColumn;
    @FXML private TableColumn<Appointments, String> titleColumn;
    @FXML private TableColumn<Appointments, String> descriptionColumn;
    @FXML private TableColumn<Appointments, String> locationColumn;
    @FXML private TableColumn<Appointments, String> contactNameColumn;
    @FXML private TableColumn<Appointments, String> typeColumn;
    @FXML private TableColumn<Appointments, Date> startColumn;
    @FXML private TableColumn<Appointments, Date> endColumn;
    @FXML private TableColumn<Appointments, Integer> customerIDColumn;
    @FXML private TableColumn<Appointments, String> customerNameColumn;

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
                        result.getDate("Start"),
                        result.getDate("End"),
                        result.getDate("Create_Date"),
                        result.getString("Created_By"),
                        result.getDate("Last_Update"),
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

    public void testAppointmentTime()
    {
        try {
            String sqlStatement = "SELECT * FROM appointments";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);
            result.next();

            System.out.println(result.getTimestamp("Start"));
            System.out.println(result.getTime("Start"));
            System.out.println(result.getTimestamp("Start"));
            
        }
        catch (Exception ex){System.out.println(ex.getMessage());}
    }




    public void initialize()
    {

        applicationIDColumn.setCellValueFactory(new PropertyValueFactory<>("applicationID"));
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