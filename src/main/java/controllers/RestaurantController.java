package controllers;

import db.DBHelper;
import models.Booking;
import models.RestaurantTable;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.List;

import static spark.Spark.get;

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
            model.put("Tables", tables);
            return new ModelAndView(model, "templates/layout.vtl");
        }, velocityTemplateEngine);
    }
}
