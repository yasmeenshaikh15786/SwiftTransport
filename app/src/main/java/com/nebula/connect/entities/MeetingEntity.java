package com.nebula.connect.entities;

/**
 * Created by Sonam on 29/3/17.
 */
public class MeetingEntity {

    public int planningId;
    public String vId;
    public String hotelName;
    public String painterAttendance;
    public String nonParticipants;
    public String meetiingDetailsStart;
    public String totalGiftsGiven;
    public String detailStartTime;
    public String status;
    public String meeting_field1;
    public String meeting_field2;
    public String meeting_field3;
    public String meeting_field4;
    public String meeting_field5;


    @Override
    public String toString() {
        String retVal="planningId ="+planningId+" vId ="+vId+" hotelName ="+hotelName+"painterAttendance ="+painterAttendance +
                "nonParticipants ="+nonParticipants+" meetiingDetailsStart ="+meetiingDetailsStart+" totalGiftsGiven ="+totalGiftsGiven+
                "detailStartTime ="+detailStartTime+" status ="+status+" meeting_field1 ="+meeting_field1+"meeting_field2 = "+meeting_field2+"meeting_field3 ="+meeting_field3
                +"meeting_field4 = "+meeting_field4+" meeting_field5 ="+meeting_field5;

        return retVal;
    }
}
