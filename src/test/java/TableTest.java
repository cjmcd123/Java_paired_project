import models.Table;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TableTest {

    Table table;

    @Before
    public void setUp(){
        table = new Table("Table1", 4);
    }

    @Test
    public void hasName(){
        assertEquals("Table1", table.getName());
    }

    @Test
    public void changeName(){
        table.setName("Table2");
        assertEquals("Table2", table.getName());
    }

    @Test
    public void hasSeats(){
        assertEquals(4, table.getNumberOfSeats());
    }

    @Test
    public void changeSeats(){
        table.setNumberOfSeats(3);
        assertEquals(3, table.getNumberOfSeats());
    }

}
