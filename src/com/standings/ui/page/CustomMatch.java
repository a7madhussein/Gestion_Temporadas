package com.standings.ui.page;

import java.io.Serializable;

public class CustomMatch extends Match implements Serializable {

    private static final long serialVersionUID = 1L;

    private String customField1;
    private int customField2;

    public CustomMatch(Team homeTeam, Team awayTeam, int roundNumber, int matchNumber, String customField1, int customField2) {
        super(homeTeam, awayTeam, roundNumber, matchNumber);
        this.customField1 = customField1;
        this.customField2 = customField2;
    }

    public String getCustomField1() {
        return customField1;
    }

    public void setCustomField1(String customField1) {
        this.customField1 = customField1;
    }

    public int getCustomField2() {
        return customField2;
    }

    public void setCustomField2(int customField2) {
        this.customField2 = customField2;
    }

    @Override
    public String toString() {
        return "CustomMatch{" +
                "customField1='" + customField1 + '\'' +
                ", customField2=" + customField2 +
                "} " + super.toString();
    }

    @Override
    public int hashCode() {
       
        return super.hashCode();
    }
}

