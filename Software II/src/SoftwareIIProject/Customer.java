package SoftwareIIProject;



import java.sql.Date;

public class Customer {

    private int id;
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private Date createDate;
    private String createBy;
    private Date lastUpdated;
    private String lastUpdatedBy;
    private String division;
    private String country;

    public Customer(int id, String name, String address, String postalCode, String phone, Date createDate, String createBy,
                    Date lastUpdated, String lastUpdatedBy, String division, String country)
    {
        this.id = id;
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createBy = createBy;
        this.lastUpdated = lastUpdated;
        this.lastUpdatedBy = lastUpdatedBy;
        this.division = division;
        this.country = country;
    }

    public  int getId(){return id;}

    public void setId(int id){ this.id = id;}

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public String getPostalCode() {return postalCode;}

    public void setPostalCode(String postalCode) {this.postalCode = postalCode;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public Date getCreateDate() {return createDate;}

    public void setCreateDate(Date createDate) {this.createDate = createDate;}

    public String getCreateBy() {return createBy;}

    public void setCreateBy(String createBy) {this.createBy = createBy;}

    public Date getLastUpdated() {return lastUpdated;}

    public void setLastUpdated(Date lastUpdated) {this.lastUpdated = lastUpdated;}

    public String getLastUpdatedBy() {return lastUpdatedBy;}

    public void setLastUpdatedBy(String lastUpdatedBy) {this.lastUpdatedBy = lastUpdatedBy;}

    public String getDivision() {return division;}

    public void setDivision(String division) {this.division = division;}

    public String getCountry() {return country;}

    public void setCountry(String country) {this.country = country;}

}
