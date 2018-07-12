package models;



import java.util.Date;

public class Booking {

    private int id;
    private Customer customer;
    private Table table;
    private Date date;
    private double receipt;
    private int size;

    public Booking(){

    }

    public Booking(Customer customer, Table table, Date date, int size) {
        this.customer = customer;
        this.table = table;
        this.date = date;
        this.receipt = 00.00;
        this.size = size;
    }



    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Table getTable() {
        return table;
    }

    public Date getDate() {
        return date;
    }

    public double getReceipt() {
        return receipt;
    }

    public int getSize() {
        return size;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setReceipt(double receipt) {
        this.receipt = receipt;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
