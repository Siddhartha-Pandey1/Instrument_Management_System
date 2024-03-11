package com.example;

import java.sql.*;

import com.example.models.Instrument;
import com.example.models.InstrumentManagement;

public class Main {
    public static final String INSTRUMENT_TABLE = "instrument";
    public static final String COLUMN_ID = "instrumentid";
    public static final String COLUMN_NAME = "instrumentname";
    public static final String COLUMN_AVAIBILITY = "isavailable";


    public static void main(String[] args) {
        String url = "jdbc:sqlite:mydb.db";
        try{
            Connection connection = DriverManager.getConnection(url);

            System.out.println("Connected");

            String createTableSQL = "CREATE TABLE IF NOT EXISTS "+INSTRUMENT_TABLE+"("+ COLUMN_ID +" text PRIMARY KEY NOT NULL," +
                    COLUMN_NAME+" text," +
                    COLUMN_AVAIBILITY +" boolean)";
            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);
            System.out.println("Database Created");

            Instrument i1 = new Instrument("GUITAR-023", "GUITAR", true);

            Instrument i2 = new Instrument("PIANO-1", "Piano", true);

            InstrumentManagement.InstrumentAdd(connection, i1);

            InstrumentManagement.InstrumentAdd(connection ,i2);

            InstrumentManagement.InstrumentBorrow(connection, "GUITAR-023", "9801234567");

            InstrumentManagement.InstrumentReturn(connection, 1);

            InstrumentManagement.InstrumentReturn(connection, 2);

            connection.close();
        }
        catch(Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}