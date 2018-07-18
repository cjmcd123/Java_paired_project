package models;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="menuItems")
public class MenuItem {

    private int id;
    private String name;
    private double price;
    private String category;
    private List<Booking> bookings;

    public MenuItem() {
    }

    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.bookings = new ArrayList<Booking>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
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

    @Column(name="price")
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Column(name="category")
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @ManyToMany
    @JoinTable(name = "booking_meal",
            joinColumns = {@JoinColumn(name = "meal_id", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "booking_id", nullable = false, updatable = false)})
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public void addBooking(Booking booking){
        this.bookings.add(booking);
    }

}
