package controllers;

import db.DBBookings;
import db.DBHelper;
import models.Booking;
import models.Customer;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

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
            HashMap<String, Object> model = new HashMap<>();
            List<Customer> customers = DBHelper.getAll(Customer.class);
            model.put("template", "templates/customers/index.vtl");
            model.put("customers", customers);
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
            Customer customer = DBHelper.find(id, Customer.class);
            List<Booking> bookings = DBBookings.customerBookings(customer);
            model.put("template", "templates/customers/show.vtl");
            model.put("bookings", bookings);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/customers/:id/edit", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(id, Customer.class);
            model.put("template", "templates/customers/edit.vtl");
            model.put("customer", customer);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        post("/customers", (req, res) -> {
            String name = req.queryParams("name");
            Double money = Double.parseDouble(req.queryParams("money"));
            Customer customer = new Customer(name, money);
            DBHelper.saveOrUpdate(customer);
            res.redirect("/customers");
            return null;
        }, velocityTemplateEngine);

        post("/customers/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(id, Customer.class);
            customer.setName(req.queryParams("name"));
            Double money = Double.parseDouble(req.queryParams("money"));
            customer.setMoney(money);
            DBHelper.saveOrUpdate(customer);
            res.redirect("/customers");
            return null;
        }, velocityTemplateEngine);

        get("/customers/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Customer customer = DBHelper.find(id, Customer.class);
            DBHelper.delete(customer);
            res.redirect("/customers");
            return null;
        }, velocityTemplateEngine);


    }
}
