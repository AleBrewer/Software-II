package sample;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Statement;

public class ConfirmCustomerDeleteController {

    @FXML private Button cancelButton;
    @FXML private Button deleteButton;

    @FXML private Customer selectedCustomer;

    private Connection conn;
    ObservableList<Customer> customerList;


    @FXML private void cancelButtonPushed(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML private void deleteButtonPushed(){
        try {
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

}
