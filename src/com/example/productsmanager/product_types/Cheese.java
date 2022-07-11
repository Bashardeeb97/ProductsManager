package com.example.productsmanager.product_types;

import com.example.productsmanager.Product;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Cheese extends Product {


    public Cheese(String name, float price, int qualityScore, LocalDate expiryDate) throws Exception {

        super(name, price, qualityScore, expiryDate);



        if (qualityScore < 30) {

            throw new Exception("Quality score of this product is under 30");
        }


        if (expiryDate.isBefore(LocalDate.now().plusDays(50))) {

            throw new Exception("This product will expire before 50 days have passed.");
        }

        if (expiryDate.isAfter(LocalDate.now().plusDays(100))) {

            throw new Exception("This product will expire after 100 days have passed.");
        }


    }

    public Cheese(String name, float price, int qualityScore, LocalDate expiryDate, LocalDate lastTimeStateUpdated, LocalDate firstDayOnTheShelf) {

        super(name, price, qualityScore, expiryDate, lastTimeStateUpdated, firstDayOnTheShelf);
    }

    @Override
    public boolean isExpired() {

        return getQualityScore() < 30 || getLocalDateNow().isAfter(getExpiryDate());

    }

    @Override
    public void updateProductState() {

        if (super.lastUpdateToday())
            return;


        super.setQualityScore(super.getQualityScore() - (int) DAYS.between(getLastTimeStateUpdated(), getLocalDateNow()));


        setLastTimeStateUpdated(getLocalDateNow());

    }


}
