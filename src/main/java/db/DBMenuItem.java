package db;

import models.Booking;
import models.MenuItem;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

import java.util.List;

public class DBMenuItem {

        private static Transaction transaction;
        private static Session session;

        public static void addMenuItemToBooking(MenuItem menuItem, Booking booking){
            booking.addMenuItem(menuItem);
            menuItem.addBooking(booking);
            DBHelper.saveOrUpdate(booking);
        }

    public static List<MenuItem> orderMenuItemsByName(){
        List<MenuItem> results = null;
        session = HibernateUtil.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            Criteria cr = session.createCriteria(MenuItem.class);
            cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            cr.addOrder(Order.asc("category"));
            results = cr.list();
        } catch (HibernateException ex) {
            transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }
        return results;
    }

    }
