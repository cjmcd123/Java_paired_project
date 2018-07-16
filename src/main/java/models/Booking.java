package models;

import db.DBHelper;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private List<MenuItem> meal;

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
        this.meal = new ArrayList<>();
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

    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ManyToMany
    @JoinTable(name = "booking_meal",
            joinColumns = {@JoinColumn(name = "booking_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "meal_id", nullable = false, updatable = false)})
    public List<MenuItem> getMeal() {
        return meal;
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

    public void setMeal(List<MenuItem> meal) {
        this.meal = meal;
    }

    public void addMenuItem(MenuItem menuItem){
        this.meal.add(menuItem);
    }

    public List<String> bookingSlots() {
        List<String> timeSlots = new ArrayList<>();
        String startTimeColon = new SimpleDateFormat("HH:mm").format(this.getStartTime());
        String endTimeColon = new SimpleDateFormat("HH:mm").format(this.getEndTime());

        timeSlots.add(startTimeColon); // adds start slots

        // adds following slots
        String nextTimeSlotColon = startTimeColon;
        while(!endTimeColon.equals(nextTimeSlotColon)) {
            String splitNextTimeSlotList[] = nextTimeSlotColon.split(":");
            if (splitNextTimeSlotList[1].equals("00")) {
                splitNextTimeSlotList[1] = "30";
                nextTimeSlotColon = splitNextTimeSlotList[0] + ":" + splitNextTimeSlotList[1];
                timeSlots.add(nextTimeSlotColon);
            } else if (splitNextTimeSlotList[1].equals("30")) {
                splitNextTimeSlotList[1] = "00";
                String newHour = String.valueOf(Integer.parseInt(splitNextTimeSlotList[0]) + 1);
                nextTimeSlotColon = newHour + ":" + splitNextTimeSlotList[1];
                timeSlots.add(nextTimeSlotColon);
            }
        }
        return timeSlots;
    }
}
