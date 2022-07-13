# ProductsManager

*******ProductsManager*******

- ProductsManager is an application for products management in 
  grocery stores.
  
- Every Product has the following four values: name, price, quality and expiray date.
- Each product belongs to a certain type of products. 
- This application uses the processing rules of products types and values of every product
  to help the store workers in two ways :
  
- 1- Determining if new product can be placed on the shleves or not.
- 2- Showing the updated values of products for the next days and 
     determining which products have to be taken off from the shelves.
 

*******General Processing rules*******

- Each product has a fixed, not variable basic price.
- The daily price is determined using this formula : Basic price + 0,1€ * Quality

*******Product types & their processing rules******* 

- This application support two Product types : Wine, Cheese.
- Adding new product types can be done simply by creating new classes in product_types folder  
  which have to inherit the abstract class Product.
  
- Cheese : - requires a minimum quality level of 30 in order to be to be placed on the shelf,<br />
             or is removed as soon as its quality falls below 30.<br />
&nbsp           - has an expiration date that
             between 50 and 100 days in the
             in the future.<br />
           - loses one quality point
             every day.<br />
           - Cheese has a daily Price<br />
           
- Wine     - any non negative quality level is accepted<br />
           - Wine does not lose quality, but gains
             every 10 days one quality point 
             until the quality has reached 50.<br />
           - Wine does not expire<br />
           - Wines do not change their price once they have been
             placed on the shelf       
  
More Infos :

- Added Products can be saved and loaded using SQL Database or CSV file.
- ProductsManagerTest class has tests for all processing rules of all product types.
- This application has been designed to be used through the command line.
