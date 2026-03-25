
/**
 * Write a description of class IncidentInfo here.
 *
 * @author (Denish Zikry)
 * @version (25/3/26)
 */
public class IncidentInfo
{
    protected String incidentId;
    protected String incidentType;
    protected int severityLevel;
    protected String reportDate;
    protected int estimatedResolutionTime;
    protected double impactCost;
    //constructor
    public IncidentInfo(String incidentId,String incidentType,int severityLevel,String reportDate,int ERT,double impactCost){
        this.incidentId = incidentId;
        this.incidentType = incidentType;
        this.severityLevel = severityLevel;
        this.reportDate = reportDate;
        estimatedResolutionTime = ERT;
        this.impactCost = impactCost;
    }
    //getter method
    public String getIncidentId(){ 
        return incidentId; 
    }
    public String getIncidentType(){
        return incidentType; 
    }
    public int getSeverityLevel(){ 
        return severityLevel; 
    }
    public String getReportDate(){ 
        return reportDate; 
    }
    public int getERT(){
        return estimatedResolutionTime; 
    }
    public double getImpactCost(){ 
        return impactCost; 
    }
    //format for display
    public String toString() {
        return String.format("[%s] %s (Sev: %d, Cost: RM%.2f)",incidentId, incidentType, severityLevel, impactCost);
    }
    
}