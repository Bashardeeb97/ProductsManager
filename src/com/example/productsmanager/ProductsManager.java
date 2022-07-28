package com.example.productsmanager;

import com.example.productsmanager.product_types.*;


import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ProductsManager {


    public static final String RESET = "\033[0m";
    public static final String RED = "\033[0;31m";     // RED
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String BLUE = "\033[0;34m";    // BLUE
    public static final String PURPLE = "\033[0;35m";  // PURPLE


    /*  TreeSet instance is used to store the Product Instances to prevent adding
     *  instances that share the same name and to show the stored products informations
     *  ordered by their names.
     */
    private static Set<Product> productsSet = new TreeSet<>();

    // Singleton instance
    private static ProductsManager productsManager;


    // The constructor is private declared to initialize singleton instance of this Class using getInstance()
    private ProductsManager() {
    }

    public void run() {

        System.out.format("\n%49s", "*".repeat(25));
        System.out.format("\n%49s", "---- Products Manger ----");
        System.out.format("\n%49s\n\n", "*".repeat(25));


        System.out.println("- Add new product using add command : " + BLUE + "add" + GREEN + " Name Product_Type Basic_Price Quality_Score Expiry_Date " + RESET + "\n  " + PURPLE + "Note:" + GREEN + " Expiry_Date" + RESET + " must have this date format year-month-day\n  " + PURPLE + "Note:" + GREEN + " Product_Type" + RESET + " can be Cheese, Wine or Chips \n  " + PURPLE + "Note:" + GREEN + " Basic_Price" + RESET + " is a positive floating point number \n  " + PURPLE + "Note:" + GREEN + " Quality_Score" + RESET + " is a natural number");
        System.out.println("- Show products informations for the following N days using show command :" + BLUE + " show" + GREEN + " N" + RESET + " \n  " + PURPLE + "Note:" + GREEN + " N" + RESET + " is the number of days");
        System.out.println("- Save products Infromations in csv file : " + BLUE + "save" + GREEN + " CSV" + RESET);
        System.out.println("- Save products Infromations in SQL Database : " + BLUE + "save" + GREEN + " SQL" + RESET);
        System.out.println("- Load products Infromations from csv file : " + BLUE + "load" + GREEN + " CSV" + RESET);
        System.out.println("- Load products Infromations from SQL Database : " + BLUE + "load" + GREEN + " SQL" + RESET);
        Scanner scanner = new Scanner(System.in);

        String input = "";

        while (true) {

            System.out.print("-> ");
            input = scanner.nextLine();

            try {

                checkCommandAndExcuteIt(input);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }

    // Initialize singleton instance of this class if it is not already initialized
    public static ProductsManager getInstance() {

        if (productsManager == null)
            productsManager = new ProductsManager();

        return productsManager;

    }

    public Set<Product> getProductsSet() {

        return new TreeSet(productsSet);

    }

    /*
     * Checks whether the specified command is already defined and checks its parameters if they
     * are valid, and then execute it.
     * @param command The given command that user type it in the terminal.
     */
    public void checkCommandAndExcuteIt(String command) throws Exception {


        String[] commandParts = command.trim().split("\\s+");

        switch (commandParts[0].toLowerCase()) {

            case "add":
                addPrdouct(commandParts);
                break;

            case "show":
                showProducts(Integer.parseInt(commandParts[1]));
                break;

            case "save":

                if (commandParts[1].equalsIgnoreCase("CSV")) {

                    writeToCSV();

                } else if (commandParts[1].equalsIgnoreCase("SQL")) {

                    writeToSQLDB();

                } else {

                    System.out.println("Prodcuts can only be saved in CSV file or in SQL Database");

                }

                break;

            case "load":
                if (commandParts[1].equalsIgnoreCase("CSV")) {

                    readFromCSV();

                } else if (commandParts[1].equalsIgnoreCase("SQL")) {

                    readFromSQLDB();
                } else {

                    System.out.println("Prodcuts can only be loaded from CSV file or from SQL Database");
                }
                break;

            default:
                System.out.println("Command have not been found");


        }
    }


    /*
     * Add the product information given by the user to the productsSet
     * if the initialization of the product raises an exception, This
     * means that the product information does not comply with its product type rules.
     * @param commandParts : The command parameters.
     */

    private void addPrdouct(String[] commandParts) throws Exception {


        Product newProduct = null;
        String message = "";
        boolean knownType = false;

        try {

            switch (commandParts[2].toLowerCase()) {


                case "cheese":
                    knownType = true;
                    newProduct = new Cheese(commandParts[1], Float.parseFloat(commandParts[3]), Integer.parseInt(commandParts[4]), LocalDate.parse(commandParts[5]));
                    break;

                case "wine":
                    knownType = true;
                    newProduct = new Wine(commandParts[1], Float.parseFloat(commandParts[3]), Integer.parseInt(commandParts[4]));
                    break;

                case "chips":
                    knownType = true;
                    newProduct = new Chips(commandParts[1], Float.parseFloat(commandParts[3]), Integer.parseInt(commandParts[4]), LocalDate.parse(commandParts[5]));
                    break;

                // add more types ...


            }

        } catch (NumberFormatException exception) {

            System.out.println("NumberFormatException occurred");
            return;

        } catch (DateTimeParseException exception) {

            System.out.println("DateTimeParseException occurred");
            return;

        } catch (ArrayIndexOutOfBoundsException exception) {

            System.out.println("Please fill all the product informations");
            return;
        }

        if (!knownType) {

            message = "The given product type is not supported";
        }

        if (newProduct != null) {

            message = productsSet.add(newProduct) ? "The item have been added successfully" : "There is already a product with the same name";

        }

        System.out.println(message);

    }


    /*
     * print the states of all products in productsSet
     * for the next days.
     * @param numberOfDays : number of the next days.
     */
    private void showProducts(int numberOfDays) {

        if (productsSet.isEmpty()) {

            System.out.println("There are no products to be shown");
            return;

        } else if (numberOfDays < 0) {

            System.out.println("Number of days must be positive");
        }

        String leftAlignFormat = "| %-15s | %-7s | %-7s | %-11s |%n";
        for (int day = 0; day <= numberOfDays; ++day) {

            Product.setNumberOfDaysInFuture(day);
            System.out.println("Day number " + day + " (" + LocalDate.now().plusDays(day) + ")");

            System.out.format("+-----------------+---------+---------+-------------+%n");
            System.out.format("| Product name    | Price   | Quality | State       |%n");
            System.out.format("+-----------------+---------+---------+-------------+%n");


            productsSet.stream().
                    forEach(product -> {
                        product.updateProductState();
                        System.out.format(leftAlignFormat,
                                product.getName(),
                                Math.round(product.getPrice() * 100f) / 100.0f,
                                product.getQualityScore(),
                                product.isExpired() ? RED + "expired    " + RESET : GREEN + "not expired" + RESET);
                    });

            System.out.format("+-----------------+---------+---------+-------------+%n\n");
        }
    }

    /*
     * Save product informations in saved_products/products.csv
     */
    public void writeToCSV() {

        if (productsSet.isEmpty()) {
            System.out.println("There are no products informations to be saved");
            return;
        }

        File csvFile = new File("src/com/example/productsmanager/saved_products/products.csv");

        try {

            PrintWriter pr = new PrintWriter(csvFile);
            Product.setNumberOfDaysInFuture(0);
            productsSet.parallelStream().
                    forEach(product -> {
                        product.updateProductState();
                        pr.printf("%s,%s,%s,%s,%s,%s,%s\n",
                                product.getName(),
                                product.getBasicPrice(),
                                product.getQualityScore(),
                                product.getExpiryDate(),
                                product.getLastTimeStateUpdated(),
                                product.getFirstDayOnTheShelf(),
                                product.getClass().getSimpleName());
                    });


            pr.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Products informations have been saved");
    }

    /*
     * read product informations from saved_products/products.csv
     */
    public void readFromCSV() {

        File f = new File("src/com/example/productsmanager/saved_products/products.csv");

        if (!f.exists()) {

            System.out.println("There are no saved products Infromations to be loaded");
            return;
        }

        productsSet.clear();
        BufferedReader reader = null;
        String line = "";
        try {

            reader = new BufferedReader(new FileReader("src/com/example/productsmanager/saved_products/products.csv"));

            while ((line = reader.readLine()) != null) {

                String[] row = line.split(",");
                productsSet.add(restoreSavedProduct(row));

            }

            System.out.println("Products Informations have been loaded successfully.");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Initialize new products by calling the second constructor
     * of the Product class  which take two parameters more, this
     * constructor is used when product informations are loaded
     * from CSV file or from SQL database.
     * @param args : informations of product.
     */
    private Product restoreSavedProduct(String[] args) {


        switch (args[args.length - 1].toLowerCase()) {

            case "cheese":
                return new Cheese(args[0], Float.parseFloat(args[1]), Integer.parseInt(args[2]), LocalDate.parse(args[3]), LocalDate.parse(args[4]), LocalDate.parse(args[5]));

            case "wine":
                return new Wine(args[0], Float.parseFloat(args[1]), Integer.parseInt(args[2]), LocalDate.parse(args[4]), LocalDate.parse(args[5]));

            case "chips":
                return new Chips(args[0], Float.parseFloat(args[1]), Integer.parseInt(args[2]), LocalDate.parse(args[3]), LocalDate.parse(args[4]), LocalDate.parse(args[5]));
        }

        return null;
    }

    /*
     * save products informations in saved_products/products.db
     */
    public void writeToSQLDB() {

        if (productsSet.isEmpty()) {
            System.out.println("There are no products informations to be saved");
            return;
        }


        try {

            Connection connection = DriverManager.getConnection("jdbc:sqlite:src/com/example/productsmanager/saved_products/products.db");
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS productsINFOS ");
            statement.execute("CREATE TABLE IF NOT EXISTS productsINFOS " +
                    "(name VARCHAR(250) NOT NULL PRIMARY KEY," +
                    "basicPrice float(9,2) NOT NULL," +
                    "qualityScore INT(255) ," +
                    "expiryDate VARCHAR(250) ," +
                    "lastTimeStateUpdated VARCHAR(250) NOT NULL," +
                    "firstDay_OnTheShelf VARCHAR(250) NOT NULL," +
                    "productType VARCHAR(250) NOT NULL)");

            Product.setNumberOfDaysInFuture(0);

            productsSet.stream().
                    forEach(product -> {
                        product.updateProductState();
                        String values = String.format("VALUES('%s','%f','%o','%s','%s','%s','%s')",
                                product.getName(),
                                product.getBasicPrice(),
                                product.getQualityScore(),
                                product.getExpiryDate(),
                                product.getLastTimeStateUpdated(),
                                product.getFirstDayOnTheShelf(),
                                product.getClass().getSimpleName());
                        try {

                            statement.execute("INSERT INTO productsINFOS " +
                                    values);

                        } catch (SQLException throwables) {

                            // The product already exists in the productsINFOS table.
                        }

                    });


            statement.close();
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        System.out.println("Products informations have been saved");
    }

    /*
     * read products informations from saved_products/products.db
     */
    public void readFromSQLDB() {


        File f = new File("src/com/example/productsmanager/saved_products/products.db");
        if (!f.exists()) {
            System.out.println("There are no saved products Infromations to be loaded");
            return;
        }


        productsSet.clear();

        try {

            Connection connection = DriverManager.getConnection("jdbc:sqlite:src/com/example/productsmanager/saved_products/products.db");
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM productsINFOS");
            ResultSet resultSet = statement.getResultSet();

            int columnCount = resultSet.getMetaData().getColumnCount();

            while (resultSet.next()) {

                String[] row = new String[columnCount];
                for (int index = 1; index <= columnCount; ++index) {
                    row[index - 1] = resultSet.getString(index);
                }
                productsSet.add(restoreSavedProduct(row));

            }

            System.out.println("Products Informations have been loaded successfully.");

            resultSet.close();
            statement.close();
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /*
     * Check if productsSet has a product with the same given name
     * @param name :  name of the product.
     */
    public boolean checkProductName(String name) {

        return productsSet.parallelStream().anyMatch(product -> product.getName().equals(name));


    }
    // Clear all products in the productsSet
    public void clearProductsSet() {

        productsSet.clear();
    }


}