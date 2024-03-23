import java.util.concurrent.BlockingQueue; 
import java.util.Random;

public class Customer extends Thread { // Defining a class named Customer and our fields
    private final int customerId; 
    private final BlockingQueue<Customer> customerQueue; 
    private final Section[] sections; 
    private final double purchaseProbability; 
    private volatile boolean running = true; 

    public Customer(int customerId, BlockingQueue<Customer> customerQueue, Section[] sections, double purchaseProbability) { // Constructor for Customer class
        this.customerId = customerId; 
        this.customerQueue = customerQueue;
        this.sections = sections;
        this.purchaseProbability = purchaseProbability;
    }

    public void stopSimulation() { // Method to stop the simulation
        running = false;
    }

    @Override
    public void run() { // Overriding the run method of Thread class
        Random rand = new Random(); // Creating a Random object for generating random numbers
        while (running) { // Running the loop while the thread is supposed to be running
            try { 
                Section section = sections[new Random().nextInt(sections.length)]; // Selecting a random section

                if (rand.nextDouble() < purchaseProbability && section.hasAvailableItems()) { // Checking if the customer purchases and the section has available items
                    Item item = section.takeItem(); // Taking an item from the section
                    ThriftStore.incrementTickCount(); // Incrementing the tick count
                    System.out.println(ThriftStore.getTickCount() + " " + Thread.currentThread().getName() + " Customer=" + customerId + " purchased item: " + item.getName()); 
                    Thread.sleep(1000); // Sleeping for 1 second
                } else { // If the customer doesn't purchase or the section doesn't have available items
                    section.addWaitingCustomer(this); // Adding the customer to the waiting list of the section
                    ThriftStore.incrementTickCount(); // Incrementing the tick count
                    System.out.println(ThriftStore.getTickCount() + " " + Thread.currentThread().getName() + " Customer=" + customerId + " waiting for items in section: " + section.getName()); 
                    Thread.sleep(2000); // Sleeping for 2 seconds
                }
            } catch (InterruptedException e) { 
                Thread.currentThread().interrupt(); // Interrupting the current thread
                System.out.println("Customer " + customerId + " was interrupted.");
            }
        }
    }
}
