package db;

import models.Booking;
import models.Customer;
import models.RestaurantTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Seeds {

    public static void seedData() throws ParseException {
        DBHelper.deleteAll(RestaurantTable.class);
        DBHelper.deleteAll(Customer.class);
        DBHelper.deleteAll(Booking.class);

        RestaurantTable restaurantTable1 = new RestaurantTable("T1", 2);
        RestaurantTable restaurantTable2 = new RestaurantTable("T2", 4);
        RestaurantTable restaurantTable3 = new RestaurantTable("T3", 4);
        DBHelper.saveOrUpdate(restaurantTable1);
        DBHelper.saveOrUpdate(restaurantTable2);
        DBHelper.saveOrUpdate(restaurantTable3);


        Customer customer1 = new Customer("Adri Florence");
        Customer customer2 = new Customer("Angelina Blyth");
        Customer customer3 = new Customer("Craig McDowall");
        Customer customer4 = new Customer("Harjit Singh");
        Customer customer5 = new Customer("Derek Leach");
        DBHelper.saveOrUpdate(customer1);
        DBHelper.saveOrUpdate(customer2);
        DBHelper.saveOrUpdate(customer3);
        DBHelper.saveOrUpdate(customer4);
        DBHelper.saveOrUpdate(customer5);


        Date date1 = new SimpleDateFormat("ddMMyyyyhhmm").parse("201020182015");
        Booking booking1 = new Booking(customer1, restaurantTable1, date1, 4);
        Booking booking2 = new Booking(customer2, restaurantTable2, date1, 2);
        Booking booking3 = new Booking(customer3, restaurantTable3, date1, 4);
        DBHelper.saveOrUpdate(booking1);
        DBHelper.saveOrUpdate(booking2);
        DBHelper.saveOrUpdate(booking3);
    }
}
