package gameobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameSquareTest {
	
	private GameSquare darkSquare;
	private GameSquare lightSquare;
	
	@BeforeEach
	void setUp() throws Exception {
		darkSquare = new GameSquare(Color.BLACK);
		lightSquare = new GameSquare(Color.WHITE);
	}

	@AfterEach
	void tearDown() throws Exception {
		darkSquare = null;
		lightSquare = null;
	}

	@Test
	void testConstructor() {
		assertTrue(darkSquare.getColor() == Color.BLACK);
		assertTrue(lightSquare.getColor() == Color.WHITE);
		assertTrue(darkSquare.isEmpty());
		assertTrue(lightSquare.isEmpty());
	}

	@Test
	void testPieceManagement() {
		GamePiece piece = new GamePiece();
		darkSquare.setPiece(piece);
		assertFalse(darkSquare.isEmpty());
		assertTrue(darkSquare.getPiece() == piece);
		darkSquare.removePiece();
		assertTrue(darkSquare.isEmpty());
		assertNull(darkSquare.getPiece());
	}
	
}
