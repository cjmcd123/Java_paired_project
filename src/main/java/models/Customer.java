package models;

import java.util.List;

public class Customer {

    private int id;
    private String name;
    private double money;
    private List<Booking> bookings;

    public Customer() {
    }

    public Customer(String name, double money) {
        this.name = name;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public int getNumberOfBookings(){
        return bookings.size();
    }

}
