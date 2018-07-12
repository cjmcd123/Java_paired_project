package db;

import models.Customer;
import models.RestaurantTable;

public class Seeds {

    public static void seedData(){
//      DBHelper.deleteAll(RestaurantTable.class);
//      DBHelper.deleteAll(Customer.class);

        RestaurantTable restaurantTable1 = new RestaurantTable("T1", 2);
        RestaurantTable restaurantTable2 = new RestaurantTable("T2", 4);
        RestaurantTable restaurantTable3 = new RestaurantTable("T3", 4);
        DBHelper.save(restaurantTable1);
        DBHelper.save(restaurantTable2);
        DBHelper.save(restaurantTable3);

        Customer customer1 = new Customer("Adri Florence", 1000);
        Customer customer2 = new Customer("Angelina Blyth", 1000);
        Customer customer3 = new Customer("Craig McDowall", 3000);
        Customer customer4 = new Customer("Harjit Singh", 500);
        Customer customer5 = new Customer("Derek Leach", 1000);
        DBHelper.save(customer1);
        DBHelper.save(customer2);
        DBHelper.save(customer3);
        DBHelper.save(customer4);
        DBHelper.save(customer5);
    }
}
