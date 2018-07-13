package controllers;

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
            model.put("dateFormat", dateFormat);
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
            String dateFormat = new SimpleDateFormat("dd-MM-yy").format(booking.getDate());
            model.put("dateFormat", dateFormat);
            model.put("template", "templates/bookings/edit.vtl");
            model.put("customers", customers);
            model.put("tables", tables);
            model.put("booking", booking);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        post("/bookings", (req, res) -> {
            int customerId = Integer.parseInt(req.queryParams("customer"));
            Customer customer = DBHelper.find(customerId, Customer.class);
            int tableId = Integer.parseInt(req.queryParams("table"));
            RestaurantTable table = DBHelper.find(tableId, RestaurantTable.class);
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(req.queryParams("date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int numberOfGuests = Integer.parseInt(req.queryParams("numberOfGuests"));
            String startHour = req.queryParams("startHour");
            String endHour = req.queryParams("endHour");
            String startMin = req.queryParams("startMin");
            String endMin = req.queryParams("endMin");
            String startTime = startHour + startMin;
            String endTime = endHour + endMin;
            try {
                Date startTimeDate = new SimpleDateFormat("hhmm").parse(startTime);
                Date endTimeDate = new SimpleDateFormat("hhmm").parse(endTime);
                Booking booking = new Booking(customer, table, date, numberOfGuests, startTimeDate, endTimeDate);
                DBHelper.saveOrUpdate(booking);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.redirect("/bookings");
            return null;
        }, velocityTemplateEngine);


        // EDIT BOOKING
        post("/bookings/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(id, Booking.class);
            int customerId = Integer.parseInt(req.queryParams("customer"));
            booking.setCustomer(DBHelper.find(customerId, Customer.class));
            int tableId = Integer.parseInt(req.queryParams("table"));
            booking.setRestaurantTable(DBHelper.find(tableId, RestaurantTable.class));
            booking.setTotalCost(Integer.parseInt(req.queryParams("numberOfGuests")));
            String startHour = req.queryParams("startHour");
            String endHour = req.queryParams("endHour");
            String startMin = req.queryParams("startMin");
            String endMin = req.queryParams("endMin");
            String startTime = startHour + startMin;
            String endTime = endHour + endMin;
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd").parse(req.queryParams("date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            booking.setDate(date);
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
