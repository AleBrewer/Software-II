package SoftwareIIProject;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Locale;
import java.util.ResourceBundle;

public class ConfirmCustomerDeleteController {

    @FXML private Button cancelButton;
    @FXML private Button deleteButton;

    @FXML private Label confirmDeleteLabel;
    @FXML private Label topLabel;
    @FXML private Label bottomLabel;

    @FXML private Customer selectedCustomer;

    private Connection conn;
    ObservableList<Customer> customerList;


    @FXML private void cancelButtonPushed(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML private void deleteButtonPushed(){
        try {

            String deleteAppointmentsSql = "DELETE FROM appointments WHERE Customer_ID = '" + selectedCustomer.getId() +"'";
            Statement deleteStmt = conn.createStatement();
            deleteStmt.executeUpdate(deleteAppointmentsSql);

            String sqlStatement = "DELETE FROM customers WHERE Customer_ID = '" + selectedCustomer.getId() + "'";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sqlStatement);

        }
        catch(Exception ex){System.out.println(ex.getMessage());}

        customerList.remove(selectedCustomer);
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }


    public void setSelectedCustomer(Customer customer, Connection databaseConnection, ObservableList<Customer> list) {
        selectedCustomer = customer;
        conn = databaseConnection;
        customerList = list;
    }

    public void initialize() {

        //Set Language
        //Locale.setDefault(new Locale("fr"));
        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        confirmDeleteLabel.setText(rb.getString("ConfirmDelete"));
        topLabel.setText(rb.getString("DeleteCustomerTop"));
        bottomLabel.setText(rb.getString("DeleteCustomerBottom"));
        deleteButton.setText(rb.getString("Delete"));
        cancelButton.setText(rb.getString("Cancel"));

    }
}
