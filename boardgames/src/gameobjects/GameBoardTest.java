/*
* Frankie Wilson
* CS 122
* File provided by professor
*/

package gameobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameBoardTest {

	private GameBoard board;
	private GameCoordinate a1 = new GameCoordinate('a', 1);
	private GameCoordinate a2 = new GameCoordinate('a', 2);
	private GameCoordinate c4 = new GameCoordinate('c', 4);
	
	@BeforeEach
	void setUp() throws Exception {
		board = new GameBoard(8, Color.BLACK, Color.WHITE);
	}

	@AfterEach
	void tearDown() throws Exception {
		board = null;
	}

	@Test
	void testConstructor() {
		assertTrue(board.getSize() == 8);
		assertTrue(board.getColor(a1) == Color.BLACK);
		assertTrue(board.getColor(a2) == Color.WHITE);
		assertTrue(board.getColor(c4) == Color.WHITE);
		assertTrue(board.isEmpty(a1));
		assertTrue(board.isEmpty(a2));
		assertTrue(board.isEmpty(c4));
	}

	@Test
	void testPieceManagement() {
		GamePiece pieceOne = new GamePiece();
		GamePiece pieceTwo = new GamePiece();
		board.setPiece(a1, pieceOne);
		board.setPiece(a2, pieceTwo);
		assertFalse(board.isEmpty(a1));
		assertFalse(board.isEmpty(a2));
		assertTrue(board.isEmpty(c4));
		assertTrue(board.getPiece(a1) == pieceOne);
		assertTrue(board.getPiece(a2) == pieceTwo);
		board.removePiece(a1);
		board.removePiece(a2);
		assertTrue(board.isEmpty(a1));
		assertTrue(board.isEmpty(a2));
	}
}
