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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LoginUIController {

    private final ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
    private final ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML private Label userLoginLabel;
    @FXML private Label userIDLabel;
    @FXML private Label passwordLabel;
    @FXML private Button loginButton;
    @FXML private Button exitButton;
    @FXML private Label localTimeLabel;
    @FXML private Label countryLabel;
    @FXML private Label loginErrorLabel;
    @FXML private TextField userIDTextField;
    @FXML private TextField passwordTextField;

    private Connection conn;


    public void loginButton (ActionEvent event) {

        try {
            Writer loginWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("login_activity.txt", true)));

            String userID;
            String password;

            userID = userIDTextField.getText();
            password = passwordTextField.getText();

            String sqlStatement = "SELECT * FROM users";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            Locale locale = Locale.getDefault();
            var rb = ResourceBundle.getBundle("translation",locale);

            while(result.next())
            {
                if (userID.equals(result.getString("User_Name")))
                {
                    if (password.equals(result.getString("Password")))
                    {
                        populateCustomerList();
                        populateAppointmentsList();
                        loginWriter.write(System.lineSeparator() + "Login Successful at " + LocalDateTime.now());
                        loginWriter.close();

                        Integer userIDNumber = Integer.parseInt(result.getString("User_ID"));
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("MainUI.fxml"));
                        Parent tableViewParent = loader.load();

                        Scene tableViewScene = new Scene(tableViewParent);

                        MainUIController mainUIController = loader.getController();
                        mainUIController.setDatabaseConnection(conn, userIDNumber, customerList, appointmentsList);

                        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

                        window.setScene(tableViewScene);
                        window.show();
                    }

                }

            }

            loginErrorLabel.setText(rb.getString("IncorrectUsernamePassword"));
            loginWriter.write(System.lineSeparator() + "Login failed at " + LocalDateTime.now());
            loginWriter.close();

        }
        catch (Exception ex){System.out.println(ex.getMessage());}
    }


    public void exitButtonPushed() throws SQLException {
        conn.close();
        System.out.println("Closed Database");
        System.exit(0);
    }


    private void populateCustomerList() {
        try {
            String sqlStatement = "SELECT * FROM customers";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);


            while (result.next()) {
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


            }
        }
        catch (Exception ex) { System.out.println("Error:" + ex.getMessage()); }
    }

    private void populateAppointmentsList()
    {
        try{

            Calendar calendar = Calendar.getInstance();

            String sqlStatement = "SELECT * FROM appointments";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);


            while(result.next()){

                String contactSql = "SELECT * FROM contacts WHERE Contact_ID = '" + result.getString("Contact_ID") + "'";
                Statement contactStmt = conn.createStatement();
                ResultSet contactResult = contactStmt.executeQuery(contactSql);
                contactResult.next();

                String customerSql = "SELECT * FROM customers WHERE Customer_ID = '" + result.getString("Customer_ID") + "'";
                Statement customerStmt = conn.createStatement();
                ResultSet customerResult = customerStmt.executeQuery(customerSql);
                customerResult.next();

                appointmentsList.add(new Appointments(
                                result.getInt("Appointment_ID"),
                                result.getString("Title"),
                                result.getString("Description"),
                                result.getString("Location"),
                                result.getString("Type"),
                                result.getTimestamp("Start", calendar).toLocalDateTime(),
                                result.getTimestamp("End", calendar).toLocalDateTime(),
                                result.getTimestamp("Create_Date").toLocalDateTime(),
                                result.getString("Created_By"),
                                result.getTimestamp("Last_Update").toLocalDateTime(),
                                result.getString("Last_Updated_By"),
                                result.getInt("Customer_ID"),
                                result.getInt("User_ID"),
                                result.getInt("Contact_ID"),
                                contactResult.getString("Contact_Name"),
                                customerResult.getString("Customer_Name")
                        )
                );

            }
        }
        catch(Exception ex) {System.out.println(ex.getMessage());}
    }


    public void initialize()
    {
        //Establish Connection with the Database
        try
        {
            String url = "jdbc:mysql://wgudb.ucertify.com:3306/WJ06Xzb";
            String username = "U06Xzb";
            String password = "53688897357";

            conn = DriverManager.getConnection(url, username, password);
        }
        catch (Exception ex) { System.out.println("Error:" + ex.getMessage()); }

        //Find TimeZone and Country
        Calendar calendar = Calendar.getInstance();
        TimeZone localtimeZone = calendar.getTimeZone();
        localTimeLabel.setText(localtimeZone.getDisplayName());
        Locale country = Locale.getDefault();
        countryLabel.setText(country.getCountry());

        //Set your Language
        Locale.setDefault(new Locale("fr"));
        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        userLoginLabel.setText(rb.getString("UserLogin"));
        userIDLabel.setText(rb.getString("UserID"));
        userIDTextField.setPromptText(rb.getString("UserID"));
        passwordLabel.setText(rb.getString("Password"));
        passwordTextField.setPromptText(rb.getString("Password"));
        loginButton.setText(rb.getString("Login"));
        exitButton.setText(rb.getString("Exit"));

    }


}
