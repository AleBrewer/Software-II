package SoftwareIIProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

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


    public void setDatabaseConnection(Connection customerUI, Integer user, ObservableList<Customer> customer, ObservableList<Appointments> appointments)
    {
        conn = customerUI;
        userID = user;
        customerList = customer;
        appointmentsList = appointments;
        setContactComboBox();
        appointmentsTableView.setItems(appointmentsList);
    }

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
