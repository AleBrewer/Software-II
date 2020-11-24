package SoftwareIIProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Controller for Appointments UI
 */
public class AppointmentsUIController {

    private ObservableList<Appointments> appointmentsList = FXCollections.observableArrayList();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    //Set TableView
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

    //Set Toggle Group
    @FXML private ToggleGroup appointmentFilter;
    @FXML private RadioButton currentMonth;
    @FXML private RadioButton currentWeek;
    private final ObservableList<Appointments> currentMonthList = FXCollections.observableArrayList();
    private final ObservableList<Appointments> currentWeekList = FXCollections.observableArrayList();


    @FXML private Label appointmentLabel;
    @FXML private Label viewByLabel;
    @FXML private Button addButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;
    @FXML private Label errorLabel;

    private Connection conn;
    private Integer userID;

    /**
     * Returns User to Main UI
     * @param event Back Button Pushed
     * @throws IOException Throw Exception
     */
    public void backButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("MainUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);
        MainUIController mainUIController = loader.getController();
        mainUIController.setDatabaseConnection(conn, userID, customerList, appointmentsList);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();
    }


    /**
     * Takes User the the Add Appointment Scene, passes Database Connection, UserID, CustomerList, and Appointment List
     * @param event Add Button Pushed
     * @throws IOException Throws Exception
     */
    public void addAppointmentsButton(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AppointmentsAddModifyUI.fxml"));
        Parent tableViewParent = loader.load();

        Scene tableViewScene = new Scene(tableViewParent);

        AppointmentsAddModifyController appointmentsAddModifyController = loader.getController();
        appointmentsAddModifyController.setDatabaseConnectionAdd(conn, userID, customerList, appointmentsList);

        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(tableViewScene);
        window.show();

    }

    /**
     * Takes User the the Update Appointment Scene, passes Database Connection, UserID, CustomerList, and Appointment List
     * @param event Add Button Pushed
     * @throws IOException Throws Exception
     */
    public void updateAppointments(ActionEvent event) throws IOException
    {
        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        if(appointmentsTableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("AppointmentsAddModifyUI.fxml"));
            Parent tableViewParent = loader.load();

            Scene tableViewScene = new Scene(tableViewParent);

            Appointments selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();
            AppointmentsAddModifyController appointmentsAddModifyController = loader.getController();
            appointmentsAddModifyController.setDatabaseConnectionModify(conn, userID, customerList, appointmentsList, selectedAppointment);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(tableViewScene);
            window.show();
        }
        else{errorLabel.setText(rb.getString("NoItem"));}
    }

    /**
     * Opens the Confirm delete Scene and passes the selected Appointment and Appointment List
     * @throws IOException Throws Exception
     */
    public void deleteButtonPushed() throws  IOException {

        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        if(appointmentsTableView.getSelectionModel().getSelectedItem() != null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("ConfirmAppointmentsDelete.fxml"));
            Parent tableViewParent = loader.load();

            Scene tableViewScene = new Scene(tableViewParent);
            Stage window = new Stage();

            Appointments selectedAppointment = appointmentsTableView.getSelectionModel().getSelectedItem();

            ConfirmAppointmentsDeleteController confirmAppointmentsDeleteController = loader.getController();
            if (currentMonth.isSelected()) {
                confirmAppointmentsDeleteController.setSelectedCustomer(selectedAppointment, conn, currentMonthList, currentWeekList, appointmentsList, appointmentsTableView);
            }
            if (currentWeek.isSelected()) {
                confirmAppointmentsDeleteController.setSelectedCustomer(selectedAppointment, conn, currentWeekList, currentMonthList, appointmentsList, appointmentsTableView);
            }

            window.setScene(tableViewScene);
            window.show();
        }
        else{errorLabel.setText(rb.getString("NoItem"));}
    }

    /**
     * Updates tables based on radio button filter selected.
     */
    public void updateTableByFilter() {
        if (appointmentFilter.getSelectedToggle().equals(currentMonth))
        {
            appointmentsTableView.setItems(currentMonthList);
        }

        if (appointmentFilter.getSelectedToggle().equals(currentWeek))
        {
            appointmentsTableView.setItems(currentWeekList);
        }
    }

    /**
     * Sets the needed Lists, Numbers and Connection. Runs the Table filter and sets the tableView
     * @param customerUI Sets Database Connection
     * @param user Sets User Id
     * @param customer Sets Customer List
     * @param appointments Sets Appointments List
     */
    public void setDatabaseConnection(Connection customerUI, Integer user, ObservableList<Customer> customer, ObservableList<Appointments> appointments)
    {
        conn = customerUI;
        userID = user;
        customerList = customer;
        appointmentsList = appointments;

        setTableFilterLists();
        appointmentsTableView.setItems(currentMonthList);

    }

    /**
     * Sets the Table filter Lists Month and Week
     * Lambda Expression is used to set the month list with a few easy lines of code instead of writing an additional method to set that list
     */
    private void setTableFilterLists()
    {
        int currentUserMonthValue = LocalDate.now().getMonthValue();

        appointmentsList.forEach( n -> { int appointmentMonth = n.getStart().getMonthValue();
            if (appointmentMonth == currentUserMonthValue) { currentMonthList.add(n); }
        });

        LocalDate weekStart;
        LocalDate weekEnd;

        LocalDateTime timeNow = LocalDateTime.now();
        String dayOfWeek = timeNow.getDayOfWeek().toString();

        switch (dayOfWeek) {
            case "SUNDAY":
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

        for(int i = 0; i < appointmentsList.toArray().length;i++)

        {
            LocalDate dateCheck = appointmentsList.get(i).getStart().toLocalDate();
            if(dateCheck.isAfter(weekStart) && dateCheck.isBefore(weekEnd)){currentWeekList.add(appointmentsList.get(i));}
        }

    }


    /**
     * Sets the Table View and Columns
     * Sets the Toggle group for the Radio Buttons
     * Sets the Language for all the Labels
     */
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

        Locale locale = Locale.getDefault();
        var rb = ResourceBundle.getBundle("translation",locale);

        appointmentLabel.setText(rb.getString("Appointments"));
        viewByLabel.setText(rb.getString("ViewBy"));
        currentMonth.setText(rb.getString("CurrentMonth"));
        currentWeek.setText(rb.getString("CurrentWeek"));
        appointmentIDColumn.setText(rb.getString("AppointmentID"));
        titleColumn.setText(rb.getString("Title"));
        descriptionColumn.setText(rb.getString("Description"));
        locationColumn.setText(rb.getString("Location"));
        contactNameColumn.setText(rb.getString("ContactName"));
        typeColumn.setText(rb.getString("Type"));
        startColumn.setText(rb.getString("Start"));
        endColumn.setText(rb.getString("End"));
        customerIDColumn.setText(rb.getString("CustomerID"));
        customerNameColumn.setText(rb.getString("CustomerName"));
        addButton.setText(rb.getString("Add"));
        updateButton.setText(rb.getString("Update"));
        deleteButton.setText(rb.getString("Delete"));
        backButton.setText(rb.getString("Back"));


    }


}
