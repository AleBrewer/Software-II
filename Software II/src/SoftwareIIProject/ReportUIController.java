package SoftwareIIProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class ReportUIController {

    private Connection conn;
    private int userID;


    public void reportsCustomerAppointmentsViewButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ReportCustomerAppointmentsUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        ReportCustomerAppointmentsController reportCustomerAppointmentsController = loader.getController();
        reportCustomerAppointmentsController.setDatabaseConnection(conn, userID);

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
        mainUIController.setDatabaseConnection(conn, userID);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    public void setDatabaseConnection(Connection loginUI, Integer user){
        conn = loginUI;
        userID = user;
    }


}
