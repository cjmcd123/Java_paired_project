package controllers;

import db.DBBookings;
import db.DBCustomer;
import db.DBHelper;
import models.Booking;
import models.Customer;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class CustomersController {

    public CustomersController(){
        this.setupEndPoints();
    }

    private void setupEndPoints(){

        VelocityTemplateEngine velocityTemplateEngine = new VelocityTemplateEngine();

        get("/customers", (req, res) -> {
            int currentPage = Integer.parseInt(req.queryParams("page"));
            List<Customer> allCustomers = DBHelper.getAll(Customer.class);
            // returns number of pages needed to display ALL customers, 10 customers/page
            int pagesNeeded = (int)Math.ceil(allCustomers.size()/10.0);
            List<Customer> customersPerPage = DBCustomer.filterCustomers(currentPage, pagesNeeded);

            HashMap<String, Object> model = new HashMap<>();
            model.put("template", "templates/customers/index.vtl");
            model.put("page", currentPage);
            model.put("pagesNeeded", pagesNeeded);
            model.put("customers", customersPerPage);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/customers/frequency", (req, res) -> {
            int currentPage = Integer.parseInt(req.queryParams("page"));
            List<Customer> allCustomers = DBCustomer.orderByFrequency();
            // returns number of pages needed to display ALL customers, 10 customers/page
            int pagesNeeded = (int)Math.ceil(allCustomers.size()/10.0);
            List<Customer> customersPerPage = DBCustomer.filterCustomersFreq(currentPage, pagesNeeded);

            HashMap<String, Object> model = new HashMap<>();
            model.put("template", "templates/customers/indexFrequency.vtl");
            model.put("page", currentPage);
            model.put("pagesNeeded", pagesNeeded);
            model.put("customers", customersPerPage);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/customers/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            model.put("template", "templates/customers/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/customers/:id", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(Customer.class, id);
            List<Booking> bookings = DBBookings.customerBookings(customer);
            double total = DBBookings.totalSpent(customer);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            model.put("template", "templates/customers/view.vtl");
            model.put("dateFormat", dateFormat);
            model.put("timeFormat", timeFormat);
            model.put("bookings", bookings);
            model.put("customer", customer);
            model.put("total", total);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/customers/:id/edit", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(Customer.class, id);
            model.put("template", "templates/customers/edit.vtl");
            model.put("customer", customer);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        // CREATE NEW CUSTOMER
        post("/customers", (req, res) -> {
            String name = req.queryParams("name");
            Customer customer = new Customer(name);
            DBHelper.saveOrUpdate(customer);
            int id = customer.getId();
            String url = "/bookings/new/" + id;
            res.redirect(url);
            return null;
        }, velocityTemplateEngine);

        post("/customers/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(Customer.class, id);
            customer.setName(req.queryParams("name"));
            DBHelper.saveOrUpdate(customer);
            res.redirect("/customers?page=1");
            return null;
        }, velocityTemplateEngine);

        // DELETE CUSTOMER
        post("/customers/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(Customer.class, id);
            DBHelper.delete(customer);
            res.redirect("/customers?page=1");
            return null;
        }, velocityTemplateEngine);

    }
}
