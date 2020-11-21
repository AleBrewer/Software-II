package SoftwareIIProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;

public class ReportCustomerPerCountryController {

    private ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private Connection conn;
    private Integer userID;

    @FXML private Label usCountLabel;
    @FXML private Label ukCountLabel;
    @FXML private Label canadaCountLabel;

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

    private void setCountryLabels()
    {
        int usCount = 0;
        int ukCount = 0;
        int canadaCount = 0;

        for (Customer customer : customerList) {
            if (customer.getCountry().equals("U.S")) { usCount++; }
            if (customer.getCountry().equals("UK")) { ukCount++; }
            if (customer.getCountry().equals("Canada")) { canadaCount++; }
        }

        usCountLabel.setText(Integer.toString(usCount));
        ukCountLabel.setText(Integer.toString(ukCount));
        canadaCountLabel.setText(Integer.toString(canadaCount));

    }

    public void setDatabaseConnection(Connection customerUI, Integer user, ObservableList<Customer> customer, ObservableList<Appointments> appointments)
    {
        conn = customerUI;
        userID = user;
        customerList = customer;
        appointmentsList = appointments;
        setCountryLabels();
    }






}
