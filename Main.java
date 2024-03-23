import java.util.Scanner; 
import java.util.concurrent.BlockingQueue; 
import java.util.concurrent.LinkedBlockingQueue; 

public class Main {
   
   public static void main(String[] args) throws InterruptedException {
       
       displayWelcomeMessage();

       try (Scanner scanner = new Scanner(System.in)) {
           
           String userResponse = scanner.nextLine().trim().toUpperCase();

           // Check user's response
           if ("Y".equals(userResponse)) {
               // Run simulation if user responds with 'Y'
               runSimulation(args);
           } else {
               System.out.println("Simulation aborted. Thank you!");
               System.exit(0);
           }
       }
   }

   // Method to display the welcome message
   private static void displayWelcomeMessage() {
       System.out.println("\033[0;34m" + "\n==========================================");
       System.out.println("\033[0;32m" + "  Welcome to the ThriftStore Simulator!");
       System.out.println("\033[0;34m" + "==========================================\033[0m");
       System.out.println("\nWould you like to start the simulation? (Y/N)\n");
   }

   // Method to run the simulation
   private static void runSimulation(String[] args) throws InterruptedException {
       // Parse command line arguments or use default values
       int numAssistants = args.length > 0 ? Integer.parseInt(args[0]) : 3;
       int numCustomers = args.length > 1 ? Integer.parseInt(args[1]) : 10;
       int numBookSections = args.length > 2 ? Integer.parseInt(args[2]) : 1;
       double purchaseProbability = args.length > 3 ? Double.parseDouble(args[3]) : 0.5;

       // Create a blocking queue for item delivery
       BlockingQueue<Item> deliveryBox = new LinkedBlockingQueue<>();

       // Create sections array with specified number of book sections
       Section[] sections = new Section[5 + numBookSections];
       sections[0] = new Section("Electronics", 10);
       sections[1] = new Section("Clothing", 10);
       sections[2] = new Section("Furniture", 10);
       sections[3] = new Section("Toys", 10);
       sections[4] = new Section("Sporting Goods", 10);
       for (int i = 5; i < 5 + numBookSections; i++) {
           sections[i] = new Section("Books", 10);
       }

       // Create delivery thread and start it
       Delivery delivery = new Delivery(deliveryBox, sections);
       Thread deliveryThread = new Thread(delivery);
       deliveryThread.start();

       // Create assistant threads and start them
       Assistant[] assistants = new Assistant[numAssistants];
       for (int i = 0; i < numAssistants; i++) {
           assistants[i] = new Assistant(i, deliveryBox, sections);
           assistants[i].start();
       }

       // Create customer threads and start them
       Customer[] customers = new Customer[numCustomers];
       for (int i = 0; i < numCustomers; i++) {
           customers[i] = new Customer(i, null, sections, purchaseProbability);
           customers[i].start();
       }

       // Wait for 20 seconds before stopping the simulation
       Thread.sleep(20000);

       // Stop simulation and display results
       stopSimulation(assistants, customers); 
       Statistics.displayResults();
   }

   // Method to stop the simulation
   private static void stopSimulation(Assistant[] assistants, Customer[] customers) throws InterruptedException {
       // Stop assistant threads
       for (Assistant assistant : assistants) {
           assistant.stopSimulation();
       }
       // Join assistant threads
       for (Assistant assistant : assistants) {
           assistant.join();
       }

       // Stop customer threads
       for (Customer customer : customers) {
           customer.stopSimulation();
           customer.join();
       }
       
       // Display simulation results and efficiency analysis
       Statistics.displayResults();
       Statistics.analyzeEfficiency(); 
   }
}
