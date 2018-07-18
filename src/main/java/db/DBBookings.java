package db;

import models.Booking;
import models.Customer;
import models.MenuItem;
import models.RestaurantTable;
import org.hibernate.*;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DBBookings {

    private static Transaction transaction;
    private static Session session;

    public static List<Booking> customerBookings(Customer customer) {
        List<Booking> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Booking.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.add(Restrictions.eq("customer", customer));
            results = cr.list();
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public static List<Booking> bookingsForGivenDate(Date date) {
        List<Booking> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Booking.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.add(Restrictions.eq("date", date));
            results = cr.list();
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public static void pay(double amount, Booking booking){
        Customer customer = booking.getCustomer();
        double loyaltyDiscount = DBBookings.loyaltyDiscount(customer);
        double moneyDiscount = DBBookings.spentDiscount(customer);
        double discount = 1.0 - loyaltyDiscount - moneyDiscount;
        double billAmount = amount * discount;
        booking.payBill(billAmount);
        DBHelper.saveOrUpdate(booking);
    }

    public static int numOfBooking(Customer customer){
        int result = DBBookings.customerBookings(customer).size();
        return result;
    }

    public static double loyaltyDiscount(Customer customer){
        double discount = 0;
        int count = DBBookings.numOfBooking(customer);
        if (count > 5){
            discount = 0.1;
        }
        return discount;
    }

    public static double spentDiscount(Customer customer){
        double discount = 0;
        List<Booking> bookings = DBBookings.customerBookings(customer);
        double total = DBBookings.totalSpent(customer);
        if (total > 500.00){
            discount = 0.1;
        }
        return discount;
    }

    public static double totalSpent(Customer customer) {
        double total = 0.00;
        List<Booking> bookings = DBBookings.customerBookings(customer);
        for (Booking booking : bookings){
            total += booking.getTotalCost();
        }
        return total;
    }

    public static List<Booking> unPaidCustomerBookings(Customer customer){
        List<Booking> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Booking.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.add(Restrictions.eq("customer", customer));
            cr.add(Restrictions.eq("totalCost", 00.00));
            results = cr.list();
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public static List<Booking> unPaidBookings(){
        List<Booking> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Booking.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.add(Restrictions.eq("totalCost", 00.00));
            results = cr.list();
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public static boolean bookingCheck(Booking newBooking){
        List<Booking> existingBookings = DBBookings.bookingsByDateAndTable(newBooking.getDate(), newBooking.getRestaurantTable());
        for (Booking booking : existingBookings){
            Date bookingStartTime = booking.getStartTime();
            Date bookingEndTime = booking.getEndTime();
            Date newStartTime = newBooking.getStartTime();
            Date newEndTime = newBooking.getEndTime();
            if ((newStartTime.after(bookingStartTime) && newStartTime.before(bookingEndTime))
                    || (newEndTime.after(bookingStartTime) && newEndTime.before(bookingEndTime))
                    || (newStartTime.equals(bookingStartTime))) {
                return true;
            }
        }
            return false;
    }

    public static List<Booking> bookingsByDate(Date date){
        List<Booking> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Booking.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.add(Restrictions.eq("date", date));
            results = cr.list();
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public static List<Booking> bookingsByDateAndTable(Date date, RestaurantTable table) {
        List<Booking> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Booking.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.add(Restrictions.eq("date", date));
            cr.add(Restrictions.eq("restaurantTable", table));
            results = cr.list();
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public static List<MenuItem> menuItemsForBooking(Booking booking) {
        List<MenuItem> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(MenuItem.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.createAlias("bookings", "booking");
            cr.add(Restrictions.eq("booking.id", booking.getId()));
            results = cr.list();
            transaction.commit();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public static List<Booking> filterBookings(int page, int pagesNeeded) {
        List<Booking> allBookings = DBHelper.getAll(Booking.class);
        int fromIndex = (10*page)-10; // inclusive  0 10 20 30
        if(page == pagesNeeded) {
            int toIndex = (allBookings.size());
            List<Booking> bookingsPerPage = allBookings.subList(fromIndex, toIndex);
            return bookingsPerPage;
        } else {
            int toIndex = (10*page); // exclusive      10 20 30 40
            List<Booking> bookingsPerPage = allBookings.subList(fromIndex, toIndex);
            return bookingsPerPage;
        }
    }

    public static List<Booking> filterBookingsUnpaid(int page, int pagesNeeded) {
        List<Booking> allBookings = DBBookings.unPaidBookings();
        int fromIndex = (10*page)-10; // inclusive  0 10 20 30
        if(page == pagesNeeded) {
            int toIndex = (allBookings.size());
            List<Booking> bookingsPerPage = allBookings.subList(fromIndex, toIndex);
            return bookingsPerPage;
        } else {
            int toIndex = (10*page); // exclusive      10 20 30 40
            List<Booking> bookingsPerPage = allBookings.subList(fromIndex, toIndex);
            return bookingsPerPage;
        }
    }

    public static List<Booking> filterBookingsDate(int page, int pagesNeeded, Date date) {
        List<Booking> allBookings = DBBookings.bookingsByDate(date);
        int fromIndex = (10*page)-10; // inclusive  0 10 20 30
        if(page == pagesNeeded) {
            int toIndex = (allBookings.size());
            List<Booking> bookingsPerPage = allBookings.subList(fromIndex, toIndex);
            return bookingsPerPage;
        } else {
            int toIndex = (10*page); // exclusive      10 20 30 40
            List<Booking> bookingsPerPage = allBookings.subList(fromIndex, toIndex);
            return bookingsPerPage;
        }
    }
}
