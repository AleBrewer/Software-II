package sample;


import com.mysql.cj.log.Log;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;


public class CustomerAddModifyController {

    @FXML ComboBox firstLevelDivisionComboBox;
    @FXML ComboBox countryComboBox;

    LoginUIController databaseConnection = new LoginUIController();

    public void exitButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    public void initialize()
    {
        try {
            String sqlStatement = "SELECT * FROM countries";
            Statement stmt = databaseConnection.conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);

            result.next();

            System.out.println(result.getString("Country"));

            //do {
            //    countryComboBox.getItems().add(result.getString("Country"));
            //} while (result.next());
        }
        catch (Exception ex){System.out.println("Error: " + ex.getMessage()); }




    }

}
