package gameobjects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NQueensSolverTest {

	private GameBoard board;
	private NQueensSolver solver;
	private GameCoordinate b1 = new GameCoordinate('b', 1);
	private GameCoordinate f2 = new GameCoordinate('f', 2);
	private GameCoordinate a3 = new GameCoordinate('a',3);
	private GameCoordinate b3 = new GameCoordinate('b',3);
	private GameCoordinate c3 = new GameCoordinate('c',3);
	private GameCoordinate d3 = new GameCoordinate('d',3);
	private GameMove row1Move = new GameMove(new Queen(), null, b1);
	private GameMove row2Move = new GameMove(new Queen(), null, f2);
	
	@BeforeEach
	void setUp() throws Exception {
		board = new GameBoard(8, Color.BLACK, Color.WHITE);
		solver = new NQueensSolver(board); //?? help
	}

	@AfterEach
	void tearDown() throws Exception {
		board = null;
		solver = null;
	}

	@Test
	void testIsSolved() {
		for(int i = 0; i < board.getSize(); ++i) {
			assertFalse(solver.puzzleIsSolved());
			// note: just forcing moves into the list to check this
			solver.getMoves().add(row1Move);
		}
		assertTrue(solver.puzzleIsSolved());
	}

	@Test
	void testCurrentRankIndex() {
		for(int i = 0; i < board.getSize(); ++i) {
			assertTrue(solver.currentRankIndex() == i);
			// see comment in method above
			solver.getMoves().add(row1Move);
		}
	}
	
	@Test
	void testIsThreatenedBy(){
		assertFalse(solver.isThreatenedBy(a3, b1));
		assertTrue(solver.isThreatenedBy(b3, b1));
		assertFalse(solver.isThreatenedBy(c3, b1));
		assertTrue(solver.isThreatenedBy(d3, b1));
	}
	
	@Test
	void testIsSafe() {
		solver.makeMove(row1Move);
		solver.makeMove(row2Move);
		assertTrue(solver.isSafe(a3));
		assertFalse(solver.isSafe(b3));
		assertTrue(solver.isSafe(c3));
		assertFalse(solver.isSafe(d3));
	}
	
	@Test
	void testNextLegalMoves() {
		solver.makeMove(row1Move);
		solver.makeMove(row2Move);
		List<GameMove> nextMoves = solver.nextLegalMoves();
		assertTrue(nextMoves.size() == 3);
		GameMove aMove = nextMoves.get(2);
		assertTrue(aMove.getTo().getFile() == 'h');
		assertTrue(aMove.getTo().getRank() == 3);
		aMove = nextMoves.get(1);
		assertTrue(aMove.getTo().getFile() == 'c');
		assertTrue(aMove.getTo().getRank() == 3);
		aMove = nextMoves.get(0);
		assertTrue(aMove.getTo().getFile() == 'a');
		assertTrue(aMove.getTo().getRank() == 3);
	}
	
	@Test
	void testSolve() {
		assertTrue(solver.solve());
	}

}