package db;

import models.Booking;
import models.Customer;
import models.RestaurantTable;
import org.hibernate.*;
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

    public static void pay(int amount, Booking booking){
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

    public static List<Booking> unPaidBookings(Customer customer){
        List<Booking> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Booking.class);
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

    public static boolean bookingCheck(Booking newBooking){
        List<Booking> existingBookings = DBBookings.bookingsByDateAndTable(newBooking.getDate(), newBooking.getRestaurantTable());
        Calendar newStartTime = Calendar.getInstance();
        newStartTime.setTime(newBooking.getStartTime());
        Calendar newEndTime = Calendar.getInstance();
        newEndTime.setTime(newBooking.getEndTime());

        for (Booking booking : existingBookings){
            Calendar currentStartTime = Calendar.getInstance();
            currentStartTime.setTime(booking.getStartTime());
            Calendar currentEndTime = Calendar.getInstance();
            currentEndTime.setTime(booking.getEndTime());
            if (newStartTime.after(currentStartTime.getTime()) && newStartTime.before(currentEndTime.getTime())){
                return true;
            }
            if (newEndTime.after(currentStartTime.getTime()) && newEndTime.before(currentEndTime.getTime())){
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

    public static List<Booking> bookingsByDateAndTable(Date date, RestaurantTable table){
        List<Booking> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Booking.class);
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


}
