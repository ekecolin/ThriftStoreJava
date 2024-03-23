import java.util.concurrent.BlockingQueue; 
import java.util.Random;

public class Assistant extends Thread { // Defining a class named Assistant and our fields
    private final int assistantId; 
    private final BlockingQueue<Item> deliveryBox; 
    private final Section[] sections; 
    private volatile boolean running = true; 
    private int tickCountForNextBreak; 
    private final Random rand = new Random(); 
    private final int breakDuration = 150; 

    public Assistant(int assistantId, BlockingQueue<Item> deliveryBox, Section[] sections) { 
        this.assistantId = assistantId; 
        this.deliveryBox = deliveryBox;
        this.sections = sections; 
        scheduleNextBreak(); // Scheduling the next break
    }

    private void scheduleNextBreak() { // Method to schedule the next break
        tickCountForNextBreak = ThriftStore.getTickCount() + 200 + rand.nextInt(101);
    }

    public void stopSimulation() { // Method to stop the simulation
        running = false; 
    }

    @Override
    public void run() { // Overriding the run method of Thread class
        while (running) { // Running the loop while the thread is supposed to be running
            try { 
                if (ThriftStore.getTickCount() >= tickCountForNextBreak) { // Checking if it's time for the next break
                    System.out.println("Assistant " + assistantId + " is taking a break."); 
                    Thread.sleep(breakDuration * 10); // Sleeping for the break duration
                    scheduleNextBreak(); // Scheduling the next break
                }

                long startTime = System.currentTimeMillis(); // Recording the start time

                Item item = deliveryBox.take(); // Taking an item from the delivery box
                stockItems(item); // Stocking the taken item

                for (Section section : sections) { // Looping through sections
                    if (section.hasWaitingCustomers() && section.hasAvailableCapacity()) { // Checking if a section has waiting customers and available capacity
                        serveCustomer(section); // Serving the customer in that section
                        break; 
                    }
                }

                long endTime = System.currentTimeMillis(); // Recording the end time
                Statistics.logAssistantWork(this.assistantId, endTime - startTime); // Logging the assistant's work duration

                ThriftStore.incrementTickCount(); // Incrementing the tick count

                Thread.sleep(1000); // Sleeping for 1 second
            } catch (InterruptedException e) { 
                Thread.currentThread().interrupt(); // Interrupting the current thread
                System.out.println("Assistant " + assistantId + " was interrupted.");
            }
        }
    }

    private void stockItems(Item items) throws InterruptedException { // Method to stock items
        for (Section section : sections) { // Looping through sections
            if (section.hasAvailableCapacity() && section.hasWaitingCustomers()) { // Checking if a section has available capacity and waiting customers
                section.addItem(items); // Adding items to the section
                return; 
            }
        }

        for (Section section : sections) { // Looping through sections again
            if (section.hasAvailableCapacity()) { // Checking if a section has available capacity
                section.addItem(items); // Adding items to the section
                return; 
            }
        }

        sections[0].addItem(items); // Adding items to the first section if none of the sections have available capacity
    }

    private void serveCustomer(Section section) throws InterruptedException { // Method to serve a customer
        section.takeItem(); // Taking an item from the section
    }
}
