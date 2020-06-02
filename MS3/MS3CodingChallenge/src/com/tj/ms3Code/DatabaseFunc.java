package com.tj.ms3Code;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.SwingWorker;

public class DatabaseFunc {

	class Task extends SwingWorker {

		@Override
		protected Object doInBackground() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	/**
	 * Connects to database "fileName"
	 * 
	 * @param String fileName
	 * @return Connection
	 * */
	private Connection connect(String fileName) {
        // SQLite connection string
        String url = "jdbc:sqlite:"+ System.getProperty("user.dir") + "\\output\\" + fileName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
	
	/**
	 * Create new database if not existing.
	 * Accepts String fileName, the desired name of database.
	 *  
	 * @return Boolean 
	 * */
	public Boolean createDatabase(String fileName) {
		Boolean result = false;

        String url = "jdbc:sqlite:"+ System.getProperty("user.dir") + "\\output\\" + fileName;

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name used for Connecting to Database is " + meta.getDriverName());
                createNewTable(url);
            	//main.lbl_DB_status.setText("Successfully connected to Local Database!");
            }
            result = true;
        	System.out.println("Successfully connected to Local Database!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
	
	/**
	 * Creates the table of the output database if no tables yet.
	 * */
	public void createNewTable(String url) {
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS parsed_data(\n"
                + "	A TEXT NOT NULL,\n"
                + "	B TEXT NOT NULL,\n"
                + "	C TEXT NOT NULL,\n"
                + "	D TEXT NOT NULL,\n"
                + "	E TEXT NOT NULL,\n"
                + "	F TEXT NOT NULL,\n"
                + "	G TEXT NOT NULL,\n"
                + "	H TEXT NOT NULL,\n"
                + "	I TEXT NOT NULL,\n"
                + "	J TEXT NOT NULL\n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	/**Insert single row data to SQLite Database
	 * */
	public void insert(String[] data) {
        String sql = "INSERT INTO parsed_data(A,B,C,D,E,F,G,H,I,J) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connect("outputDB.db");
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	for(int i = 0; i < 10; i++)
        	{
        		pstmt.setString(i+1, data[i]);
        	}
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	/**
	 * Insert List of data to SQLite Database
	 * */
	public void insertAll(ArrayList<String[]> data) {
        String sql = "INSERT INTO parsed_data(A,B,C,D,E,F,G,H,I,J) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = connect("outputDB.db");
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	conn.setAutoCommit(false);
        	for(String[] row : data)
        	{
            	for(int i = 0; i < 10; i++)
            	{
            		pstmt.setString(i+1, row[i]);
            	}
                pstmt.addBatch();
        	}
        	pstmt.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
