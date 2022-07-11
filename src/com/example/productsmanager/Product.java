package com.example.productsmanager;

import java.time.LocalDate;

public abstract class Product implements Comparable<Product> {


    private final float basicPrice;
    private String name;
    private int qualityScore;
    private LocalDate expiryDate;






    private LocalDate firstDayOnTheShelf;
    private LocalDate lastTimeStateUpdated;
    private static int numberOfDaysInFuture = 0;

    protected Product(String name, float price, int qualityScore, LocalDate expiryDate) {




        this.name = name;
        this.basicPrice = price;
        this.qualityScore = qualityScore;
        this.expiryDate = expiryDate;


        this.firstDayOnTheShelf = LocalDate.now();
        this.lastTimeStateUpdated = LocalDate.now();
    }

    protected Product(String name, float price, int qualityScore, LocalDate expiryDate,LocalDate lastTimeStateUpdated,LocalDate firstDayOnTheShelf) {

        this(name, price, qualityScore,expiryDate);

        this.firstDayOnTheShelf = firstDayOnTheShelf;
        this.lastTimeStateUpdated = lastTimeStateUpdated;
    }


    // return True if the product must be taken off the shelves
    public abstract boolean isExpired();


    /*  must be called befor calling any other public methods in this
     *  class from other classes to update the state of product
     */
    public abstract void updateProductState();


    public float getPrice() {

        return basicPrice + 0.1f * qualityScore;
    }

    @Override
    public int compareTo(Product product) {
        return this.name.compareToIgnoreCase(product.getName());
    }

    protected boolean lastUpdateToday() {

        return lastTimeStateUpdated.equals(getLocalDateNow());


    }

    public static void setNumberOfDaysInFuture(int numberOfDays) {

        numberOfDaysInFuture = numberOfDays;

    }

    public void setLastTimeStateUpdated(LocalDate lastTimeStateUpdated) {
        this.lastTimeStateUpdated = lastTimeStateUpdated;
    }

    protected void setQualityScore(int qualityScore) {
        this.qualityScore = qualityScore;
    }

    public LocalDate getLastTimeStateUpdated() {
        return lastTimeStateUpdated;
    }

    public float getBasicPrice() {
        return basicPrice;
    }




    public String getName() {
        return name;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public LocalDate getFirstDayOnTheShelf() {
        return firstDayOnTheShelf;
    }



    protected LocalDate getLocalDateNow() {

        return LocalDate.now().plusDays(numberOfDaysInFuture);

    }

    @Override
    public boolean equals(Object obj) {

        return ((Product)obj).getName().equals(name);

    }
}
