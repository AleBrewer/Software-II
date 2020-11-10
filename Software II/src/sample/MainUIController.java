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



public class MainUIController {

    ObservableList<Customer> customerList = FXCollections.observableArrayList();

    //Configure the Customer Table
    @FXML private TableView<Customer> customerTableView;
    @FXML private TableColumn<Customer, Integer> customerIDColumn;
    @FXML private TableColumn<Customer, String> customerNameColumn;
    @FXML private TableColumn<Customer, String> customerAddressColumn;
    @FXML private TableColumn<Customer, String> customerPostalCodeColumn;
    @FXML private TableColumn<Customer, String> customerPhoneColumn;
    @FXML private TableColumn<Customer, String> customerStateColumn;
    @FXML private TableColumn<Customer, String> customerCountryColumn;

    Connection conn;
    private Integer userID;

    public void addButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CustomerAddModifyUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        CustomerAddModifyController customerAddModifyController = loader.getController();
        customerAddModifyController.setDatabaseConnectionAdd(conn, userID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    public void updateButtonPushed(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("CustomerAddModifyUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);


        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        CustomerAddModifyController customerAddModifyController = loader.getController();
        customerAddModifyController.setDatabaseConnectionModify(conn, userID, selectedCustomer);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    public void deleteButtonPushed() throws  IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ConfirmCustomerDelete.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = new Stage();

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        ConfirmCustomerDeleteController confirmCustomerDeleteController = loader.getController();
        confirmCustomerDeleteController.setSelectedCustomer(selectedCustomer, conn, customerList);

        window.setScene(tableViewScene);
        window.show();

    }

    public void appointmentsButtonPushed(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AppointmentsUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        AppointmentsUIController appointmentsUIController = loader.getController();
        appointmentsUIController.setDatabaseConnection(conn, userID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    public void setDatabaseConnection(Connection loginUI, Integer user){
        conn = loginUI;
        userID = user;
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
                String firstDivisionSql = "SELECT * FROM first_level_divisions WHERE Division_ID = '" + result.getString("Division_ID") + "'";
                Statement firstDivisionStmt = conn.createStatement();
                ResultSet firstDivisionResult = firstDivisionStmt.executeQuery(firstDivisionSql);
                firstDivisionResult.next();

                String countrySql = "SELECT * FROM countries WHERE Country_ID = '" + firstDivisionResult.getString("COUNTRY_ID") + "'";
                Statement countryStmt = conn.createStatement();
                ResultSet countryResult = countryStmt.executeQuery(countrySql);
                countryResult.next();

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
                        firstDivisionResult.getString("Division"),
                        countryResult.getString("Country")
                        )
                );

            }while(result.next());

            customerTableView.setItems(customerList);
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
        customerCountryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));

    }

    public void exitButtonPushed() throws SQLException {
        conn.close();
        System.out.println("Closed Database");
        System.exit(0);
    }


}
