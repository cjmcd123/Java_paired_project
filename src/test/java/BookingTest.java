import models.Booking;
import models.Customer;
import models.Table;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class BookingTest {

    Booking booking;
    Customer customer;
    Table table;

    @Before
    public void setUp() throws ParseException {
        customer = new Customer("Bob", 1000.00);
        table = new Table("Table1", 4);
        Date date = new SimpleDateFormat( "yyyyMMddhhmm" ).parse( "201811202000" );
//        Date time = new SimpleDateFormat("hhmm").parse("2000");
//        date.setTime(time);
        booking = new Booking(customer, table, date, 3);
    }

    @Test
    public void hasCustomer(){
        assertEquals(customer, booking.getCustomer());
    }

    @Test
    public void setCustomer(){
        Customer customer1 = new Customer("Jen", 1000.00);
        booking.setCustomer(customer1);
        assertEquals(customer1, booking.getCustomer());
    }

    @Test
    public void hasTable(){
        assertEquals(table, booking.getTable());
    }

    @Test
    public void setTable(){
        Table table1 = new Table("Table2", 5);
        booking.setTable(table1);
        assertEquals(table1, booking.getTable());
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
    public void receiptStartsEmpty(){
        assertEquals(00.00, booking.getReceipt(), 0.02);
    }

    @Test
    public void changeReceipt(){
        booking.setReceipt(10.00);
        assertEquals(10.00, booking.getReceipt(), 0.02);
    }

    @Test
    public void hasCustomers(){
        assertEquals(3, booking.getSize());
    }

    @Test
    public void setCustomers(){
        booking.setSize(4);
        assertEquals(4, booking.getSize());
    }


}
