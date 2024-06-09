package gameobjects;

import java.util.ArrayList;
import java.util.List;

public class KnightsTourSolver extends PuzzleSolver {

	public KnightsTourSolver(GameBoard board, GameMove firstMove) {
		super(board);
		makeMove(firstMove);
	}
	
	int[] xOffsets = {2, 2, -2, -2, 1, -1, 1, -1}; 
	int[] yOffsets = {1, -1, 1, -1, 2, 2, -2, -2};
	
	public List<GameCoordinate> nextPossibleKnightCoordinates() {
		List<GameCoordinate> possibles = new ArrayList<GameCoordinate>();
		GameCoordinate currentLoc = moves.get(moves.size()-1).getTo();
		for(int i = 0; i < xOffsets.length; ++i) {
			int fileIndex = currentLoc.fileIndex + xOffsets[i];
			int rankIndex = currentLoc.rankIndex + yOffsets[i];
			if(fileIndex >= 0 && fileIndex < board.getSize() && 
					rankIndex >= 0 && rankIndex < board.getSize()) {
				possibles.add(new GameCoordinate(fileIndex, rankIndex));
			}
		}
		return possibles;
	}
	
	public boolean hasVisited(GameCoordinate aCoord) {
		for(GameMove eachMove : moves) {
			if(eachMove.getTo().equals(aCoord)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean puzzleIsSolved() {
		return moves.size() == board.getSize() * board.getSize();
	}
	
	@Override
	public List<GameMove> nextLegalMoves(){
		List<GameMove> nextMoves = new ArrayList<GameMove>();
		List<GameCoordinate> possibles = nextPossibleKnightCoordinates();
		GameMove lastMove = moves.get(moves.size()-1);
		for(GameCoordinate eachCoord : possibles) {
			if(!hasVisited(eachCoord)) {
				GameMove aMove = new GameMove(lastMove.getPiece(), lastMove.getTo(), eachCoord);
				nextMoves.add(aMove);
			}
		}
		return nextMoves;
	}
}
