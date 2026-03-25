
/**
 * Write a description of class DataReader here.
 *
 * @author (Denish Zikry)
 * @version (25/3/26)
 */
import java.util.*;
import java.io.*;
public class DataReader
{
    public static void main(String[]args){
        try{
            BufferedReader in = new BufferedReader (new FileReader("cyber_incidents.txt"));

            LinkedList<AnalystInfo> analystList = new LinkedList<>();
            String indata;
            AnalystInfo currentAnalyst;

            while((indata = in.readLine()) != null){
                StringTokenizer st= new StringTokenizer (indata, "|");
                //Parse Analyst Data
                String sAnalystId = st.nextToken();
                String sAnalystName = st.nextToken();
                String sExpertiseArea = st.nextToken();

                // Parse Incident Data
                String sIncidentId = st.nextToken();
                String sIncidentType = st.nextToken();
                int iSeverityLevel = Integer.parseInt(st.nextToken());
                String sReportDate = st.nextToken();
                int iERT = Integer.parseInt(st.nextToken());
                double dImpactCost = Double.parseDouble(st.nextToken());

                IncidentInfo newIncident = new IncidentInfo(sIncidentId, sIncidentType, iSeverityLevel, sReportDate, iERT, dImpactCost);

                currentAnalyst = null; 
                for (int i = 0; i < analystList.size(); i++) {
                    if (analystList.get(i).getAnalystId().equals(sAnalystId)) {
                        currentAnalyst = analystList.get(i);
                        break; // Stop searching once found
                    }

                }
                if (currentAnalyst == null) {
                    currentAnalyst = new AnalystInfo(sAnalystId, sAnalystName, sExpertiseArea);
                    analystList.add(currentAnalyst);
                }

                currentAnalyst.addIncident(newIncident);
            }
            in.close();
            System.out.println("Data successfully loaded!");
        }catch(Exception ex){
            System.out.println("Error reading file or invalid input: " + ex.getMessage());
        }

    }
}