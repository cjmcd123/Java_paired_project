package controllers;

import db.DBBookings;
import db.DBHelper;
import models.Booking;
import models.Customer;
import models.Table;
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
            model.put("template", "templates/bookings/index.vtl");
            model.put("bookings", bookings);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            model.put("template", "templates/bookings/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(id, Booking.class);
            Customer customer = DBHelper.find(booking.getCustomer().getId(), Customer.class);
            Table table = DBHelper.find(booking.getTable().getId(), Table.class);
            model.put("template", "templates/bookings/show.vtl");
            model.put("booking", booking);
            model.put("customer", customer);
            model.put("table", table);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/bookings/:id/edit", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(id, Customer.class);
            List<Customer> customers = DBHelper.getAll(Customer.class);
            List<Table> tables = DBHelper.getAll(Table.class);
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
            Table table = DBHelper.find(tableId, Table.class);
            String day = req.queryParams("day");
            String month = req.queryParams("month");
            String year = req.queryParams("year");
            String hour = req.queryParams("hour");
            String min = req.queryParams("min");
            String dateString = day + month + year + hour + min;
            int numberOfGuests = Integer.parseInt(req.queryParams("numberOfGuests"));
            try {
                Date date = new SimpleDateFormat("ddMMyyyhhmm").parse(dateString);
                Booking booking = new Booking(customer, table, date, numberOfGuests);
                DBHelper.saveOrUpdate(booking);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.redirect("/bookings");
            return null;
        }, velocityTemplateEngine);

        post("/bookings/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(id, Booking.class);
            int customerId = Integer.parseInt(req.queryParams("customer"));
            booking.setCustomer(DBHelper.find(customerId, Customer.class));
            int tableId = Integer.parseInt(req.queryParams("table"));
            booking.setTable(DBHelper.find(tableId, Table.class));
            int numberOfGuests = Integer.parseInt(req.queryParams("numberOfGuests"));
            String day = req.queryParams("day");
            String month = req.queryParams("month");
            String year = req.queryParams("year");
            String hour = req.queryParams("hour");
            String min = req.queryParams("min");
            String dateString = day + month + year + hour + min;
            booking.setSize(numberOfGuests);
            try {
                Date date = new SimpleDateFormat("ddMMyyyhhmm").parse(dateString);
                booking.setDate(date);
                DBHelper.saveOrUpdate(booking);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            res.redirect("/bookings");
            return null;
        }, velocityTemplateEngine);

        get("/bookings/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Booking booking = DBHelper.find(id, Booking.class);
            DBHelper.delete(booking);
            res.redirect("/bookings");
            return null;
        }, velocityTemplateEngine);


    }
}
