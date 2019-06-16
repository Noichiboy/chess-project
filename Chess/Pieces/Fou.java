package Chess.Pieces;

import Chess.ChessPiece;
import Chess.Move;

public class Fou extends ChessPiece {

	public Fou(PieceColor color) {
		super(PieceType.Fou, color, validMoves(), true);
	}


	private static Move[] validMoves(){
		return	new Move[]{	new Move(1, 1, false, false), new Move(1, -1, false, false),
	                        new Move(-1, 1, false, false), new Move(-1, -1, false, false)};
	}
}
