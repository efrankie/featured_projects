/*
* Frankie Wilson
* CS 120
*/

package gameobjects;

import java.util.ArrayList;
import java.util.List;

public class NQueensSolver extends PuzzleSolver {

	public NQueensSolver(GameBoard board) {
		super(board);
	}

	@Override
	public boolean puzzleIsSolved() {
		return moves.size() == board.getSize();
	}

	@Override
	public List<GameMove> nextLegalMoves() {
		List<GameMove> nextMoves = new ArrayList<GameMove>();
		for (int fileIndex = 0; fileIndex < board.getSize(); ++fileIndex) {
			GameCoordinate gameCoord = new GameCoordinate(fileIndex, currentRankIndex());
			if (isSafe(gameCoord)) {
				nextMoves.add(new GameMove(new Queen(), null, gameCoord));
			}
		}
		return nextMoves;
	}

	public boolean isThreatenedBy(GameCoordinate aPlace, GameCoordinate aQueenPos) {
		return aPlace.fileIndex == aQueenPos.fileIndex || aPlace.rankIndex == aQueenPos.rankIndex
				|| Math.abs(aPlace.fileIndex - aQueenPos.fileIndex) == Math.abs(aPlace.rankIndex - aQueenPos.rankIndex);
	}

	public int currentRankIndex() {
		return moves.size();
	}

	public boolean isSafe(GameCoordinate gameCoord) {
		for (GameMove eachMove : moves) {
			if (isThreatenedBy(gameCoord, eachMove.getTo())) {
				return false;
			}
		}
		return true;
	}
}
