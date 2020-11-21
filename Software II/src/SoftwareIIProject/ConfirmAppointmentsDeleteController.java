package SoftwareIIProject;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConfirmAppointmentsDeleteController {

    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    @FXML private Appointments selectedAppointment;

    private Connection conn;

    ObservableList<Appointments> currentViewList;
    ObservableList<Appointments> secondaryViewList;
    ObservableList<Appointments> masterList;
    TableView<Appointments> appointmentsTableView;

    @FXML private Label confirmDeleteLabel;
    @FXML private Label topLabel;
    @FXML private Label bottomLabel;


    @FXML private void cancelButtonPushed(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML private void deleteButtonPushed() {

        try {
            String sqlStatement = "DELETE FROM appointments WHERE Appointment_ID = '" + selectedAppointment.getAppointmentID() + "'";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlStatement);
        }
        catch(Exception ex){System.out.println(ex.getMessage());}

        currentViewList.remove(selectedAppointment);
        secondaryViewList.remove(selectedAppointment);
        masterList.remove(selectedAppointment);

        appointmentsTableView.setItems(currentViewList);

        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }

    public void setSelectedCustomer(Appointments customer, Connection databaseConnection, ObservableList<Appointments> currentView,
                                    ObservableList<Appointments> secondaryView, ObservableList<Appointments> master,
                                    TableView<Appointments> appointmentsTable) {

        selectedAppointment = customer;
        conn = databaseConnection;
        currentViewList = currentView;
        secondaryViewList = secondaryView;
        masterList = master;
        appointmentsTableView = appointmentsTable;

    }


    public void initialize() {

        //Set Language
        //Locale.setDefault(new Locale("fr"));
        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        confirmDeleteLabel.setText(rb.getString("ConfirmDelete"));
        topLabel.setText(rb.getString("DeleteCustomerTop"));
        bottomLabel.setText(rb.getString("DeleteAppointmentBottom"));
        deleteButton.setText(rb.getString("Delete"));
        cancelButton.setText(rb.getString("Cancel"));

    }

}

