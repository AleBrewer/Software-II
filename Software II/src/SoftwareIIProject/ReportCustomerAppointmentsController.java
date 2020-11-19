package SoftwareIIProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;


public class ReportCustomerAppointmentsController {

    private Connection conn;
    private int userID;

    @FXML Label januaryType;
    @FXML Label januaryNumber;
    @FXML Label februaryType;
    @FXML Label februaryNumber;
    @FXML Label marchType;
    @FXML Label marchNumber;
    @FXML Label aprilType;
    @FXML Label aprilNumber;
    @FXML Label mayType;
    @FXML Label mayNumber;
    @FXML Label juneType;
    @FXML Label juneNumber;
    @FXML Label julyType;
    @FXML Label julyNumber;
    @FXML Label augustType;
    @FXML Label augustNumber;
    @FXML Label septemberType;
    @FXML Label septemberNumber;
    @FXML Label octoberType;
    @FXML Label octoberNumber;
    @FXML Label novemberType;
    @FXML Label novemberNumber;
    @FXML Label decemberType;
    @FXML Label decemberNumber;


    private void createReport()
    {
        try {
            String appointmentSql = "SELECT * FROM appointments";
            PreparedStatement appointmentsStmt = conn.prepareStatement(appointmentSql, ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet appointmentsResults = appointmentsStmt.executeQuery(appointmentSql);

            ArrayList<String> appointmentTypes = new ArrayList<>();

            while(appointmentsResults.next()) {

                String newItem = appointmentsResults.getString("Type");
                if (appointmentTypes.contains(newItem)){}
                else{appointmentTypes.add(newItem);}




            }

            System.out.println(appointmentTypes);
            System.out.println(appointmentTypes.size());

            StringBuilder populateType = new StringBuilder();

            for (String appointmentType : appointmentTypes) {
                populateType.append(appointmentType).append("\n");
            }


            januaryType.setText(populateType.toString());

            String output = "";

            for(int i = 0; i < appointmentTypes.size(); i++) {

                appointmentsResults.beforeFirst();
                int counter = 0;
                while (appointmentsResults.next()) {
                    String databaseString = appointmentsResults.getString("Type");
                    if(appointmentTypes.get(i).equals(databaseString))
                    {
                        counter = counter + 1;
                    }
                }
                output = output + counter +"\n";


            }

            januaryNumber.setText(output);



            /*
            while(appointmentsResults.next()) {

                Timestamp monthSort = appointmentsResults.getTimestamp("Start");
                int monthSortNumber = monthSort.toLocalDateTime().getMonthValue();





                if (monthSortNumber == 1)
                {

                }

                */







        }
        catch(Exception ex){System.out.println(ex.getMessage());}


    }


    public void backToAppointments(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ReportsUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        ReportUIController reportUIControllerUIController = loader.getController();
        reportUIControllerUIController.setDatabaseConnection(conn, userID);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    public void setDatabaseConnection(Connection loginUI, Integer user){
        conn = loginUI;
        userID = user;
        createReport();
    }
















}
