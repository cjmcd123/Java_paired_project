package db;

import models.Booking;
import models.Customer;

import java.util.List;

public class DBCustomer {

    public static List<Customer> filterCustomers(int page, int pagesNeeded) {
        List<Customer> allCustomers = DBHelper.getAll(Customer.class);
        int fromIndex = (10*page)-10; // inclusive  0 10 20 30
        if(page == pagesNeeded) {
            int toIndex = (allCustomers.size());
            List<Customer> customersPerPage = allCustomers.subList(fromIndex, toIndex);
            return customersPerPage;
        } else {
            int toIndex = (10*page); // exclusive      10 20 30 40
            List<Customer> customersPerPage = allCustomers.subList(fromIndex, toIndex);
            return customersPerPage;
        }
    }

    public static int totalPaid(Customer customer){
        List<Booking> bookings = DBBookings.customerBookings(customer);
       int result = customer.totalSpentArray(bookings);
       return result;
    }

}
