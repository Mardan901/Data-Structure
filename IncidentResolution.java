
import java.util.*;

public class IncidentResolution {

    public static void processResolutions(Queue<AnalystInfo> internalQueue, 
                                          Queue<AnalystInfo> externalQueue, 
                                          Queue<AnalystInfo> criticalQueue) {
        
        Stack<AnalystInfo> resolvedStack = new Stack<>();

        // Loop continues until ALL queues are completely empty
        while (!internalQueue.isEmpty() || !externalQueue.isEmpty() || !criticalQueue.isEmpty()) {
            // Serve batches of 5 in the required order
            processBatch(internalQueue, resolvedStack, 5);
            processBatch(externalQueue, resolvedStack, 5);
            processBatch(criticalQueue, resolvedStack, 5);
        }

        // Once all queues are empty and everyone is in the stack, display them
        displayResolvedStack(resolvedStack);
    }

    // Helper method to process a specific number of analysts from a queue
    private static void processBatch(Queue<AnalystInfo> queue, Stack<AnalystInfo> stack, int batchSize) {
        for (int i = 0; i < batchSize; i++) {
            if (!queue.isEmpty()) {
                // Dequeue from the front of the queue (FIFO)
                AnalystInfo analyst = queue.poll(); 
                // Push onto the top of the stack (LIFO)
                stack.push(analyst); 
            }
        }
    }

    // Method to pop and display the stack contents
    private static void displayResolvedStack(Stack<AnalystInfo> resolvedStack) {
        System.out.println("\n===== Resolved Incidents (Stack - LIFO) =====");
        
        // Pop elements until the stack is empty
        while (!resolvedStack.isEmpty()) {
            AnalystInfo analyst = resolvedStack.pop();
            
            System.out.println("Analyst Name: " + analyst.getAnalystName());
            System.out.println("Expertise Area: " + analyst.getExpertiseArea());
            
            double totalImpactCost = 0;
            System.out.print("Incidents Handled: ");
            
            // Loop to print incident IDs and calculate total cost
            for (IncidentInfo incident : analyst.getIncidents()) {
                System.out.print(incident.getIncidentId() + " ");
                totalImpactCost += incident.getImpactCost();
            }
            
            System.out.println("\nTotal Impact Cost Resolved: RM " + String.format("%.2f", totalImpactCost));
            System.out.println("-------------------------------------------");
        }
    }
}