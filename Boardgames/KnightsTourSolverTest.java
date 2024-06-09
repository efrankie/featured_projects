package gameobjects;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KnightsTourSolverTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSolve() {
		GameMove firstMove = new GameMove(new Knight(), null, new GameCoordinate('a', 1));
		GameBoard board = new GameBoard(6, Color.BLACK, Color.WHITE);
		KnightsTourSolver solver = new KnightsTourSolver(board, firstMove);
		assertTrue(solver.solve());
		assertTrue(true);
	}

}
