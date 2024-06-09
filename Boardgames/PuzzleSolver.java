/*
* Frankie Wilson
* CS 120
*/

package gameobjects;

import java.util.ArrayList;
import java.util.List;

public abstract class PuzzleSolver {

	protected GameBoard board;
	protected List<GameMove> moves;
	
	public PuzzleSolver(GameBoard aBoard) {
		board = aBoard;
		moves = new ArrayList<GameMove>();
	}
	
	public abstract boolean puzzleIsSolved();
	public abstract List<GameMove> nextLegalMoves();
	
	public List<GameMove> getMoves(){
		return moves;
	}
	
	public boolean solve() {
        if(puzzleIsSolved()){
            return true;
        }
        List<GameMove> nextMoves = nextLegalMoves();
        for(GameMove eachMove : nextMoves){
            makeMove(eachMove);
            if(solve()) return true;
            unmakeMove(eachMove);
        }
        return false;
	}
	
    public void makeMove(GameMove aMove){
        moves.add(aMove);
        aMove.makeMoveOn(board);
    }

    public void unmakeMove(GameMove aMove){
        aMove.unmakeMoveOn(board);
        moves.remove(moves.size()-1);
    }
}