package com.hst.ops.bean.support;

public class PartyStateList {

    private String state_id;
    private String state_name_ta;
    private String state_name_en;
    private String state_logo;

    public PartyStateList(String state_id, String state_name_en, String state_logo){

        this.state_id = state_id;
        this.state_name_en = state_name_en;
        this.state_logo = state_logo;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getState_name_ta() {
        return state_name_ta;
    }

    public void setState_name_ta(String state_name_ta) {
        this.state_name_ta = state_name_ta;
    }

    public String getState_name_en() {
        return state_name_en;
    }

    public void setState_name_en(String state_name_en) {
        this.state_name_en = state_name_en;
    }

    public String getState_logo() {
        return state_logo;
    }

    public void setState_logo(String state_logo) {
        this.state_logo = state_logo;
    }
}
