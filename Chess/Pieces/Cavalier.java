package Chess.Pieces;

import Chess.ChessPiece;
import Chess.Move;

public class Cavalier extends ChessPiece {

	public Cavalier(ChessPiece.PieceColor color) {
		super(PieceType.Cavalier, color, validMoves(), false);
	}


	private static Move[] validMoves(){
		return new Move[]{	new Move(2, 1, false, false), new Move(1, 2, false, false),
	                    	new Move(2, -1, false, false), new Move(-1, 2, false, false),
	                        new Move(2, -1, false, false), new Move(-1, 2, false, false),
	                        new Move(-2, 1, false, false), new Move(1, -2, false, false),
	                        new Move(-2, -1, false, false), new Move(-1, -2, false, false),
	                        new Move(-2, -1, false, false), new Move(-1, -2, false, false)};
	}
}
