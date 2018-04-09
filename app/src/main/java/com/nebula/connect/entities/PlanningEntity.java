package com.nebula.connect.entities;

/**
 * Created by Sonam on 29/3/17.
 */
public class PlanningEntity {

    public int planningId;
    public String vId;
    public String division;
    public String depo;
    public String depoCode;
    public String meetingType;
    public String remarks;
    public String meetingCode;
    public String date;
    public String trainingCenterName;
    public String startTime;
    public String statedId;
    public String villageId;
    public String location;
    public String estimateAttendance;
    public String nerolacTsi;
    public String nerolacTsiContact;
    public String budgetPerMeet;
    public String asmName;
    public String asmContact;
    public String dsmName;
    public String dsmContact;
    public String month;
    public String status;
    public String planning_field1;
    public String planning_field2;
    public String planning_field3;
    public String planning_field4;
    public String planning_field5;



    @Override
    public String toString() {
        String retVal="planningId ="+planningId+" vId ="+vId+" villageId ="+villageId+"division ="+division +
                "depo ="+depo+" depoCode ="+depoCode+" meetingType ="+meetingType+" meetingCode ="+meetingCode+
                "date ="+date+" trainingCenterName ="+trainingCenterName+" startTime ="+startTime+"statedId = "+statedId+"villageId ="+villageId
                +"location = "+location+" estimateAttendance ="+estimateAttendance+
                "nerolacTsi ="+nerolacTsi+" nerolacTsiContact ="+nerolacTsiContact+" budgetPerMeet ="+budgetPerMeet+" asmName ="+asmName+
                "asmContact ="+asmContact+" dsmName ="+dsmName+" dsmContact ="+dsmContact+"month = "+month+" status = "+status+"planning_field1 ="+planning_field1
                +"planning_field2 = "+planning_field2+" planning_field3 ="+planning_field3
                +"planning_field4 = "+planning_field4+" planning_field5 ="+planning_field5;

        return retVal;
    }
}
