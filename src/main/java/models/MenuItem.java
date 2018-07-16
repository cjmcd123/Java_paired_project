package models;

import javax.persistence.*;

@Entity
@Table(name="menuItems")
public class MenuItem {

    private int id;
    private String name;
    private double price;
    private String description;
    private Menu menu;

    public MenuItem(String name, double price, String description, Menu menu) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.menu = menu;
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

    @Column(name="description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    @JoinColumn(name="menu_id", nullable=false)
    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
