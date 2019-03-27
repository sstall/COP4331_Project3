package course.oop.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class playerDB {
	String dbURL;
	
	public playerDB() throws SQLException {
		dbURL = "./res/players.db";
		Connection conn = connectDB();
		
		String sql = "CREATE TABLE IF NOT EXISTS players ("
				+ "name text PRIMARY KEY,"
				+ "marker text NOT NULL,"
				+ "numWins integer NOT NULL);";
		
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
		conn.close();
	}
	
	public playerDB(String URL) throws SQLException {
		dbURL = URL;
		Connection conn = connectDB();
		
		String sql = "CREATE TABLE IF NOT EXISTS players ("
				+ "name text PRIMARY KEY,"
				+ "marker text NOT NULL,"
				+ "numWins integer NOT NULL);";
		
		Statement stmt = conn.createStatement();
		stmt.execute(sql);
		conn.close();
	}
	
	private Connection connectDB() throws SQLException {
		return DriverManager.getConnection("jdbc:sqlite:" + dbURL);
	}
	
	/**
	 * @param name
	 * @param marker
	 * @return True if successful, false if name already exists
	 * @throws SQLException
	 */
	public boolean addPlayer(String name, String marker) throws SQLException {
		Connection conn = connectDB();
		
		String sql = "SELECT marker FROM players WHERE name = ?";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,  name);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			conn.close();
			return false;
		}
		
		sql = "INSERT INTO players(name, marker,numWins) VALUES(?,?,?)";
		
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		pstmt.setString(2, marker);
		pstmt.setInt(3, 0);
		pstmt.executeUpdate();
		
		conn.close();
		return true;
	}
	
	public void removePlayer(String name) throws SQLException {
		Connection conn = connectDB();
		
		String sql = "DELETE FROM players WHERE name = ?";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		pstmt.executeUpdate();
	}
	
	public boolean increaseNumWins(String name) throws SQLException {
		Connection conn = connectDB();
		
		String sql = "SELECT numWins FROM players WHERE name = ?";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		ResultSet rs = pstmt.executeQuery();
		
		if(!rs.next()) {
			conn.close();
			return false;
		}
		
		sql = "UPDATE players SET numWins = ? WHERE name = ?";
		
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, rs.getInt("numWins") + 1);
		pstmt.setString(2, name);
		pstmt.execute();
		
		conn.close();
		return true;
	}
	
	public Vector<String> getPlayerByName(String name) throws SQLException {
		Connection conn = connectDB();
		
		String sql = "SELECT marker, numWins FROM players WHERE name = ?";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			Vector<String> res = new Vector<String>();
			
			res.add(rs.getString("marker"));
			res.add(rs.getInt("numWins") + "");
			conn.close();
			return res;
		}
		
		conn.close();
		return null;
	}
	
	public Vector<Vector<String>> getPlayers() throws SQLException {
		Connection conn = connectDB();
		
		String sql = "SELECT name, marker, numWins FROM players;";
		
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		Vector<Vector<String>> res = new Vector<Vector<String>>();
	
		while(rs.next()) {
			Vector<String> data = new Vector<String>();
			data.add(rs.getString("name"));
			data.add(rs.getString("marker"));
			data.add(rs.getInt("numWins")+"");
			res.add(data);
		}
		
		conn.close();
		return res;
	}
	
	public int getPlayerWins(String name) throws SQLException{
		Connection conn = connectDB();
		
		String sql = "SELECT numWins FROM players WHERE name = ?";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, name);
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			int res =  rs.getInt("numWins");
			conn.close();
			return res;
		}
		
		conn.close();
		return -1;
	}
	
	public boolean deleteDB() {
		File f = new File(dbURL);
		return f.delete();
	}
}
