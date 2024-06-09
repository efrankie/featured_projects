/*
* Frankie Wilson
* CS 122
*/

package gameobjects;

public class GameBoard {

	private GameSquare[][] squares;

	public GameBoard(int size, Color darkColor, Color lightColor) {
		squares = new GameSquare[size][size];
		for (int rankIndex = 0; rankIndex < size; ++rankIndex) {
			for (int fileIndex = 0; fileIndex < size; ++fileIndex) {
				Color aColor;
				if ((rankIndex + fileIndex) % 2 == 0) {
					aColor = darkColor;
				} else {
					aColor = lightColor;
				}
				squares[fileIndex][rankIndex] = new GameSquare(aColor);
			}
		}

	}

	public int getSize() {
		return squares.length;
	}

	public GameSquare getSquare(GameCoordinate aCoord) {
		return squares[aCoord.fileIndex][aCoord.rankIndex];
	}

	public Color getColor(GameCoordinate aCoord) {
		return getSquare(aCoord).getColor();
	}

	public boolean isEmpty(GameCoordinate aCoord) {
		return getSquare(aCoord).isEmpty();
	}

	public GamePiece getPiece(GameCoordinate aCoord) {
		return getSquare(aCoord).getPiece();
	}

	public void setPiece(GameCoordinate aCoord, GamePiece piece) {
		getSquare(aCoord).setPiece(piece);
	}

	public void removePiece(GameCoordinate aCoord) {
		getSquare(aCoord).removePiece();
	}
}
