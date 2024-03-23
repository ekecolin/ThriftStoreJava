# To compile 
javac Main.java ThriftStore.java Assistant.java Customer.java Delivery.java Item.java Section.java Statistics.java

# To run
java Main 5 15 2 0.7

# Configuration parameters
- 5 assistants = Simulation starts with 5 assistants to handle item stocking and customer service
- 15 customers = 15 customer threads actively browsing, waiting for items or purchasing items
- 2 additional book sections = Besides predefined sections - Electronics, Clothing, Furniture, Toys and Sporting Goods, I added 2 more book sections totalling 7 sections
- 0.7 purchase probability = Each customer has a 70% chance of deciding to purchase an item when they find an available item in a section
