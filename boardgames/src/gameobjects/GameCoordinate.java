/*
* Frankie Wilson
* CS 122
*/

package gameobjects;

public class GameCoordinate {
	public int rankIndex;
	public int fileIndex;

	public GameCoordinate(char aFile, int aRank) {
		rankIndex = aRank - 1;
		fileIndex = (int) aFile - (int) 'a';
	}

	public GameCoordinate(int aFileIndex, int aRankIndex) {
		rankIndex = aRankIndex;
		fileIndex = aFileIndex;
	}

	public int getFileIndex() {

		return fileIndex;
	}

	public int getRankIndex() {
		return rankIndex;
	}

	public char getFile() {
		return (char) ((int) 'a' + fileIndex);
	}

	public int getRank() {
		return rankIndex + 1;
	}
}
