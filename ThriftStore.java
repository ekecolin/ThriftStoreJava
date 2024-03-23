import java.util.concurrent.BlockingQueue; 
import java.util.concurrent.LinkedBlockingQueue;

public class ThriftStore { // Defining a class named ThriftStore and our static fields
    private static int tickCount = 0;
    private static final int NUM_ASSISTANTS = 5;
    private static final int NUM_CUSTOMERS = 10;
    private static final int MAX_TICKS = 100;
    private static Section[] sections;
    private static Assistant[] assistants; 
    private static Customer[] customers;
    private static final double DEFAULT_PURCHASE_PROBABILITY = 0.5; 

    public static void setSections(Section[] sections) { // Method to set sections
        ThriftStore.sections = sections;
    }

    public static void setAssistants(Assistant[] assistants) { // Method to set assistants
        ThriftStore.assistants = assistants;
    }

    public static void setCustomers(Customer[] customers) { // Method to set customers
        ThriftStore.customers = customers;
    }

    public static synchronized void incrementTickCount() { // Method to increment tick count
        tickCount++;
        if (tickCount >= MAX_TICKS) {
            stopSimulation();
        }
    }

    public static synchronized int getTickCount() { // Method to get tick count
        return tickCount; 
    }

    private static void stopSimulation() { // Method to stop simulation
        if (assistants != null) { 
            for (Assistant assistant : assistants) { 
                assistant.stopSimulation(); 
            }
        }
        if (customers != null) { // Checking if customers array is not null
            for (Customer customer : customers) { 
                customer.stopSimulation(); 
            }
        }
        System.out.println("Simulation stopped at tick: " + tickCount); 
        Statistics.displayResults();
        Statistics.analyzeEfficiency();
        System.exit(0); 
    }

    public static void main(String[] args) { // Main method
        BlockingQueue<Item> deliveryBox = new LinkedBlockingQueue<>(); 

        Section[] sections = { // Initializing sections array with Section objects
            new Section("electronics", 10),
            new Section("clothing", 10),
            new Section("furniture", 10), 
            new Section("toys", 10), 
            new Section("sporting_goods", 10), 
            new Section("books", 10) 
        };
        setSections(sections); 

        Assistant[] assistants = new Assistant[NUM_ASSISTANTS]; // Creating an array for assistants
        for (int i = 0; i < NUM_ASSISTANTS; i++) { 
            assistants[i] = new Assistant(i, deliveryBox, sections);
            assistants[i].start(); 
        }
        setAssistants(assistants);

        Customer[] customers = new Customer[NUM_CUSTOMERS]; // Creating an array for customers
        for (int i = 0; i < NUM_CUSTOMERS; i++) { 
            customers[i] = new Customer(i, null, sections, DEFAULT_PURCHASE_PROBABILITY); 
            customers[i].start(); 
        }
        setCustomers(customers);

        Thread deliveryThread = new Thread(new Delivery(deliveryBox, sections)); // Creating a thread for delivery
        deliveryThread.start();

        while (tickCount < MAX_TICKS) { 
            try {
                Thread.sleep(1000); 
            } catch (InterruptedException e) {
                e.printStackTrace(); 
            }
        }
        stopSimulation(); 
    }
}
