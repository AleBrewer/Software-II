package SoftwareIIProject;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Statement;

public class ConfirmAppointmentsDeleteController {

    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    @FXML private Appointments selectedAppointment;

    private Connection conn;
    ObservableList<Appointments> appointmentsList;


    @FXML private void cancelButtonPushed(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML private void deleteButtonPushed(){
        try {
            String sqlStatement = "DELETE FROM appointments WHERE Appointment_ID = '" + selectedAppointment.getAppointmentID() + "'";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlStatement);
        }
        catch(Exception ex){System.out.println(ex.getMessage());}

        appointmentsList.remove(selectedAppointment);
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }


    public void setSelectedCustomer(Appointments customer, Connection databaseConnection, ObservableList<Appointments> list) {
        selectedAppointment = customer;
        conn = databaseConnection;
        appointmentsList = list;
    }



}

