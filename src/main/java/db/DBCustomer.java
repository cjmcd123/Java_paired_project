package db;

import models.Booking;
import models.Customer;
import models.MenuItem;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class DBCustomer {

    private static Transaction transaction;
    private static Session session;

    public static List<Customer> filterCustomers(int page, int pagesNeeded) {
        List<Customer> allCustomers = DBCustomer.orderByName();
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

    public static double totalPaid(Customer customer){
        List<Booking> bookings = DBBookings.customerBookings(customer);
       double result = customer.totalSpentArray(bookings);
       return result;
    }

    public static List<Customer> orderByName(){
        List<Customer> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Customer.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.addOrder(Order.asc("name"));
            results = cr.list();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public static List<Customer> orderByFrequency(){
        List<Customer> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        List<Customer> customers = DBHelper.getAll(Customer.class);
        try {
            int count = 0;

            for (Customer customer : customers){
                count = DBBookings.customerBookings(customer).size();
                if (count > 0) {
                    customer.setBookingCount(count);
                    DBHelper.saveOrUpdate(customer);
                }
            }
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(Customer.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.add(Restrictions.gt("bookingCount", 0));
            cr.addOrder(Order.desc("bookingCount"));
            results = cr.list();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    public static List<Customer> filterCustomersFreq(int page, int pagesNeeded) {
        List<Customer> allCustomers = DBCustomer.orderByFrequency();
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
}
