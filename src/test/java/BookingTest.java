import models.Booking;
import models.Customer;
import models.RestaurantTable;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BookingTest {

    Booking booking;
    Customer customer;
    RestaurantTable restaurantTable;

    @Before
    public void setUp() throws ParseException {
        customer = new Customer("Bob");
        restaurantTable = new RestaurantTable("Table1", 4);
        Date date = new SimpleDateFormat( "yyyyMMddhhmm" ).parse( "201811202000" );
        Date startTime = new SimpleDateFormat("hhmm").parse("2000");
        Date endTime = new SimpleDateFormat("hhmm").parse("2100");
        booking = new Booking(customer, restaurantTable, date, 3, startTime, endTime);
    }

    @Test
    public void hasCustomer(){
        assertEquals(customer, booking.getCustomer());
    }

    @Test
    public void setCustomer(){
        Customer customer1 = new Customer("Jen");
        booking.setCustomer(customer1);
        assertEquals(customer1, booking.getCustomer());
    }

    @Test
    public void hasTable(){
        assertEquals(restaurantTable, booking.getRestaurantTable());
    }

    @Test
    public void setTable(){
        RestaurantTable restaurantTable1 = new RestaurantTable("Table2", 5);
        booking.setRestaurantTable(restaurantTable1);
        assertEquals(restaurantTable1, booking.getRestaurantTable());
    }

    @Test
    public void hasDate(){
        assertEquals(10, booking.getDate().getMonth());
    }

    @Test
    public void setDate() throws ParseException {
        Date date1 = new SimpleDateFormat("ddMMyyyy").parse("20102018");
        booking.setDate(date1);
        assertEquals(9, booking.getDate().getMonth());
    }

    @Test
    public void hasTime(){
        assertEquals(20, booking.getDate().getHours());
    }

    @Test
    public void changeTime() throws ParseException {
        Date date1 = new SimpleDateFormat("ddMMyyyyhhmm").parse("201020182015");
        booking.setDate(date1);
        assertEquals(15, booking.getDate().getMinutes());
    }

    @Test
    public void totalCostStartsZero(){
        assertEquals(00.00, booking.getTotalCost(), 0.02);
    }

    @Test
    public void changeReceipt(){
        booking.setTotalCost(10.00);
        assertEquals(10.00, booking.getTotalCost(), 0.02);
    }

    @Test
    public void hasCustomers(){
        assertEquals(3, booking.getNumberOfGuests());
    }

    @Test
    public void setCustomers(){
        booking.setNumberOfGuests(4);
        assertEquals(4, booking.getNumberOfGuests());
    }

    @Test
    public void payBill(){
        booking.payBill(20.00);
        assertEquals(20.00, booking.getTotalCost(), 0.02);
    }

    @Test
    public void dateOutputTest() throws ParseException {
        Date date1 = new SimpleDateFormat("ddMMyyyy").parse("20102018");
        String  date = new SimpleDateFormat("ddMMyy").format(date1);
        assertEquals("201018", date);
    }

}
