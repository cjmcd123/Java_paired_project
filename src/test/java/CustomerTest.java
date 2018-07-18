import db.DBHelper;
import models.Booking;
import models.Customer;
import models.RestaurantTable;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class CustomerTest {

    Customer customer;

    @Before
    public void setUp(){
        customer = new Customer("Bob");
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
    public void bookingsStartsEmpty(){
        assertEquals(0, customer.getNumberOfBookings());
    }

    @Test
    public void totalMoneySpent() throws ParseException {
        RestaurantTable restaurantTable = new RestaurantTable("Table1", 4);
        Date date = new SimpleDateFormat( "yyyyMMddhhmm" ).parse( "201811202000" );
        Date startTime = new SimpleDateFormat("hhmm").parse("2010");
        Date endTime = new SimpleDateFormat("hhmm").parse("2100");
        Booking booking = new Booking(customer, restaurantTable, date, 3, startTime, endTime);
        booking.payBill(20.00);
        customer.addBooking(booking);
        assertEquals(20.00, customer.totalSpent(), 0.02);
    }

}
