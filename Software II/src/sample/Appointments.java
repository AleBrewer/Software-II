package sample;

import java.sql.Date;

public class Appointments {

    private int applicationID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Date start;
    private Date end;
    private Date createDate;
    private String createBy;
    private Date lastUpdate;
    private String lastUpdateBy;
    private int customerID;
    private int userID;
    private int contactID;
    private String contactName;
    private String customerName;

    public Appointments(int applicationID, String title, String description, String location, String type, Date start, Date end, Date createDate,
                        String createBy, Date lastUpdate, String lastUpdateBy, int customerID, int userID, int contactID, String contactName, String customerName )
    {
        this.applicationID = applicationID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.createDate = createDate;
        this.createBy = createBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
        this.contactName = contactName;
        this.customerName = customerName;
    }

    public int getApplicationID() { return applicationID; }

    public void setApplicationID(int applicationID) { this.applicationID = applicationID; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactName() {
        return contactName;
    }

    public String getCustomerName() {
        return customerName;
    }
}
