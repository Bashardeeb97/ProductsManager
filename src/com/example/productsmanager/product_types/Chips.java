package com.example.productsmanager.product_types;

import com.example.productsmanager.Product;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Chips extends Product {

    boolean halfPriceDiscount=false;

    public Chips(String name, float price, int qualityScore, LocalDate expiryDate) throws Exception {

        super(name, price, qualityScore, expiryDate);
        if (qualityScore < 0) {

            throw new Exception("Quality score of this product is negative");

        }



    }

    public Chips(String name, float price, int qualityScore, LocalDate expiryDate, LocalDate lastTimeStateUpdated, LocalDate firstDayOnTheShelf) {

        super(name, price, qualityScore, expiryDate, lastTimeStateUpdated, firstDayOnTheShelf);
    }

    @Override
    public boolean isExpired() {
        return  getLocalDateNow().isAfter(getExpiryDate()) || getQualityScore() < 0;
    }

    @Override
    public void updateProductState() {

        if (super.lastUpdateToday())
            return;

        if(getExpiryDate().isBefore(getLocalDateNow().plusDays(30))){

            halfPriceDiscount = true;

        }


        super.setQualityScore(super.getQualityScore() -
                ((int)DAYS.between(getFirstDayOnTheShelf(), getLocalDateNow())/7 -
                (int)DAYS.between(getFirstDayOnTheShelf(), getLastTimeStateUpdated())/7));






        setLastTimeStateUpdated(getLocalDateNow());


    }

    @Override
    public float getPrice() {

        return halfPriceDiscount ? super.getBasicPrice()/2f : super.getPrice();

    }
}
