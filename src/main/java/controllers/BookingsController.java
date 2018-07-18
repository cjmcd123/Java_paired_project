package controllers;

import db.DBBookings;
import db.DBCustomer;
import db.DBHelper;
import db.DBMenuItem;
import models.Booking;
import models.Customer;
import models.MenuItem;
import models.RestaurantTable;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static spark.Spark.get;
import static spark.Spark.post;

public class BookingsController {

    public BookingsController(){
        this.setupEndPoints();
    }

    private void setupEndPoints(){

        VelocityTemplateEngine velocityTemplateEngine = new VelocityTemplateEngine();

        get("/bookings", (req, res) -> {
            int currentPage = Integer.parseInt(req.queryParams("page"));
            List<Booking> allBookings = DBHelper.getAll(Booking.class);
            // returns number of pages needed to display ALL customers, 10 customers/page
            int pagesNeeded = (int)Math.ceil(allBookings.size()/10.0);
            List<Booking> bookingsPerPage = DBBookings.filterBookings(currentPage, pagesNeeded);

            HashMap<String, Object> model = new HashMap<>();
//            List<Booking> bookings = DBHelper.getAll(Booking.class);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String date = dtf.format(now);
            model.put("dateFormat", dateFormat);
            model.put("timeFormat", timeFormat);
            model.put("template", "templates/bookings/index.vtl");
            model.put("bookings", bookingsPerPage);
            model.put("date", date);
            model.put("page", currentPage);
            model.put("pagesNeeded", pagesNeeded);
//            model.put("bookings", bookings);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        // RETURNS TABLE VIEW FOR INDIVIDUAL DAY
        get ("/bookings/tableview", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(req.queryParams("date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<Booking> bookings = DBBookings.bookingsForGivenDate(date);
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            List<String> timeSlots = Arrays.asList("11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00");

            model.put("dateFormat", dateFormat);
            model.put("timeFormat", timeFormat);
            model.put("template", "templates/bookings/tableview.vtl");
            model.put("bookings", bookings);
            model.put("tables", tables);
            model.put("timeSlots", timeSlots);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);


        get("/bookings/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Customer> customers = DBCustomer.orderByName();
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String dateString = dtf.format(now);
            model.put("date", dateString);
            model.put("template", "templates/bookings/create.vtl");
            model.put("customers", customers);
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/seatError", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Customer> customers = DBHelper.getAll(Customer.class);
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String dateString = dtf.format(now);
            model.put("date", dateString);
            model.put("template", "templates/bookings/createSeats.vtl");
            model.put("customers", customers);
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/doubleBooked", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Customer> customers = DBHelper.getAll(Customer.class);
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String dateString = dtf.format(now);
            model.put("date", dateString);
            model.put("template", "templates/bookings/createDouble.vtl");
            model.put("customers", customers);
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/timeError", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Customer> customers = DBHelper.getAll(Customer.class);
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String dateString = dtf.format(now);
            model.put("date", dateString);
            model.put("template", "templates/bookings/createTime.vtl");
            model.put("customers", customers);
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/seatError/:id", (req, res) -> {
            int customerId = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(Customer.class, customerId);
            HashMap<String, Object> model = new HashMap<>();
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String dateString = dtf.format(now);
            model.put("date", dateString);
            model.put("template", "templates/bookings/newSeats.vtl");
            model.put("customer", customer);
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/doubleBooked/:id", (req, res) -> {
            int customerId = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(Customer.class, customerId);
            HashMap<String, Object> model = new HashMap<>();
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String dateString = dtf.format(now);
            model.put("date", dateString);
            model.put("template", "templates/bookings/newDouble.vtl");
            model.put("customer", customer);
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/timeError/:id", (req, res) -> {
            int customerId = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(Customer.class, customerId);
            HashMap<String, Object> model = new HashMap<>();
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            model.put("template", "templates/bookings/newTime.vtl");
            model.put("customer", customer);
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/new/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(Customer.class, id);
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String dateString = dtf.format(now);
            model.put("date", dateString);
            model.put("template", "templates/bookings/new.vtl");
            model.put("customer", customer);
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/view", (req, res) -> {
            int currentPage = Integer.parseInt(req.queryParams("page"));
            List<Booking> allBookings = DBBookings.unPaidBookings();
            // returns number of pages needed to display ALL customers, 10 customers/page
            int pagesNeeded = (int)Math.ceil(allBookings.size()/10.0);
            HashMap<String, Object> model = new HashMap<>();
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(req.queryParams("dateSearch"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<Booking> bookingsPerPage = DBBookings.filterBookingsDate(currentPage, pagesNeeded, date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String dateString = dtf.format(now);
            model.put("dateFormat", dateFormat);
            model.put("timeFormat", timeFormat);
            model.put("template", "templates/bookings/index.vtl");
            model.put("bookings", bookingsPerPage);
            model.put("date", dateString);
            model.put("page", currentPage);
            model.put("pagesNeeded", pagesNeeded);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/payView", (req, res) -> {
            int currentPage = Integer.parseInt(req.queryParams("page"));
            List<Booking> allBookings = DBBookings.unPaidBookings();
            // returns number of pages needed to display ALL customers, 10 customers/page
            int pagesNeeded = (int)Math.ceil(allBookings.size()/10.0);
            HashMap<String, Object> model = new HashMap<>();
            List<Booking> bookings = DBBookings.filterBookingsUnpaid(currentPage, pagesNeeded);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String dateString = dtf.format(now);
            model.put("date", dateString);
            model.put("dateFormat", dateFormat);
            model.put("timeFormat", timeFormat);
            model.put("template", "templates/bookings/indexPayView.vtl");
            model.put("bookings", bookings);
            model.put("page", currentPage);
            model.put("pagesNeeded", pagesNeeded);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        post("/bookings/:id/pay", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(Booking.class,id);
            double amount = Double.parseDouble(req.queryParams("sum"));
            DBBookings.pay(amount, booking);
            res.redirect("/bookings?page=1");
            return null;
        }, velocityTemplateEngine);

        get("/bookings/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(Booking.class, id);
            Customer customer = DBHelper.find(Customer.class, booking.getCustomer().getId());
            List<MenuItem> allMenuItem = DBMenuItem.orderMenuItemsByName(); // alphabetical
            List<MenuItem> meal = DBBookings.menuItemsForBooking(booking);
            RestaurantTable table = DBHelper.find(RestaurantTable.class, booking.getRestaurantTable().getId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            DecimalFormat df2 = new DecimalFormat(".##");

            model.put("template", "templates/bookings/details.vtl");
            model.put("dateFormat", dateFormat);
            model.put("timeFormat", timeFormat);
            model.put("booking", booking);
            model.put("customer", customer);
            model.put("allMenuItem", allMenuItem);
            model.put("meal", meal);
            model.put("table", table);
            model.put("df2", df2);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/:id/edit", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(Booking.class, id);
            List<Customer> customers = DBHelper.getAll(Customer.class);
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            String dateFormat = new SimpleDateFormat("yyyy-mm-dd").format(booking.getDate());
            String startFormatString = new SimpleDateFormat("HHmm").format(booking.getStartTime());
            Integer startFormat = Integer.parseInt(startFormatString);
            String endFormatString = new  SimpleDateFormat("HHmm").format(booking.getEndTime());
            Integer endFormat = Integer.parseInt(endFormatString);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();
            model.put("cal", cal);
            model.put("sdf", sdf);

            model.put("dateFormat", dateFormat);
            model.put("template", "templates/bookings/edit.vtl");
            model.put("customers", customers);
            model.put("tables", tables);
            model.put("booking", booking);
            model.put("startFormat", startFormat);
            model.put("endFormat", endFormat);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);


        // SAVE NEW BOOKING
        post("/bookings", (req, res) -> {
            int customerId = Integer.parseInt(req.queryParams("customer"));
            Customer customer = DBHelper.find(Customer.class, customerId);
            int tableId = Integer.parseInt(req.queryParams("table"));
            RestaurantTable table = DBHelper.find(RestaurantTable.class, tableId);
            int numberOfGuests = Integer.parseInt(req.queryParams("numberOfGuests"));
            Date date = null;
            Booking booking = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(req.queryParams("date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String startTime = req.queryParams("startTime");
            String endTime = req.queryParams("endTime");
            Date startTimeDate = null;
            Date endTimeDate = null;
            try {
                startTimeDate = new SimpleDateFormat("HHmm").parse(startTime);
                endTimeDate = new SimpleDateFormat("HHmm").parse(endTime);
                booking = new Booking(customer, table, date, numberOfGuests, startTimeDate, endTimeDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (booking.getNumberOfGuests() > table.getNumberOfSeats()){
                res.redirect("/bookings/seatError");
                return null;
            }
            boolean bookingCheck = DBBookings.bookingCheck(booking);
            if (bookingCheck){
                res.redirect("/bookings/doubleBooked");
                return null;
            }
//            non working double booking check
            int startTimeInt = Integer.parseInt(startTime);
            int endTimeInt = Integer.parseInt(endTime);
            if (startTimeInt < endTimeInt){
                DBHelper.saveOrUpdate(booking);
                res.redirect("/bookings?page=1");
                return null;
            } if (endTimeInt == 0000 && startTimeInt < 2330){
                DBHelper.saveOrUpdate(booking);
                res.redirect("/bookings?page=1");
                return null;
            } else {
                res.redirect("/bookings/timeError");
                return null;
            }
        }, velocityTemplateEngine);

//        create new booking from new customer
        post("/bookings/new/:id", (req, res) -> {
            int customerId = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(Customer.class, customerId);
            int tableId = Integer.parseInt(req.queryParams("table"));
            RestaurantTable table = DBHelper.find(RestaurantTable.class, tableId);
            int numberOfGuests = Integer.parseInt(req.queryParams("numberOfGuests"));
            Date date = null;
            Booking booking = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(req.queryParams("date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String startTime = req.queryParams("startTime");
            String endTime = req.queryParams("endTime");
            Date startTimeDate = null;
            Date endTimeDate = null;
            try {
                startTimeDate = new SimpleDateFormat("HHmm").parse(startTime);
                endTimeDate = new SimpleDateFormat("HHmm").parse(endTime);
                booking = new Booking(customer, table, date, numberOfGuests, startTimeDate, endTimeDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (booking.getNumberOfGuests() > table.getNumberOfSeats()){
                res.redirect("/bookings/seatError/" + customerId);
                return null;
            }
            boolean bookingCheck = DBBookings.bookingCheck(booking);
            if (bookingCheck){
                res.redirect("/bookings/doubleBooked/" + customerId);
                return null;
            }
//            non working double booking check
            int startTimeInt = Integer.parseInt(startTime);
            int endTimeInt = Integer.parseInt(endTime);
            if (startTimeInt < endTimeInt){
                DBHelper.saveOrUpdate(booking);
                res.redirect("/bookings?page=1");
                return null;
            } if (endTimeInt == 0000 && startTimeInt < 2330){
                DBHelper.saveOrUpdate(booking);
                res.redirect("/bookings?page=1");
                return null;
            } else {
                res.redirect("/bookings/timeError/" + customerId);
                return null;
            }
        }, velocityTemplateEngine);


        // EDIT BOOKING
        post("/bookings/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(Booking.class, id);

            int customerId = Integer.parseInt(req.queryParams("id"));
            booking.setCustomer(DBHelper.find(Customer.class, customerId));
            int tableId = Integer.parseInt(req.queryParams("table"));
            booking.setRestaurantTable(DBHelper.find(RestaurantTable.class, tableId));
            booking.setNumberOfGuests(Integer.parseInt(req.queryParams("numberOfGuests")));

            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(req.queryParams("date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            booking.setDate(date);
            String startTime = req.queryParams("startTime");
            String endTime = req.queryParams("endTime");
            try {
                Date startTimeDate = new SimpleDateFormat("HHmm").parse(startTime);
                Date endTimeDate = new SimpleDateFormat("HHmm").parse(endTime);
                booking.setStartTime(startTimeDate);
                booking.setEndTime(endTimeDate);
                DBHelper.saveOrUpdate(booking);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.redirect("/bookings?page=1");
            return null;
        }, velocityTemplateEngine);

        post("/bookings/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(Booking.class, id);
            DBHelper.delete(booking);
            res.redirect("/bookings?page=1");
            return null;
        }, velocityTemplateEngine);


    }
}
