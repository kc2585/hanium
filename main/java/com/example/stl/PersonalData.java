package com.example.stl;

/* id= 출발지
   name = 목적지
   country = user id
 */


public class PersonalData {
    private String member_id;
    private String member_Start;
    private String member_Goal;

    public String getMember_id(){
        return member_id;
    }

    public String getMember_Start() {
        return member_Start;
    }

    public String getMember_Goal() {
        return member_Goal;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public void setMember_Start(String member_Start) {
        this.member_Start = member_Start;
    }

    public void setMember_Goal(String member_Goal) {
        this.member_Goal = member_Goal;
    }
}
