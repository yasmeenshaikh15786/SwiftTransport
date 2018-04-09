package com.nebula.connect.entities;

/**
 * Created by Sonam on 29/3/17.
 */
public class PainterEntity {

    public int painterId;
    public String planningId;
    public String vId;
    public String villageId;
    public String painterName;
    public String nppCode;
    public String dealerName;
    public String dealerId;
    public String contact;
    public String detailStartTime;
    public String status;
    public String business_started_year;
    public String qualification;
    public String team_size;
    public String dealer_code;
    public String dealername;
    public String dealer_contact;
    public String order_booking;
    public String remark1;
    public String remark2;
    public String painter_field1;
    public String painter_field2;
    public String painter_field3;
    public String painter_field4;
    public String painter_field5;






    @Override
    public String toString() {
        String retVal="painterId ="+painterId+"planningId ="+planningId+" vId ="+vId+" villageId ="+villageId+"painterName ="+painterName +
                "nppCode ="+nppCode+" dealerName ="+dealerName+" dealerId ="+dealerId+" contact ="+contact+
                "detailStartTime ="+detailStartTime+" status ="+status+" business_started_year ="+business_started_year+
                " qualification ="+qualification +" team_size ="+team_size+" dealer_code ="+dealer_code+" dealername ="+dealername+
                " dealer_contact ="+dealer_contact+" order_booking ="+order_booking +" remark1 ="+remark1+" remark2 ="+remark2+" painter_field1 ="+painter_field1+"painter_field2 = "+painter_field2+"painter_field3 ="+painter_field3
                +"painter_field4 = "+painter_field4+" painter_field5 ="+painter_field5;

        return retVal;
    }
}
