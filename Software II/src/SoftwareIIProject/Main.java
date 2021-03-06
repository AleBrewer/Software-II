package SoftwareIIProject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class that start the program and sets Login UI Scene
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LoginUI.fxml"));
        primaryStage.setScene(new Scene(root, 508, 287));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
