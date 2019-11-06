package com.example.stl;

/* id= 출발지
   name = 목적지
   country = user id
 */


public class PersonalData {
    private String member_starting_point;
    private String member_destination;
    private String member_id;

    public String getMember_id(){
        return member_starting_point;
    }

    public String getMember_name() {
        return member_destination;
    }

    public String getMember_address() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_starting_point = member_id;
    }

    public void setMember_name(String member_name) {
        this.member_destination = member_name;
    }

    public void setMember_address(String member_address) {
        this.member_id = member_address;
    }
}
