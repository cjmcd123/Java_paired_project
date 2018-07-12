import models.RestaurantTable;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RestaurantTableTest {

    RestaurantTable restaurantTable;

    @Before
    public void setUp(){
        restaurantTable = new RestaurantTable("Table1", 4);
    }

    @Test
    public void hasName(){
        assertEquals("Table1", restaurantTable.getName());
    }

    @Test
    public void changeName(){
        restaurantTable.setName("Table2");
        assertEquals("Table2", restaurantTable.getName());
    }

    @Test
    public void hasSeats(){
        assertEquals(4, restaurantTable.getNumberOfSeats());
    }

    @Test
    public void changeSeats(){
        restaurantTable.setNumberOfSeats(3);
        assertEquals(3, restaurantTable.getNumberOfSeats());
    }

}
