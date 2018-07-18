package models;


import db.DBBookings;
import db.DBCustomer;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="customers")
public class Customer {

    private int id;
    private String name;
    private List<Booking> bookings;
    private int bookingCount;

    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
        this.bookings = new ArrayList<Booking>();
        this.bookingCount = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy="customer", fetch = FetchType.EAGER)
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    @Column(name = "bookingCount")
    public int getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(int bookingCount) {
        this.bookingCount = bookingCount;
    }

    @Transient // getNumberOfBookings will not appear as getter for Hibernate
    public int getNumberOfBookings(){
        return bookings.size();
    }

    public double totalSpent(){
        double total = 0;
        for (Booking booking : bookings){
            total += booking.getTotalCost();
        }
        return total;
    }

    public double totalSpentArray(List<Booking> bookings){
        double total = 0;
        for (Booking booking : bookings){
            total += booking.getTotalCost();
        }
        return total;
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
    }

    public double totalSpentDB(){
        double total = DBCustomer.totalPaid(this);
        return total;
    }

    public int bookingCountDB(){
        int count = DBBookings.customerBookings(this).size();
        return count;
    }
}
