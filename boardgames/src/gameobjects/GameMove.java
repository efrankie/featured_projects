/*
* Frankie Wilson
* CS 122
*/

package gameobjects;

import java.io.StringWriter;

public class GameMove {
    private GamePiece piece;
    private GameCoordinate from;
    private GameCoordinate to;

	public GameMove(GamePiece aPiece, GameCoordinate aFromCoord, GameCoordinate aToCoord) {
		piece = aPiece;
		from = aFromCoord;
		to = aToCoord;
	}
	
	public GamePiece getPiece() {
		return piece;
	}
	
	public GameCoordinate getTo() {
		return to;
	}
	
	public GameCoordinate getFrom() {
		return from;
	}
	
	public void makeMoveOn(GameBoard aBoard) {
		if(from != null) {
			aBoard.removePiece(from);
		}
		aBoard.setPiece(to, piece);
	}
	
	public void unmakeMoveOn(GameBoard aBoard) {
		aBoard.removePiece(to);
		if(from != null) {
			aBoard.setPiece(from, piece);
		}
	}
	
	@Override
	public String toString() {
		StringWriter writer = new StringWriter();
		writer.write(piece.toString());
		writer.write(' ');
		if(from != null) {
			writer.write(from.toString());
			writer.write(" -> ");
		}
		writer.write(to.toString());
		return writer.toString();
	}
}
