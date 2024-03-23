import java.util.HashMap; 
import java.util.Map; 
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder; 

public class Statistics { // Defining a class named Statistics and our static fields
    private static final Map<Integer, LongAdder> assistantWorkTimes = new HashMap<>(); 
    private static final Map<Integer, LongAdder> customerWaitTimes = new HashMap<>(); 
    private static final AtomicLong totalAssistantWorkTime = new AtomicLong(0); 
    private static final AtomicLong totalCustomerWaitTime = new AtomicLong(0); 
    private static final LongAdder assistantWorkEvents = new LongAdder(); 
    private static final LongAdder customerWaitEvents = new LongAdder(); 

    public static synchronized void logAssistantWork(int assistantId, long workTime) { // Method to log assistant work
        assistantWorkTimes.computeIfAbsent(assistantId, k -> new LongAdder()).add(workTime); 
        totalAssistantWorkTime.addAndGet(workTime); 
        assistantWorkEvents.increment(); 
        System.out.println("Assistant " + assistantId + " worked for " + workTime + " milliseconds."); 
    }

    public static synchronized void logCustomerWait(int customerId, long waitTime) { // Method to log customer wait
        customerWaitTimes.computeIfAbsent(customerId, k -> new LongAdder()).add(waitTime); 
        totalCustomerWaitTime.addAndGet(waitTime); 
        customerWaitEvents.increment(); 
        System.out.println("Customer " + customerId + " waited for " + waitTime + " milliseconds."); 
    }

    public static void analyzeEfficiency() { // Method to analyze efficiency
        double assistantEfficiency = totalAssistantWorkTime.get() / (double) assistantWorkEvents.intValue();
        double customerSatisfactionMetric = totalCustomerWaitTime.get() / (double) customerWaitEvents.intValue(); 
        System.out.println("Assistant Efficiency: " + assistantEfficiency + " [Work Time per Event]"); 
        System.out.println("Customer Satisfaction Metric: " + customerSatisfactionMetric + " [Wait Time per Event]");
    }

    public static void displayResults() { // Method to display results
        long averageAssistantWorkTime = assistantWorkEvents.longValue() == 0 ? 0 : totalAssistantWorkTime.get() / assistantWorkEvents.longValue(); 
        System.out.println("Average assistant work time: " + averageAssistantWorkTime + " milliseconds."); 
        long averageCustomerWaitTime = customerWaitEvents.longValue() == 0 ? 0 : totalCustomerWaitTime.get() / customerWaitEvents.longValue(); 
        System.out.println("Average customer wait time: " + averageCustomerWaitTime + " milliseconds."); 
    }
}
