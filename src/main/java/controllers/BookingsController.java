package controllers;

import db.DBBookings;
import db.DBHelper;
import models.Booking;
import models.Customer;
import models.RestaurantTable;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class BookingsController {

    public BookingsController(){
        this.setupEndPoints();
    }

    private void setupEndPoints(){

        VelocityTemplateEngine velocityTemplateEngine = new VelocityTemplateEngine();

        get("/bookings", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Booking> bookings = DBHelper.getAll(Booking.class);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
            model.put("dateFormat", dateFormat);
            model.put("timeFormat", timeFormat);
            model.put("template", "templates/bookings/index.vtl");
            model.put("bookings", bookings);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<Customer> customers = DBHelper.getAll(Customer.class);
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            model.put("template", "templates/bookings/create.vtl");
            model.put("customers", customers);
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/new/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(id, Customer.class);
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            model.put("template", "templates/bookings/new.vtl");
            model.put("customer", customer);
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(id, Booking.class);
            Customer customer = DBHelper.find(booking.getCustomer().getId(), Customer.class);
            RestaurantTable table = DBHelper.find(booking.getRestaurantTable().getId(), RestaurantTable.class);
            model.put("template", "templates/bookings/show.vtl");
            model.put("booking", booking);
            model.put("customer", customer);
            model.put("table", table);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/:id/edit", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(id, Booking.class);
            List<Customer> customers = DBHelper.getAll(Customer.class);
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            String dateFormat = new SimpleDateFormat("yyyy-mm-dd").format(booking.getDate());
            String startFormatString = new SimpleDateFormat("hhmm").format(booking.getStartTime());
            Integer startFormat = Integer.parseInt(startFormatString);
            String endFormatString = new  SimpleDateFormat("hhmm").format(booking.getEndTime());
            Integer endFormat = Integer.parseInt(endFormatString);
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
            Customer customer = DBHelper.find(customerId, Customer.class);
            int tableId = Integer.parseInt(req.queryParams("table"));
            RestaurantTable table = DBHelper.find(tableId, RestaurantTable.class);
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
                startTimeDate = new SimpleDateFormat("hhmm").parse(startTime);
                endTimeDate = new SimpleDateFormat("hhmm").parse(endTime);
                booking = new Booking(customer, table, date, numberOfGuests, startTimeDate, endTimeDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Booking bookingCheck = DBBookings.boookingCheck(date, startTimeDate, endTimeDate, table);
            if (bookingCheck != null){
                res.redirect("/bookings/new");
                return null;
            }
            int startTimeInt = Integer.parseInt(startTime);
            int endTimeInt = Integer.parseInt(endTime);
            if (startTimeInt < endTimeInt){
                DBHelper.saveOrUpdate(booking);
                res.redirect("/bookings");
                return null;
            } if (endTimeInt == 0000 && startTimeInt < 2330){
                DBHelper.saveOrUpdate(booking);
                res.redirect("/bookings");
                return null;
            } else {
                res.redirect("bookings/new");
                return null;
            }
        }, velocityTemplateEngine);


        // EDIT BOOKING
        post("/bookings/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(id, Booking.class);

            int customerId = Integer.parseInt(req.queryParams("id"));
            booking.setCustomer(DBHelper.find(customerId, Customer.class));
            int tableId = Integer.parseInt(req.queryParams("table"));
            booking.setRestaurantTable(DBHelper.find(tableId, RestaurantTable.class));
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
                Date startTimeDate = new SimpleDateFormat("hhmm").parse(startTime);
                Date endTimeDate = new SimpleDateFormat("hhmm").parse(endTime);
                booking.setStartTime(startTimeDate);
                booking.setEndTime(endTimeDate);
                DBHelper.saveOrUpdate(booking);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.redirect("/bookings");
            return null;
        }, velocityTemplateEngine);

        post("/bookings/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(id, Booking.class);
            DBHelper.delete(booking);
            res.redirect("/bookings");
            return null;
        }, velocityTemplateEngine);



    }
}
