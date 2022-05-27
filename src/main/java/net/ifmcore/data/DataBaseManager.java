package net.ifmcore.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import net.ifmcore.Uni;

public class DataBaseManager {
	public static DataBaseManager instance;
	
	String url = "jdbc:sqlite:"+Uni.pluginFolder+"/DataBase.db";
    
    private static Connection con;
    
    public DataBaseManager() {
    	instance = this;
    	File dbFile = new File(Uni.pluginFolder+"/DataBase.db");
    	if(!dbFile.exists()) {
    		try {
    			dbFile.createNewFile();
				makeDataBase();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
    	}
    	try {
    		con = DriverManager.getConnection(url);
    	}
    	catch(SQLException e) {
    		if(e.getMessage().startsWith("Unknown database")) {
    			try {
    				makeDataBase();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
    		}
    		else
    			e.printStackTrace();
    	}
    }
    
    void makeDataBase() throws SQLException {
    	con = DriverManager.getConnection(url);
    	Statement stmt = con.createStatement();
		stmt.execute("CREATE TABLE settings (name TEXT, settings TEXT)");
		stmt.execute("CREATE TABLE stats (name TEXT, badges TEXT)");
		stmt.execute("CREATE TABLE auth (name TEXT, password TEXT, ip TEXT, lastAuthTime TEXT)");
		stmt.execute("CREATE TABLE ip (name TEXT, ip TEXT, lastonline TEXT, lastpos TEXT)");
		stmt.execute("CREATE TABLE items (name TEXT, item TEXT, amount INTEGER)");
    }
    
    public static boolean checkInDataBase(String table, String column, String condition) {
    	try {
    		String[] data = getData(table, new String[] {column}, condition);
    		if(!data[0].equals(""))
    			return true;
    	}
    	catch(Exception e) {
    	}
    	return false;
    }
    
    public static void insertData(String table, String values) {
    	try {
    		Statement stmt = con.createStatement();
    		stmt.execute("INSERT INTO "+table+" values ("+values+")");
    		stmt.close();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void setData(String table, String values, String condition) {
    	try {
    		Statement stmt = con.createStatement();
    		stmt.execute("UPDATE "+table+" SET "+values+" where "+condition);
    		stmt.close();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    public static void addToData(String table, String[] columns, Object[] values, String condition) {
    	String[] data = getData(table, columns, condition);
    	String valuesWithNames = "";
    	
    	for(int i = 0; i < data.length; i++) {
    		if(i != 0)
    			valuesWithNames += ", ";
    		
    		if(values[i] instanceof String)
    			valuesWithNames += columns[i]+"=\""+values[i] + data[i]+"\"";
    		else if(values[i] instanceof Integer)
    			valuesWithNames += columns[i]+"="+String.valueOf((int)values[i] + Integer.parseInt(data[i]));
    		else if(values[i] instanceof Float)
    			valuesWithNames += columns[i]+"=\""+String.valueOf((float)values[i] + Float.parseFloat(data[i]))+"\"";
    		else
    			valuesWithNames += columns[i]+"\""+data[i]+"\"";
    	}
    	setData(table, valuesWithNames, condition);
    }
    
    public static String[] getData(String table, String[] columns, String condition) {
    	try {
    		Statement stmt = con.createStatement();
    		ResultSet result = stmt.executeQuery("SELECT * FROM "+table+" where "+condition);
    		String[] data = new String[columns.length];
    		while (result.next()) {
    			for(int i = 0; i < columns.length; i++)
    				data[i] = result.getString(columns[i]);
            }
    		stmt.close();
    		result.close();
    		return data;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	return new String[columns.length];
    }
    
    public static List<String[]> getDataAll(String table, String[] columns, String condition) {
    	try {
    		Statement stmt = con.createStatement();
    		ResultSet result = stmt.executeQuery("SELECT * FROM "+table+" where "+condition);
    		List<String[]> data = new ArrayList<String[]>();
    		while (result.next()) {
    			String[] row = new String[columns.length];
    			for(int i = 0; i < columns.length; i++)
    				row[i] = result.getString(columns[i]);
    			data.add(row);
            }
    		stmt.close();
    		result.close();
    		return data;
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    	return new ArrayList<String[]>();
    }
    
    public static List<List<String>> getAllTableData(String table) {
    	try {
    		Statement stmt = con.createStatement();
    		ResultSet result = stmt.executeQuery("SELECT * FROM "+table);
    		ResultSetMetaData rsMetaData = result.getMetaData();
    		int count = rsMetaData.getColumnCount();
    		List<List<String>> data = new ArrayList<List<String>>();
    		while (result.next()) {
    			List<String> row = new ArrayList<String>();
                for (int i = 1; i < count + 1; i++) {
                	row.add(String.valueOf(result.getObject(i)));
                }
                data.add(row);
    		}
    		return data;
    	}
    	catch(Exception e) {
    	}
    	return new ArrayList<List<String>>();
    }
    
    public static void deleteData(String table, String condition) {
    	try {
    		Statement stmt = con.createStatement();
    		stmt.execute("DELETE FROM "+table+" WHERE "+condition+"");
    		stmt.close();
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
