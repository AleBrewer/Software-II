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

/**
 * Confirm Appointments Delete Window Controller
 */
public class ConfirmAppointmentsDeleteController {

    @FXML private Button cancelButton;
    @FXML private Button deleteButton;
    @FXML private Appointments selectedAppointment;

    private Connection conn;

    //Filtered lists and Appointments List
    private ObservableList<Appointments> currentViewList;
    private ObservableList<Appointments> secondaryViewList;
    private ObservableList<Appointments> masterList;
    private TableView<Appointments> appointmentsTableView;

    @FXML private Label confirmDeleteLabel;
    @FXML private Label topLabel;
    @FXML private Label bottomLabel;
    @FXML private Label appointmentID;
    @FXML private Label appointmentType;

    /**
     * Closes window if Cancel Button is pushed
     */
    @FXML private void cancelButtonPushed(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Deletes selected item from the Database, week filtered list(if applicable), month filtered list, and appointments list
     */
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

    /**
     * Sets all the Lists
     * @param appointment Sets Selected Appointment
     * @param databaseConnection Sets Database connection
     * @param currentView Sets list currently being viewed (Month filter or week filter)
     * @param secondaryView (Sets the list not being viewed (Month filter or week filter)
     * @param master (Sets the Appointments list)
     * @param appointmentsTable (Sets Appointment Tableview)
     */
    public void setSelectedCustomer(Appointments appointment, Connection databaseConnection, ObservableList<Appointments> currentView,
                                    ObservableList<Appointments> secondaryView, ObservableList<Appointments> master,
                                    TableView<Appointments> appointmentsTable) {

        selectedAppointment = appointment;
        conn = databaseConnection;
        currentViewList = currentView;
        secondaryViewList = secondaryView;
        masterList = master;
        appointmentsTableView = appointmentsTable;
        setAppointmentInformation();

    }

    /**
     * Sets Languages for all the Labels and Buttons
     */

    private void setAppointmentInformation(){
        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        appointmentID.setText(rb.getString("AppointmentID") + ": " + selectedAppointment.getAppointmentID());
        appointmentType.setText(rb.getString("Type") + ": " + selectedAppointment.getType());
    }


    public void initialize() {

        //Set Language
        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        confirmDeleteLabel.setText(rb.getString("ConfirmDelete"));
        topLabel.setText(rb.getString("DeleteCustomerTop"));
        bottomLabel.setText(rb.getString("DeleteAppointmentBottom"));
        deleteButton.setText(rb.getString("Delete"));
        cancelButton.setText(rb.getString("Cancel"));
    }

}

