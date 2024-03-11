package com.example.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class InstrumentManagement {

    private static final String INSERT_INSTRUMENT_SQL = "INSERT INTO instrument (instrumentid, instrumentname, isavailable) VALUES (?, ?, ?)";
    private static final String BORROW_INSTRUMENT_SQL = "UPDATE instrument SET isavailable = FALSE WHERE instrumentid = ?";
    private static final String BORROW_LOG_DB_SQL = "CREATE TABLE IF NOT EXISTS borrow_log (id integer PRIMARY KEY, instrumentid text, borrower_phonenumber text, borrow_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, hasreturned BOOLEAN DEFAULT FALSE)";
    private static final String BORROW_LOG_SQL = "INSERT INTO borrow_log (instrumentid, borrower_phonenumber) VALUES (?, ?)";
    private static final String CHECK_INSTRUMENT_AVAILABILITY = "SELECT instrumentid from instrument where instrumentid = ?";
    private static final String CHECK_INSTRUMENT_AVAILABILITY_FOR_BORROW = "SELECT isavailable from instrument where instrumentid = ?";
    private static final String CHECK_IF_RETURNED = "SELECT hasreturned from borrow_log where id = ?";
    private static final String RETURN_SQL = "UPDATE borrow_log SET hasreturned = TRUE where id = ?";
    private static final String FETCH_INSTRUMENT_ID = "SELECT instrumentid from borrow_log where id = ?";
    private static final String MAKE_INSTRUMENT_AVAILABLE = "UPDATE instrument SET isavailable = TRUE where instrumentid = ?";

    public static void InstrumentAdd(Connection connection, Instrument instrument) {
        try {
            PreparedStatement checkInstrumentPreparedStatement = connection.prepareStatement(CHECK_INSTRUMENT_AVAILABILITY);
            checkInstrumentPreparedStatement.setString(1, instrument.getInstrumentID());
            ResultSet result = checkInstrumentPreparedStatement.executeQuery();
            boolean instrumentExists = false;
            while (result.next()) {
                if (result.getString("instrumentid").equals(instrument.getInstrumentID())) {
                    instrumentExists = true;
                    break;
                }
            }
            if (!instrumentExists) {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INSTRUMENT_SQL);
                preparedStatement.setString(1, instrument.getInstrumentID());
                preparedStatement.setString(2, instrument.getInstrumentName());
                preparedStatement.setBoolean(3, instrument.isAvailable());
                preparedStatement.execute();
                System.out.println("Instrument added: " + instrument.getInstrumentName());
            } else {
                System.out.println("Instrument already exists.");
            }
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    public static void InstrumentBorrow(Connection connection, String instrumentID, String phoneNumber) {
        try {
            Statement statement = connection.createStatement();
            statement.execute(BORROW_LOG_DB_SQL);
            PreparedStatement borrowAvailabilityPreparedStatement = connection.prepareStatement(CHECK_INSTRUMENT_AVAILABILITY_FOR_BORROW);
            borrowAvailabilityPreparedStatement.setString(1, instrumentID);
            ResultSet resultSet = borrowAvailabilityPreparedStatement.executeQuery();
            boolean instrumentAvailable = false;
        
            while (resultSet.next()) {
                if (resultSet.getBoolean("isavailable")==true) {
                    instrumentAvailable = true;
                    break;
                }
            }
    
            if (instrumentAvailable) {
                PreparedStatement instrumentBorrowStatement = connection.prepareStatement(BORROW_INSTRUMENT_SQL);
                instrumentBorrowStatement.setString(1, instrumentID);
                instrumentBorrowStatement.executeUpdate();
                
                PreparedStatement borrowLogStatement = connection.prepareStatement(BORROW_LOG_SQL);
                borrowLogStatement.setString(1, instrumentID);
                borrowLogStatement.setString(2, phoneNumber);
                borrowLogStatement.executeUpdate();
    
                System.out.println("Instrument borrowed: " + instrumentID);
            } else {
                System.out.println("Instrument not available for borrowing.");
            }
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
    

    public static void InstrumentReturn(Connection connection, int borrowID) {
        try{
            PreparedStatement ifReturnedPreparedStatement = connection.prepareStatement(CHECK_IF_RETURNED);
            ifReturnedPreparedStatement.setInt(1, borrowID);
            ResultSet resultReturnCheck = ifReturnedPreparedStatement.executeQuery();
            boolean hasReturned = false;
            while(resultReturnCheck.next()){
                if(resultReturnCheck.getBoolean("hasreturned")==true){
                    hasReturned = true;
                    System.out.println("Borrow with id "+borrowID+" has already been returned.");
                    break;
                }
            }
            if(!hasReturned){
                PreparedStatement returnInstruPreparedStatement = connection.prepareStatement(RETURN_SQL);
                returnInstruPreparedStatement.setInt(1, borrowID);
                returnInstruPreparedStatement.execute();

                PreparedStatement fetchInstrumentPreparedStatement = connection.prepareStatement(FETCH_INSTRUMENT_ID);
                fetchInstrumentPreparedStatement.setInt(1, borrowID);
                ResultSet resultSet_Fetch = fetchInstrumentPreparedStatement.executeQuery();
                if(resultSet_Fetch.next()){
                    PreparedStatement updateAvailabilitypPreparedStatement = connection.prepareStatement(MAKE_INSTRUMENT_AVAILABLE);
                    updateAvailabilitypPreparedStatement.setString(1, resultSet_Fetch.getString("instrumentid"));
                    updateAvailabilitypPreparedStatement.execute();
                }

                System.out.println("Borrow with id "+borrowID+" has been returned.");

            }
        }
        catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }

        
    }
}