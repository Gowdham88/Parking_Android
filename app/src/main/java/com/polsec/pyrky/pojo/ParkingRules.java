package com.polsec.pyrky.pojo;

public class ParkingRules {

    private String mRule;
    private String mTiming;
    private String mCharges;
    private String mMessage;

    public ParkingRules(String mRule, String mTiming, String mCharges, String mMessage) {
        this.mRule = mRule;
        this.mTiming = mTiming;
        this.mCharges = mCharges;
        this.mMessage = mMessage;
    }

    public String getmRule() {
        return mRule;
    }

    public void setmRule(String mRule) {
        this.mRule = mRule;
    }

    public String getmTiming() {
        return mTiming;
    }

    public void setmTiming(String mTiming) {
        this.mTiming = mTiming;
    }

    public String getmCharges() {
        return mCharges;
    }

    public void setmCharges(String mCharges) {
        this.mCharges = mCharges;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }
}
