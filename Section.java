import java.util.concurrent.BlockingQueue; 
import java.util.concurrent.ArrayBlockingQueue;

public class Section { // Defining a class named Section and our static fields
    private final String name;
    private final int capacity; 
    private final BlockingQueue<Item> itemQueue; 
    private final BlockingQueue<Customer> customerQueue; 

    public Section(String name, int capacity) { 
        this.name = name; 
        this.capacity = capacity; 
        this.itemQueue = new ArrayBlockingQueue<>(capacity); 
        this.customerQueue = new ArrayBlockingQueue<>(capacity); 
    }

    public synchronized boolean hasAvailableItems() { // Method to check if there are available items in the section
        return !itemQueue.isEmpty(); 
    }

    public synchronized boolean hasAvailableCapacity() { // Method to check if there is available capacity in the section
        return itemQueue.size() < capacity;
    }

    public synchronized boolean hasWaitingCustomers() { // Method to check if there are waiting customers in the section
        return !customerQueue.isEmpty();
    }

    public synchronized void addItem(Item item) throws InterruptedException { // Method to add an item to the section
        itemQueue.put(item); 
    }

    public synchronized void addWaitingCustomer(Customer customer) throws InterruptedException { // Method to add a waiting customer to the section
        customerQueue.put(customer); 
    }

    public synchronized Item takeItem() throws InterruptedException { // Method to take an item from the section
        return itemQueue.take(); 
    }

    public synchronized Customer takeWaitingCustomer() throws InterruptedException { // Method to take a waiting customer from the section
        return customerQueue.take(); 
    }

    public String getName() { // Method to get the name of the section
        return name; 
    }
}

