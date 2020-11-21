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
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReportUIController {

    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();

    private Connection conn;
    private int userID;

    @FXML Label reportLabel;
    @FXML Label firstReportTopLabel;
    @FXML Label firstReportBottomLabel;
    @FXML Label secondReportLabel;
    @FXML Label thirdReportLabel;
    @FXML Button viewButtonOne;
    @FXML Button viewButtonTwo;
    @FXML Button viewButtonThree;
    @FXML Button backButton;

    public void reportsCustomerAppointmentsViewButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ReportCustomerAppointmentsUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        ReportCustomerAppointmentsController reportCustomerAppointmentsController = loader.getController();
        reportCustomerAppointmentsController.setDatabaseConnection(conn, userID, customerList, appointmentsList);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    public void reportContactScheduleReportViewButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ReportsContactSchedule.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        ReportsContactScheduleController reportsContactScheduleController = loader.getController();
        reportsContactScheduleController.setDatabaseConnection(conn, userID, customerList, appointmentsList);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    public void reportCustomerPerCountryViewButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ReportCustomerPerCountry.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        ReportCustomerPerCountryController reportCustomerPerCountryController = loader.getController();
        reportCustomerPerCountryController.setDatabaseConnection(conn, userID, customerList, appointmentsList);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    public void backToAppointments(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        MainUIController mainUIController = loader.getController();
        mainUIController.setDatabaseConnection(conn, userID, customerList, appointmentsList);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    public void setDatabaseConnection(Connection loginUI, Integer user, ObservableList<Customer> customers, ObservableList<Appointments> appointments){
        conn = loginUI;
        userID = user;
        customerList = customers;
        appointmentsList = appointments;

    }

    public void initialize()
    {
        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        reportLabel.setText(rb.getString("Reports"));
        firstReportTopLabel.setText(rb.getString("FirstReportTop"));
        firstReportBottomLabel.setText(rb.getString("FirstReportBottom"));
        secondReportLabel.setText(rb.getString("SecondReport"));
        thirdReportLabel.setText(rb.getString("ThirdReport"));
        viewButtonOne.setText(rb.getString("View"));
        viewButtonTwo.setText(rb.getString("View"));
        viewButtonThree.setText(rb.getString("View"));
        backButton.setText(rb.getString("Back"));

    }

}
