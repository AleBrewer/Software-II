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
import java.sql.*;
import java.time.LocalDateTime;


public class MainUIController {

    ObservableList<Customer> customerList = FXCollections.observableArrayList();

    //Configure the Customer Table
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer,String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerAddressColumn;
    @FXML private TableColumn<Customer, String> customerPostalCodeColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;
    @FXML private TableColumn<Customer, String> customerStateColumn;
    @FXML private TableColumn<Customer, String> customerCountryColumn;


    //LoginUIController databaseConnection = new LoginUIController();

    //String url = "jdbc:mysql://wgudb.ucertify.com:3306/WJ06Xzb";
    //String username = "U06Xzb";
    //String password = "53688897357";

    Connection conn;


    public void addButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CustomerAddModifyUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();


        try {
            Statement stmt = conn.createStatement();
            String sqlStatement = "INSERT INTO customers" +
                    "(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                    "VALUES (4, 'Alex Brewer', '1234 Happy town', '84444','801-123-1234', '11/02/20', 'Alex', '11/02/20', 'Alex', '23')";

            int rows = stmt.executeUpdate(sqlStatement);

            System.out.println(sqlStatement);
            System.out.println("Add Button");
        }
        catch (Exception ex) {System.out.println("Error: " + ex.getMessage());}

    }




    public void updateButtonPushed()
    {
        System.out.println("Update Button");
    }

    public void deleteButtonPushed()
    {
        System.out.println("Delete Pushed");
    }

    public void appointmentsButtonPushed()
    {
        System.out.println("Appointments pushed");
    }





    public void setDatabaseConnection(Connection loginUI){
        conn = loginUI;
        populateCustomerTable();
    }


    private void populateCustomerTable()
    {
        try {
            String sqlStatement = "SELECT * FROM customers";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            result.next();

            do
            {
                customerList.add(new Customer(
                                result.getInt("Customer_ID"),
                                result.getString("Customer_Name"),
                                result.getString("Address"),
                                result.getString("Postal_Code"),
                                result.getString("Phone"),
                                result.getDate("Create_Date"),
                                result.getString("Created_By"),
                                result.getDate("Last_Update"),
                                result.getString("Last_Updated_By"),
                                result.getString("Division_ID")
                        )
                );

                customerTableView.setItems(customerList);

            }while(result.next());
        }
        catch (Exception ex){System.out.println("Error:" + ex.getMessage());}
    }


    public void initialize()
    {
        //Set up Customer table Columns
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerStateColumn.setCellValueFactory(new PropertyValueFactory<>("division"));
        //customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("Country"));



    }

    public void exitButtonPushed() throws SQLException {
        conn.close();
        System.out.println("Closed Database");
        System.exit(0);
    }


}
