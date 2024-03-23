import java.util.concurrent.BlockingQueue; 
import java.util.Random; 

public class Delivery implements Runnable { // Defining a class named Delivery which implements the Runnable interface for concurrent execution
    private final BlockingQueue<Item> deliveryBox; 
    private final Section[] sections;

    public Delivery(BlockingQueue<Item> deliveryBox, Section[] sections) {
        this.deliveryBox = deliveryBox;
        this.sections = sections; 
    }

    @Override
    public void run() { // Overriding the run method of the Runnable interface
        while (true) { // Running an infinite loop
            try { 
                Item item = generateRandomItem(); // Generating a random item
                deliveryBox.put(item); // Putting the item into the delivery box
                ThriftStore.incrementTickCount(); // Incrementing the tick count
                System.out.println(ThriftStore.getTickCount() + " " + Thread.currentThread().getName() + " Deposit_of_items : " + item.getName() + "=" + sections.length); 
                Thread.sleep(5000); // Sleeping for 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace(); 
            }
        }
    }

    private Item generateRandomItem() { // Method to generate a random item
        String[] itemNames = {"Electronics", "Clothing", "Furniture", "Toys", "Sporting Goods", "Books"}; // Array of possible item names
        String itemName = itemNames[new Random().nextInt(itemNames.length)]; // Selecting a random item name
        return new Item(itemName);
    }
}
