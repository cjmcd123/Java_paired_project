package db;

import models.Booking;
import models.Customer;

import java.util.List;

public class DBCustomer {

    public static int totalPaid(Customer customer){
        List<Booking> bookings = DBBookings.customerBookings(customer);
       int result = customer.totalSpentArray(bookings);
       return result;
    }

}
