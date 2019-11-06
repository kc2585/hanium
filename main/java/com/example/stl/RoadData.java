package com.example.stl;

import java.io.Serializable;

// 길 안내를 위해 쓰이는 Array. DB에 저장되어 있는 b_pos,n_pos,Guide를 받아온다.
// 사용자가 정한 Array를 intent로 넘길때는 array class에서 implements Serializable를 해야한다고 한다
// 다른 방법도 있는데 이게 가장 간편한거 같다.
public class RoadData implements Serializable {
    private String member_b_pos;
    private String member_n_pos;
    private String member_Guide;

    public String getMember_b_pos(){
        return member_b_pos;
    }

    public String getMember_n_pos() {
        return member_n_pos;
    }

    public String getMember_Guide() {
        return member_Guide;
    }

    public void setMember_b_pos(String member_b_pos) {
        this.member_b_pos = member_b_pos;
    }

    public void setMember_n_pos(String member_n_pos) {
        this.member_n_pos = member_n_pos;
    }

    public void setMember_Guide(String member_Guide) {
        this.member_Guide = member_Guide;
    }
}
