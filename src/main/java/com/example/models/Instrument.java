package com.example.models;

public class Instrument {
    String instrumentID;
    String instrumentName;
    boolean isAvailable = true;
    
    public Instrument(String instrumentID, String instrumentName, boolean isAvailable) {
        this.instrumentID = instrumentID;
        this.instrumentName = instrumentName;
        this.isAvailable = isAvailable;
    }

    public String getInstrumentID() {
        return instrumentID;
    }

    public void setInstrumentID(String instrumentID) {
        this.instrumentID = instrumentID;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    
}
