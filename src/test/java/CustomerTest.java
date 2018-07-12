import models.Customer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomerTest {

    Customer customer;

    @Before
    public void setUp(){
        customer = new Customer("Bob", 100.00);
    }

    @Test
    public void hasName(){
        assertEquals("Bob", customer.getName());
    }

    @Test
    public void changeName(){
        customer.setName("Jen");
        assertEquals("Jen", customer.getName());
    }

    @Test
    public void hasMoney(){
        assertEquals(100.00, customer.getMoney(), 0.02);
    }

    @Test
    public void changeMoney(){
        customer.setMoney(80.00);
        assertEquals(80.00, customer.getMoney(), 0.02);
    }

    @Test
    public void bookingsStartsEmpty(){
        assertEquals(0, customer.getNumberOfBookings());
    }

}
