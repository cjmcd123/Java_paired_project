package db;

import models.Booking;
import models.Customer;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DBCustomer {

    private static Transaction transaction;
    private static Session session;

    public static int totalPaid(Customer customer){
        List<Booking> bookings = DBBookings.customerBookings(customer);
       int result = customer.totalSpentArray(bookings);
       return result;
    }

}
