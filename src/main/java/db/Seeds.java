package db;

import models.Customer;
import models.Restaurant;
import models.Table;

public class Seeds {

    public static void seedData(){
//      DBHelper.deleteAll(Table.class);
//      DBHelper.deleteAll(Customer.class);

        Restaurant restaurant = new Restaurant("The CodeClan Canteen");
        Table table1 = new Table("T1", 2);
        Table table2 = new Table("T2", 4);
        Table table3 = new Table("T3", 4);
        restaurant.addTable(table1);
        restaurant.addTable(table2);
        restaurant.addTable(table3);
        DBHelper.save(restaurant);

        Customer customer1 = new Customer("Adri Florence", 1000);
        Customer customer2 = new Customer("Angelina Blyth", 1000);
        Customer customer3 = new Customer("Craig McDowall", 3000);
        Customer customer4 = new Customer("Harjit Singh", 500);
        Customer customer5 = new Customer("Adri Florence", 1000);
        DBHelper.save(customer1);
        DBHelper.save(customer2);
        DBHelper.save(customer3);
        DBHelper.save(customer4);
        DBHelper.save(customer5);
    }
}
