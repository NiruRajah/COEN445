package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServerDB {
	
	private static Connection con;
	private static boolean hasData = false;
	
	public void getConnection() throws ClassNotFoundException, SQLException {
		
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:Server.db");
		initialise();
	}
	
	public void initialise() throws SQLException {
		if(!hasData) {
			hasData = true;
		
		 // check for client table
		 Statement s = con.createStatement();
		 ResultSet rs = s.executeQuery("SELECT 'client' FROM sqlite_master WHERE type='table' AND name='client'");
		 if( !rs.next()) {
			 System.out.println("Building Client Table!");
			 // need to build the table
			 Statement s2 = con.createStatement();
				//ResultSet res = state.executeQuery("SELECT * FROM sqlite_master WHERE type='table' and name='user'");
				s2.execute("CREATE TABLE client(id integer, client varchar(60), room varchar(60), primary key(id));");
			 }
		}
		
		PreparedStatement ps = con.prepareStatement("INSERT INTO client values(?,'Client 10','118');");
		ps.execute();
			
		}
	
	public ResultSet displayClients() throws ClassNotFoundException, SQLException {
		if(con == null) {
			 // get connection
			 getConnection();
		 }
		 Statement s = con.createStatement();
		 ResultSet res = s.executeQuery("select client, room from client");
		 return res;
	}

}
