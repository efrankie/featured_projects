/*
* Frankie Wilson
* CS 122
*/

package gameobjects;

public class GameSquare {
	private Color color;
	private GamePiece piece;

	public GameSquare(Color aColor) {
		color = aColor;
	}

	public Color getColor() {
		return color;
	}

	public boolean isEmpty() {
		return piece == null;
	}

	public void setPiece(GamePiece aPiece) {
		piece = aPiece;
	}

	public GamePiece getPiece() {
		return piece;
	}

	public void removePiece() {
		piece = null;
	}

}
