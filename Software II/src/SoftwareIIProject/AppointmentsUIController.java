package SoftwareIIProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;


public class AppointmentsUIController {

    ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();

    @FXML TableView<Appointments> appointmentsTableView;
    @FXML private TableColumn<Appointments, Integer> appointmentIDColumn;
    @FXML private TableColumn<Appointments, String> titleColumn;
    @FXML private TableColumn<Appointments, String> descriptionColumn;
    @FXML private TableColumn<Appointments, String> locationColumn;
    @FXML private TableColumn<Appointments, String> contactNameColumn;
    @FXML private TableColumn<Appointments, String> typeColumn;
    @FXML private TableColumn<Appointments, Date> startColumn;
    @FXML private TableColumn<Appointments, Date> endColumn;
    @FXML private TableColumn<Appointments, Integer> customerIDColumn;
    @FXML private TableColumn<Appointments, String> customerNameColumn;

    @FXML private ToggleGroup appointmentFilter;
    @FXML private RadioButton currentMonth;
    @FXML private RadioButton currentWeek;
    ObservableList<Appointments> currentMonthList = FXCollections.observableArrayList();
    ObservableList<Appointments> currentWeekList = FXCollections.observableArrayList();


    Connection conn;
    private Integer userID;


    public void backButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);
        MainUIController mainUIController = loader.getController();
        mainUIController.setDatabaseConnection(conn, userID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }

    private void populateAppointmentsList()
    {
        try{

            Calendar calendar = Calendar.getInstance();

            String sqlStatement = "SELECT * FROM appointments";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);
            result.next();

            do{

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

            }while(result.next());
        }
        catch(Exception ex) {System.out.println(ex.getMessage());}
    }


    public void setDatabaseConnection(Connection customerUI, Integer user)
    {
        conn = customerUI;
        userID = user;
        populateAppointmentsList();
        setTableFilterLists();
        appointmentsTableView.setItems(currentMonthList);

    }

    public void addAppointmentsButton(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AppointmentsAddModifyUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        AppointmentsAddModifyController appointmentsAddModifyController = loader.getController();
        appointmentsAddModifyController.setDatabaseConnectionAdd(conn, userID);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();

    }

    public void updateAppointments(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AppointmentsAddModifyUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        Appointments selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        AppointmentsAddModifyController appointmentsAddModifyController = loader.getController();
        appointmentsAddModifyController.setDatabaseConnectionModify(conn, userID,selectedAppointment);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    public void deleteButtonPushed() throws  IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ConfirmAppointmentsDelete.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);
        Stage window = new Stage();

        Appointments selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();

        ConfirmAppointmentsDeleteController confirmAppointmentsDeleteController = loader.getController();
        confirmAppointmentsDeleteController.setSelectedCustomer(selectedAppointment, conn, appointmentsList);

        window.setScene(tableViewScene);
        window.show();

    }

    public void updateTableByFilter()
    {
        if(appointmentFilter.getSelectedToggle().equals(currentMonth))
        {
            appointmentsTableView.setItems(currentMonthList);
        }

        if(appointmentFilter.getSelectedToggle().equals(currentWeek))
        {
            appointmentsTableView.setItems(currentWeekList);
        }

    }

    private void setTableFilterLists()
    {
        int currentUserMonthValue = LocalDate.now().getMonthValue();

        for(int i = 0; i < appointmentsList.toArray().length;i++)
        {
            int appointmentMonth = appointmentsList.get(i).getStart().getMonthValue();
            if (appointmentMonth == currentUserMonthValue){currentMonthList.add(appointmentsList.get(i));}
        }

        LocalDate weekStart;
        LocalDate weekEnd;

        LocalDateTime timeNow = LocalDateTime.now();
        String dayOfWeek = timeNow.getDayOfWeek().toString();

        System.out.println(dayOfWeek);

        switch (dayOfWeek) {
            case "Sunday":
                weekStart = timeNow.toLocalDate();
                weekEnd = timeNow.toLocalDate().plusDays(6);
                break;
            case "MONDAY":
                weekStart = timeNow.toLocalDate().minusDays(1);
                weekEnd = timeNow.toLocalDate().plusDays(5);
                break;
            case "TUESDAY":
                weekStart = timeNow.toLocalDate().minusDays(2);
                weekEnd = timeNow.toLocalDate().plusDays(4);
                break;
            case "WEDNESDAY":
                weekStart = timeNow.toLocalDate().minusDays(3);
                weekEnd = timeNow.toLocalDate().plusDays(3);
                break;
            case "THURSDAY":
                weekStart = timeNow.toLocalDate().minusDays(4);
                weekEnd = timeNow.toLocalDate().plusDays(2);
                break;
            case "FRIDAY":
                weekStart = timeNow.toLocalDate().minusDays(5);
                weekEnd = timeNow.toLocalDate().plusDays(1);
                break;
            case "SATURDAY":
                weekStart = timeNow.toLocalDate().minusDays(6);
                weekEnd = timeNow.toLocalDate();
                break;
            default:
                weekStart = timeNow.toLocalDate();
                weekEnd = timeNow.toLocalDate();
                break;
        }


        System.out.println(weekStart.toString());
        System.out.println(weekEnd.toString());

        for(int i = 0; i < appointmentsList.toArray().length;i++)

        {
            LocalDate dateCheck = appointmentsList.get(i).getStart().toLocalDate();
            if(dateCheck.isAfter(weekStart) && dateCheck.isBefore(weekEnd)){currentWeekList.add(appointmentsList.get(i));}
        }

    }



    public void initialize()
    {

        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactNameColumn.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));

        appointmentFilter = new ToggleGroup();
        currentMonth.setToggleGroup(appointmentFilter);
        currentWeek.setToggleGroup(appointmentFilter);
        currentMonth.setSelected(true);

    }


}
