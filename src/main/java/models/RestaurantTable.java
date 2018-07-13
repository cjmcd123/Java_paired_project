package models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="restaurantTables")
public class RestaurantTable {

    private int id;
    private String name;
    private int numberOfSeats;
    private List<Booking> bookings;

    public RestaurantTable() {
    }

    public RestaurantTable(String name, int numberOfSeats) {
        this.name = name;
        this.numberOfSeats = numberOfSeats;
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

    @Column(name="numberOfSeats")
    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy="customer", fetch = FetchType.LAZY)
    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
