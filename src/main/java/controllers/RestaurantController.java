package controllers;

import db.DBHelper;
import models.Booking;
import models.Customer;
import models.RestaurantTable;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;
import static spark.Spark.post;

public class RestaurantController {

    public RestaurantController(){
        this.setupEndPoints();
    }

    private void setupEndPoints() {

        VelocityTemplateEngine velocityTemplateEngine = new VelocityTemplateEngine();

        get("/tables", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            List<RestaurantTable> tables = DBHelper.getAll(RestaurantTable.class);
            model.put("template", "templates/tables/index.vtl");
            model.put("tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/tables/new", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            model.put("template", "templates/tables/create.vtl");
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        get("/tables/:id/edit", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            int id = Integer.parseInt(req.params(":id"));
            RestaurantTable restaurantTable = DBHelper.find(RestaurantTable.class, id);
            model.put("template", "templates/tables/edit.vtl");
            model.put("table", restaurantTable);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);

        // CREATE NEW Table
        post("/tables", (req, res) -> {
            String name = req.queryParams("name");
            int numberOfSeats = Integer.parseInt(req.queryParams("seats"));
            RestaurantTable restaurantTable = new RestaurantTable(name, numberOfSeats);
            DBHelper.saveOrUpdate(restaurantTable);
            res.redirect("/tables");
            return null;
        }, velocityTemplateEngine);

        post("/tables/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            RestaurantTable restaurantTable = DBHelper.find(RestaurantTable.class, id);
            restaurantTable.setName(req.queryParams("name"));
            int numberOfSeats = Integer.parseInt(req.queryParams("seats"));
            restaurantTable.setNumberOfSeats(numberOfSeats);
            DBHelper.saveOrUpdate(restaurantTable);
            res.redirect("/tables");
            return null;
        }, velocityTemplateEngine);

        post("/tables/:id/delete", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            RestaurantTable restaurantTable = DBHelper.find(RestaurantTable.class, id);
            DBHelper.delete(restaurantTable);
            res.redirect("/tables");
            return null;
        }, velocityTemplateEngine);

    }
}
