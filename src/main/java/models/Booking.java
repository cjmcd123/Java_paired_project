package models;

import db.DBHelper;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="bookings")
public class Booking {

    private int id;
    private Customer customer;
    private RestaurantTable restaurantTable;
    private Date date;
    private Date startTime;
    private Date endTime;
    private double totalCost;
    private int numberOfGuests;

    public Booking(){

    }

    public Booking(Customer customer, RestaurantTable restaurantTable, Date date, int numberOfGuests, Date startTime, Date endTime) {
        this.customer = customer;
        this.restaurantTable = restaurantTable;
        this.date = date;
        this.totalCost = 00.00;
        this.numberOfGuests = numberOfGuests;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    public Customer getCustomer() {
        return customer;
    }

    @ManyToOne
    @JoinColumn(name="restaurantTable_id", nullable=false)
    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    @Column(name="date")
    public Date getDate() {
        return date;
    }

    @Column(name="totalCost")
    public double getTotalCost() {
        return totalCost;
    }

    @Column(name="numberOfGuests")
    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Column(name = "startTime")
    public Date getStartTime() {
        return startTime;
    }

    @Column(name = "endTime")
    public Date getEndTime() {
        return endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void payBill(double amount) {
        this.setTotalCost(amount);
    }
}
