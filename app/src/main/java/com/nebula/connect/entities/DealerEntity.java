package com.nebula.connect.entities;

/**
 * Created by Sonam on 29/3/17.
 */
public class DealerEntity {

    public int dealerId;
    public String meetingId;
    public String vId;
    public String name;
    public String code;
    public String contact1;
    public String contact2;
    public String stateId;
    public String villageId;
    public String dealer_field1;
    public String dealer_field2;
    public String dealer_field3;
    public String dealer_field4;
    public String dealer_field5;


    @Override
    public String toString() {
        String retVal="dealerId ="+dealerId+"meetingId = "+meetingId+" vId ="+vId+" name ="+name+"code ="+code +
                "contact1 ="+contact1+" contact2 ="+contact2+" stateId = "+stateId+" villageId = "+villageId+" dealer_field1 ="+dealer_field1+
                "dealer_field2 ="+dealer_field2+" dealer_field3 ="+dealer_field3+" dealer_field4 ="+dealer_field4
                +"dealer_field5 = "+dealer_field5;

        return retVal;
    }
}
