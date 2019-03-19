package test.course.oop.db;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.Vector;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import course.oop.db.playerDB;

class playerDBTest {
	
	playerDB db;
	
	@BeforeEach
	void setUp() throws Exception {
		db = new playerDB();
	}
	
	@AfterEach
	void tearDown() throws Exception {
		db.deleteDB();
	}
	

	@Test
	void addNewPlayerTest() {
		try {
			boolean res = db.addPlayer("Sam", "S");
			if(!res) {
				fail();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	@Test
	void getPlayerTest() {
		try {
			db.addPlayer("Sam", "S");
			Vector<String> res = db.getPlayerByName("Sam");
			System.out.println(res);
			if(res == null) {
				fail();
			}
			assertEquals("S", res.get(0));
			assertEquals("0", res.get(1));
			
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	void newPlayerZeroWins() {
		try {	
			db.addPlayer("Sam", "S");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		try {
			int wins = db.getPlayerWins("Sam");
			assertEquals(0, wins);
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	void increasePlayerWins() {
		try {
			db.addPlayer("Sam", "S");
			db.increaseNumWins("Sam");
			
			int wins = db.getPlayerWins("Sam");
			
			assertEquals(1, wins);
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	}

}
