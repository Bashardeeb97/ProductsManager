package com.example.productsmanager.test;

import com.example.productsmanager.Product;
import com.example.productsmanager.ProductsManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ProductsManagerTest {


    @org.junit.BeforeClass
    public static void beforeClass() {

        ProductsManager.getInstance();

    }

    @org.junit.Before
    public void befor() {

        ProductsManager.getInstance().clearProductsSet();

    }

    @org.junit.Test
    public void addPrdouct() {


        String[] commands = {"add cheese0 Cheese 45.3 30 " + LocalDate.now().plusDays(50),   // valid
                "add wine0 Wine 45.3 1",                                                               // valid
                "add chips0 Chips 45.3 5 " + LocalDate.now().plusDays(50),                   // valid
                "add cheese1 Cheese 5 29 " + LocalDate.now().plusDays(55),                   // not valid
                "add cheese2 Cheese 5 33 " + LocalDate.now().plusDays(49),                   // not valid
                "add cheese3 Cheese 5 33 " + LocalDate.now().plusDays(101),                  // not valid
                "add wine1 Wine 5 -1",
                "add chips1 Chips 45.3 -1 " + LocalDate.now().plusDays(50)};                                                                // not valid


        for (String command : commands) {
            try {
                ProductsManager.getInstance().checkCommandAndExcuteIt(command);
            } catch (Exception e) {
                //ingnore all exceptions to run all the assertions
            }
        }


        assertTrue(ProductsManager.getInstance().checkProductName("cheese0"));
        assertTrue(ProductsManager.getInstance().checkProductName("wine0"));
        assertTrue(ProductsManager.getInstance().checkProductName("chips0"));

        assertFalse(ProductsManager.getInstance().checkProductName("cheese1"));
        assertFalse(ProductsManager.getInstance().checkProductName("cheese2"));
        assertFalse(ProductsManager.getInstance().checkProductName("cheese3"));
        assertFalse(ProductsManager.getInstance().checkProductName("wine1"));
        assertFalse(ProductsManager.getInstance().checkProductName("chips1"));
    }


    @org.junit.Test
    public void showProducts() throws Exception {

        String[] commands = {"add cheese0 Cheese 8 30 " + LocalDate.now().plusDays(52),
                "add cheese1 Cheese 8 100 " + LocalDate.now().plusDays(60),
                "add chips0 Chips 32 11 " + LocalDate.now().plusDays(70),
                "add wine Wine 8 0 " + LocalDate.now().plusDays(60)

        };


        for (String command : commands) {

            ProductsManager.getInstance().checkCommandAndExcuteIt(command);

        }


        List<Product> productsList = new ArrayList<>(ProductsManager.getInstance().getProductsSet());

        Product.setNumberOfDaysInFuture(1);
        productsList.get(0).updateProductState();
        assertTrue(productsList.get(0).isExpired());
        assertEquals(productsList.get(0).getPrice(), 10.9f, 0.001f);


        Product.setNumberOfDaysInFuture(61);
        productsList.get(1).updateProductState();
        assertTrue(productsList.get(1).isExpired());
        assertEquals(productsList.get(1).getPrice(), 11.9f, 0.001f);


        Product.setNumberOfDaysInFuture(69);
        productsList.get(2).updateProductState();
        assertFalse(productsList.get(2).isExpired());
        assertEquals(productsList.get(2).getPrice(), 16f, 0.001f);
        assertEquals(productsList.get(2).getQualityScore(), 2);

        Product.setNumberOfDaysInFuture(60);
        productsList.get(3).updateProductState();
        assertEquals(productsList.get(3).getQualityScore(), 6);
        assertEquals(productsList.get(3).getPrice(), 8f, 0.001f);


    }


    @org.junit.Test
    public void writeAndLoad_SQL() throws Exception {

        String[] commands = {"add cheese0 Cheese 45.3 30 " + LocalDate.now().plusDays(50),   // valid
                "add wine0 Wine 45.3 1",
                "add chips0 Chips 45.3 5 " + LocalDate.now().plusDays(50)};

        for (String command : commands) {

            ProductsManager.getInstance().checkCommandAndExcuteIt(command);

        }

        ProductsManager.getInstance().writeToSQLDB();
        ProductsManager.getInstance().clearProductsSet();
        ProductsManager.getInstance().readFromSQLDB();


        assertTrue(ProductsManager.getInstance().checkProductName("cheese0"));
        assertTrue(ProductsManager.getInstance().checkProductName("wine0"));
        assertTrue(ProductsManager.getInstance().checkProductName("chips0"));

    }

    @org.junit.Test
    public void writeAndLoad_CSV() throws Exception {

        String[] commands = {"add cheese0 Cheese 45.3 30 " + LocalDate.now().plusDays(50),   // valid
                "add wine0 Wine 45.3 1",
                "add chips0 Chips 45.3 5 " + LocalDate.now().plusDays(50)};

        for (String command : commands) {

            ProductsManager.getInstance().checkCommandAndExcuteIt(command);

        }

        ProductsManager.getInstance().writeToCSV();
        ProductsManager.getInstance().clearProductsSet();
        ProductsManager.getInstance().readFromCSV();


        assertTrue(ProductsManager.getInstance().checkProductName("cheese0"));
        assertTrue(ProductsManager.getInstance().checkProductName("wine0"));
        assertTrue(ProductsManager.getInstance().checkProductName("chips0"));

    }


}