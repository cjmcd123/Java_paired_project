import models.Restaurant;
import models.Table;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class RestaurantTest {

    Restaurant restaurant;
    Table table;

    @Before
    public void setUp(){
        restaurant = new Restaurant("Codeclan");
        table = new Table("Table1", 4);
    }

    @Test
    public void hasName(){
        assertEquals("Codeclan", restaurant.getName());
    }

    @Test
    public void changeName(){
        restaurant.setName("Codeclan Food");
        assertEquals("Codeclan Food", restaurant.getName());
    }

    @Test
    public void tablesStartsEmpty(){
        assertEquals(0, restaurant.getTables().size());
    }

    @Test
    public void addTable(){
        restaurant.addTable(table);
        assertEquals(1, restaurant.getTables().size());
    }

}
