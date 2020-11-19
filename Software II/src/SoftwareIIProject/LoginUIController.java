package SoftwareIIProject;

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
import java.sql.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LoginUIController {

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

    Connection conn;
    Integer userIDNumber;

    public void loginButton (ActionEvent event) {

        try {

            String userID;
            String password;

            userID = userIDTextField.getText();
            password = passwordTextField.getText();

            String sqlStatement = "SELECT * FROM users";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);


            while(result.next())
            {
                if (userID.equals(result.getString("User_Name")))
                {
                    if (password.equals(result.getString("Password")))
                    {
                        userIDNumber = Integer.parseInt(result.getString("User_ID"));
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("MainUI.fxml"));
                        Parent tableViewParent = loader.load();

                        Scene tableViewScene = new Scene(tableViewParent);

                        MainUIController mainUIController = loader.getController();
                        mainUIController.setDatabaseConnection(conn, userIDNumber);

                        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

                        window.setTitle("Customer Appointment Scheduler");
                        window.setScene(tableViewScene);
                        window.show();
                    }
                    else {loginErrorLabel.setText("Incorrect UserID or Password");}
                }
                else {loginErrorLabel.setText("Incorrect UserID or Password");}
            }
        }
        catch (Exception ex){System.out.println(ex.getMessage());}
    }


    public void exitButtonPushed() throws SQLException {
        conn.close();
        System.out.println("Closed Database");
        System.exit(0);
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
            System.out.println("Connected to Database");
        }
        catch (Exception ex) { System.out.println("Error:" + ex.getMessage()); }

        //Find TimeZone and Country
        Calendar calendar = Calendar.getInstance();
        TimeZone localtimeZone = calendar.getTimeZone();
        localTimeLabel.setText(localtimeZone.getDisplayName());
        Locale country = Locale.getDefault();
        countryLabel.setText(country.getCountry());

        //Set your Language
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
