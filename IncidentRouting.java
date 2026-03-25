import java.util.*;

public class IncidentRouting {
    public static void routeAnalysts(LinkedList<AnalystInfo> analystList) {
        // Create the three queues required in Phase 2
        Queue<AnalystInfo> internalQueue = new LinkedList<>();
        Queue<AnalystInfo> externalQueue = new LinkedList<>();
        Queue<AnalystInfo> criticalQueue = new LinkedList<>();

        // This variable helps alternate between Queue 1 and Queue 2
        boolean assignToInternal = true;

        // Loop through every analyst in the LinkedList
        for (AnalystInfo analyst : analystList) {

            // Get the number of incidents assigned to this analyst
            int incidentCount = analyst.getIncidents().size();

            // If analyst has 3 or fewer incidents
            if (incidentCount <= 3) {
                
                // Alternate assignment between Internal and External queue
                if (assignToInternal) {

                    internalQueue.add(analyst); // Add analyst to Internal Queue

                } else {

                    externalQueue.add(analyst); // Add analyst to External Queue
                }

                // Toggle the boolean value to alternate next assignment
                assignToInternal = !assignToInternal;

            }

            // If analyst has more than 3 incidents
            else {

                // Assign analyst to Critical Queue
                criticalQueue.add(analyst);
            }
        }

        //Display all queues
        displayQueue("Internal Threat Queue", internalQueue);
        displayQueue("External Threat Queue", externalQueue);
        displayQueue("Critical Incident Queue", criticalQueue);
    }
    public static void displayQueue(String queueName, Queue<AnalystInfo> queue) {

        System.out.println("\n===== " + queueName + " ====");

        // Loop through every analyst in the queue
        for (AnalystInfo analyst : queue) {

            System.out.println("Analyst Name: " + analyst.getAnalystName());
            System.out.println("Expertise Area: " + analyst.getExpertiseArea());

            int totalImpactCost = 0;

            System.out.println("Incidents Assigned:");

            // Display each incident handled by the analyst
            for (IncidentInfo incident : analyst.getIncidents()) {
                
                System.out.println(incident.getIncidentId() + " - " + incident.getIncidentType());

                // Add the impact cost to the total
                totalImpactCost += incident.getImpactCost();

            }

            System.out.println("Total Impact Cost: RM " + totalImpactCost);
            System.out.println("----------------------------");
        }
    }
}
