package com.example.productsmanager.product_types;

import com.example.productsmanager.Product;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Wine extends Product {


    public Wine(String name, float price, int qualityScore) throws Exception {

        super(name, price, qualityScore, null);

        if (qualityScore < 0) {

            throw new Exception("Quality score of this product is negative");

        }


    }

    public Wine(String name, float price, int qualityScore, LocalDate lastTimeStateUpdated, LocalDate firstDayOnTheShelf) {

        super(name, price, qualityScore, null, lastTimeStateUpdated, firstDayOnTheShelf);

    }

    @Override
    public boolean isExpired() {

        return false;

    }

    @Override
    public void updateProductState() {

        if (super.lastUpdateToday())
            return;

        super.setQualityScore(super.getQualityScore() +
                ((int)DAYS.between(getFirstDayOnTheShelf(), getLocalDateNow())/10 -
                        (int)DAYS.between(getFirstDayOnTheShelf(), getLastTimeStateUpdated())/10));


        setLastTimeStateUpdated(getLocalDateNow());


    }


    @Override
    public float getPrice() {

        return getBasicPrice();

    }

    @Override
    public int getQualityScore() {


        return Math.min(super.getQualityScore(), 50);

    }


}
