package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class LoginUIController {

    String url = "jdbc:mysql://wgudb.ucertify.com:3306/WJ06Xzb";
    String username = "U06Xzb";
    String password = "53688897357";

    Connection conn;


    public void loginButton (ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        MainUIController mainUIController = loader.getController();
        mainUIController.setDatabaseConnection(conn);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    public void exitButtonPushed() throws SQLException {
        conn.close();
        System.out.println("Closed Database");
        System.exit(0);
    }


    public void initialize()
    {
        try
        {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to Database");
        }
        catch (Exception ex) { System.out.println("Error:" + ex.getMessage()); }
    }
}
