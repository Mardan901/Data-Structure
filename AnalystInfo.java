
/**
 * Write a description of class AnalystInfo here.
 *
 * @author (Denish Zikry)
 * @version (25/3/26)
 */
import java.util.*;
public class AnalystInfo
{
    protected String analystId;
    protected String analystName;
    protected String expertiseArea;
    protected LinkedList<IncidentInfo> incidents;
    //contructor
    public AnalystInfo(String analystId, String analystName,String expertiseArea){
        this.analystId = analystId;
        this.analystName = analystName;
        this.expertiseArea = expertiseArea;
        this.incidents = new LinkedList<>();
    }
    //to add incident into analyst
    public void addIncident(IncidentInfo incident) { 
        this.incidents.add(incident); 
    }
    //getter method
    public String getAnalystId(){
        return analystId;
    }
    public String getAnalystName(){
        return analystName;
    }
    public String getExpertiseArea(){
        return expertiseArea;
    }
    public List<IncidentInfo> getIncidents(){
        return incidents;
    }
    
    public int getIncidentCount(){
        return incidents.size();
    }
}