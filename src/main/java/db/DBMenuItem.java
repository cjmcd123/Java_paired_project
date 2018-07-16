package db;

import models.Booking;
import models.MenuItem;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DBMenuItem {

        private static Transaction transaction;
        private static Session session;

        public static void addMenuItemToBooking(MenuItem menuItem, Booking booking){
            booking.addMenuItem(menuItem);
            menuItem.addBooking(booking);
            DBHelper.saveOrUpdate(booking);
        }

    }
