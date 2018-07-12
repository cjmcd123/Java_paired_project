package controllers;

import db.Seeds;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

public class MainController {

    //    ENTRY POINT
    public static void main(String[] args) {

        Seeds.seedData();

        staticFileLocation("/public");

        RestaurantController restaurantController = new RestaurantController();

        get("/home", (req, res) -> {
            HashMap<String, Object> model = new HashMap<>();
            model.put("template", "views/home.vtl");
            return new ModelAndView(model, "views/layout.vtl");
        }, new VelocityTemplateEngine());


    }
}
