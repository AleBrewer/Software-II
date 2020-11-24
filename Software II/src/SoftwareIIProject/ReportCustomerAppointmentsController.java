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
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for Customer Appointments Report
 */
public class ReportCustomerAppointmentsController {

    private Connection conn;
    private int userID;

    @FXML Button backButton;
    @FXML Label customerAppointmentLabel;

    @FXML Label januaryType;      @FXML Label januaryNumber;
    @FXML Label februaryType;     @FXML Label februaryNumber;
    @FXML Label marchType;        @FXML Label marchNumber;
    @FXML Label aprilType;        @FXML Label aprilNumber;
    @FXML Label mayType;          @FXML Label mayNumber;
    @FXML Label juneType;         @FXML Label juneNumber;
    @FXML Label julyType;         @FXML Label julyNumber;
    @FXML Label augustType;       @FXML Label augustNumber;
    @FXML Label septemberType;    @FXML Label septemberNumber;
    @FXML Label octoberType;      @FXML Label octoberNumber;
    @FXML Label novemberType;     @FXML Label novemberNumber;
    @FXML Label decemberType;     @FXML Label decemberNumber;

    @FXML Label januaryTypeLabel;      @FXML Label januaryNumberLabel;
    @FXML Label februaryTypeLabel;     @FXML Label februaryNumberLabel;
    @FXML Label marchTypeLabel;        @FXML Label marchNumberLabel;
    @FXML Label aprilTypeLabel;        @FXML Label aprilNumberLabel;
    @FXML Label mayTypeLabel;          @FXML Label mayNumberLabel;
    @FXML Label juneTypeLabel;         @FXML Label juneNumberLabel;
    @FXML Label julyTypeLabel;         @FXML Label julyNumberLabel;
    @FXML Label augustTypeLabel;       @FXML Label augustNumberLabel;
    @FXML Label septemberTypeLabel;    @FXML Label septemberNumberLabel;
    @FXML Label octoberTypeLabel;      @FXML Label octoberNumberLabel;
    @FXML Label novemberTypeLabel;     @FXML Label novemberNumberLabel;
    @FXML Label decemberTypeLabel;     @FXML Label decemberNumberLabel;

    @FXML Label januaryLabel;
    @FXML Label februaryLabel;
    @FXML Label marchLabel;
    @FXML Label aprilLabel;
    @FXML Label mayLabel;
    @FXML Label juneLabel;
    @FXML Label julyLabel;
    @FXML Label augustLabel;
    @FXML Label septemberLabel;
    @FXML Label octoberLabel;
    @FXML Label novemberLabel;
    @FXML Label decemberLabel;


    private ObservableList<Customer> customerList = FXCollections.observableArrayList();
    private ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
    private final ArrayList<String> appointmentTypesList = new ArrayList<>();

    /**
     * Creates report and outputs Types and quantities of appointments for each month
     * Lambda Expression used here to create a Types list and ensure there aren't duplicates for the same type
     */
    private void createReport()
    {

        appointmentsList.forEach( n -> { String newItem = n.getType();

            if (!appointmentTypesList.contains(newItem))
            {appointmentTypesList.add(n.getType());}
        });



        ObservableList<Appointments> janList = FXCollections.observableArrayList();
        ObservableList<Appointments> febList = FXCollections.observableArrayList();
        ObservableList<Appointments> marList = FXCollections.observableArrayList();
        ObservableList<Appointments> aprList = FXCollections.observableArrayList();
        ObservableList<Appointments> mayList = FXCollections.observableArrayList();
        ObservableList<Appointments> junList = FXCollections.observableArrayList();
        ObservableList<Appointments> julList = FXCollections.observableArrayList();
        ObservableList<Appointments> augList = FXCollections.observableArrayList();
        ObservableList<Appointments> sepList = FXCollections.observableArrayList();
        ObservableList<Appointments> octList = FXCollections.observableArrayList();
        ObservableList<Appointments> novList = FXCollections.observableArrayList();
        ObservableList<Appointments> decList = FXCollections.observableArrayList();


        for (Appointments appointments : appointmentsList) {
            int monthValue = appointments.getStart().getMonthValue();

            if (monthValue == 1) {
                janList.add(appointments);
            }
            if (monthValue == 2) {
                febList.add(appointments);
            }
            if (monthValue == 3) {
                marList.add(appointments);
            }
            if (monthValue == 4) {
                aprList.add(appointments);
            }
            if (monthValue == 5) {
                mayList.add(appointments);
            }
            if (monthValue == 6) {
                junList.add(appointments);
            }
            if (monthValue == 7) {
                julList.add(appointments);
            }
            if (monthValue == 8) {
                augList.add(appointments);
            }
            if (monthValue == 9) {
                sepList.add(appointments);
            }
            if (monthValue == 10) {
                octList.add(appointments);
            }
            if (monthValue == 11) {
                novList.add(appointments);
            }
            if (monthValue == 12) {
                decList.add(appointments);
            }
        }

        ArrayList<String> janValues = getTypeAndNumber(janList);
        januaryType.setText(janValues.get(0));
        januaryNumber.setText(janValues.get(1));


        ArrayList<String> febValues = getTypeAndNumber(febList);
        februaryType.setText(febValues.get(0));
        februaryNumber.setText(febValues.get(1));

        ArrayList<String> marValues = getTypeAndNumber(marList);
        marchType.setText(marValues.get(0));
        marchNumber.setText(marValues.get(1));

        ArrayList<String> aprValues = getTypeAndNumber(aprList);
        aprilType.setText(aprValues.get(0));
        aprilNumber.setText(aprValues.get(1));

        ArrayList<String> mayValues = getTypeAndNumber(mayList);
        mayType.setText(mayValues.get(0));
        mayNumber.setText(mayValues.get(1));

        ArrayList<String> juneValues = getTypeAndNumber(junList);
        juneType.setText(juneValues.get(0));
        juneNumber.setText(juneValues.get(1));

        ArrayList<String> julValues = getTypeAndNumber(julList);
        julyType.setText(julValues.get(0));
        julyNumber.setText(julValues.get(1));

        ArrayList<String> augValues = getTypeAndNumber(augList);
        augustType.setText(augValues.get(0));
        augustNumber.setText(augValues.get(1));

        ArrayList<String> sepValues = getTypeAndNumber(sepList);
        septemberType.setText(sepValues.get(0));
        septemberNumber.setText(sepValues.get(1));

        ArrayList<String> octValues = getTypeAndNumber(octList);
        octoberType.setText(octValues.get(0));
        octoberNumber.setText(octValues.get(1));

        ArrayList<String> novValues = getTypeAndNumber(novList);
        novemberType.setText(novValues.get(0));
        novemberNumber.setText(novValues.get(1));

        ArrayList<String> decValues = getTypeAndNumber(decList);
        decemberType.setText(decValues.get(0));
        decemberNumber.setText(decValues.get(1));

    }


    /**
     * Returns User to reports Scene
     * @param event Back button Pushed
     * @throws IOException throws exception
     */
    public void backToAppointments(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ReportsUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        ReportUIController reportUIControllerUIController = loader.getController();
        reportUIControllerUIController.setDatabaseConnection(conn, userID, customerList, appointmentsList);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    /**
     * Sets database Connection, lists, and user ID. Starts report running
     * @param loginUI Database connection
     * @param user User ID
     * @param customers Customer List
     * @param appointments Appointment List
     */
    public void setDatabaseConnection(Connection loginUI, Integer user, ObservableList<Customer> customers, ObservableList<Appointments> appointments){
        conn = loginUI;
        userID = user;
        customerList = customers;
        appointmentsList = appointments;
        createReport();
    }

    /**
     * Checks appointment list for Type and Quantity for each month and returns String Array with all type and quantities in order
     * @param monthSelected Month of year to check
     * @return String array with all types that month and the quantities of each Type
     */
    private ArrayList<String> getTypeAndNumber(ObservableList<Appointments> monthSelected){

        StringBuilder typeString = new StringBuilder();
        StringBuilder count = new StringBuilder();
        int countInt;

        for (String typeCheck : appointmentTypesList) {
            countInt = 0;

            for (Appointments appointments : monthSelected) {
                if (appointments.getType().equals(typeCheck)) {
                    countInt++;
                    if (!typeString.toString().contains(typeCheck)) {
                        typeString.append(typeCheck).append("\n");
                    }
                }
            }
            if (countInt > 0) {
                count.append(countInt).append("\n");
            }
        }

        ArrayList<String> returnValues = new ArrayList<>();
        returnValues.add(String.valueOf(typeString));
        returnValues.add(String.valueOf(count));

        return returnValues;

    }

    /**
     * Sets labels for User's language (English or French)
     */
    public void initialize() {
        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation", locale);

        januaryTypeLabel.setText(rb.getString("Type"));     januaryNumberLabel.setText(rb.getString("Number"));
        februaryTypeLabel.setText(rb.getString("Type"));    februaryNumberLabel.setText(rb.getString("Number"));
        marchTypeLabel.setText(rb.getString("Type"));       marchNumberLabel.setText(rb.getString("Number"));
        aprilTypeLabel.setText(rb.getString("Type"));       aprilNumberLabel.setText(rb.getString("Number"));
        mayTypeLabel.setText(rb.getString("Type"));         marchNumberLabel.setText(rb.getString("Number"));
        juneTypeLabel.setText(rb.getString("Type"));        juneNumberLabel.setText(rb.getString("Number"));
        julyTypeLabel.setText(rb.getString("Type"));        julyNumberLabel.setText(rb.getString("Number"));
        augustTypeLabel.setText(rb.getString("Type"));      augustNumberLabel.setText(rb.getString("Number"));
        septemberTypeLabel.setText(rb.getString("Type"));   septemberNumberLabel.setText(rb.getString("Number"));
        octoberTypeLabel.setText(rb.getString("Type"));     octoberNumberLabel.setText(rb.getString("Number"));
        novemberTypeLabel.setText(rb.getString("Type"));    novemberNumberLabel.setText(rb.getString("Number"));
        decemberTypeLabel.setText(rb.getString("Type"));    decemberNumberLabel.setText(rb.getString("Number"));

        januaryLabel.setText(rb.getString("January"));
        februaryLabel.setText(rb.getString("February"));
        marchLabel.setText(rb.getString("March"));
        aprilLabel.setText(rb.getString("April"));
        mayLabel.setText(rb.getString("May"));
        juneLabel.setText(rb.getString("June"));
        julyLabel.setText(rb.getString("July"));
        augustLabel.setText(rb.getString("August"));
        septemberLabel.setText(rb.getString("September"));
        octoberLabel.setText(rb.getString("October"));
        novemberLabel.setText(rb.getString("November"));
        decemberLabel.setText(rb.getString("December"));

        customerAppointmentLabel.setText(rb.getString("CustomerAppointments"));
        backButton.setText(rb.getString("Back"));

    }













}
