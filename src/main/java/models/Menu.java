package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="menu")
public class Menu {

    private int id;
    List<MenuItem> menu = new ArrayList<>();

    public Menu() {
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

    @OneToMany(mappedBy="menu", fetch = FetchType.LAZY)
    public void addMenuItem(MenuItem menuItem){
        menu.add(menuItem);
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }
}
