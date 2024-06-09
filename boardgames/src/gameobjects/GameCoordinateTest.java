package gameobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameCoordinateTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testa1() {
		GameCoordinate a1 = new GameCoordinate('a', 1);
		assertTrue(a1.fileIndex == 0);
		assertTrue(a1.rankIndex == 0);
		assertTrue(a1.getFileIndex() == 0);
		assertTrue(a1.getRankIndex() == 0);
		assertTrue(a1.getFile() == 'a');
		assertTrue(a1.getRank() == 1);
		a1 = new GameCoordinate(0, 0);
		assertTrue(a1.getFileIndex() == 0);
		assertTrue(a1.getRankIndex() == 0);

		assertTrue(a1.getRank() == 1);
	}
	
	@Test
	void testc4() {
		GameCoordinate c4 = new GameCoordinate('c', 4);
		assertTrue(c4.fileIndex == 2);
		assertTrue(c4.rankIndex == 3);
		assertTrue(c4.getFileIndex() == 2);
		assertTrue(c4.getRankIndex() == 3);
		assertTrue(c4.getFile() == 'c');
		assertTrue(c4.getRank() == 4);
		c4 = new GameCoordinate(2, 3);
		assertTrue(c4.getFileIndex() == 2);
		assertTrue(c4.getRankIndex() == 3);
		assertTrue(c4.getFile() == 'c');
		assertTrue(c4.getRank() == 4);
	}
}
