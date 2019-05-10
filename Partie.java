package Chess;

import Chess.ChessPiece.*;
import java.util.ArrayList;

public class ChessGame {

    private final ChessBoard board;
    private boolean isFinished;
    private PieceColor currentPlayer;

    public ChessGame(){
        board = new ChessBoard();
        currentPlayer = PieceColor.White;
        isFinished = false;
    }

    
    public boolean playMove(Tuple from, Tuple to){
        if(isValidMove(from, to, false)) {
            Tile fromTile = board.getBoardArray()[from.Y()][from.X()];
            ChessPiece pieceToMove = fromTile.getPiece();

            Tile toTile = board.getBoardArray()[to.Y()][to.X()];
            toTile.setPiece(pieceToMove);

            fromTile.empty();
            endTurn();
            return true;
        } else {
            return false;
        }
    }

    
    public ChessBoard getBoard(){
        return board;
    }


    public boolean isFinished(){
        return isFinished;
    }

    private void endTurn(){
        currentPlayer = (currentPlayer == PieceColor.White)
            ? PieceColor.Black
            : PieceColor.White;
    }

    private boolean isCheckPreventable(PieceColor color){
        boolean canPreventCheck = false;
        Tuple[] locations = board.getAllPiecesLocationForColor(color);

        for(Tuple location : locations){
            Tile fromTile = board.getTileFromTuple(location);
            ChessPiece piece = fromTile.getPiece();
            Tuple[] possibleMoves = validMovesForPiece(piece, location);

            for(Tuple newLocation : possibleMoves){
                Tile toTile = board.getTileFromTuple(newLocation);
                ChessPiece toPiece = toTile.getPiece();

                
                toTile.setPiece(piece);
                fromTile.empty();

                if (!isKingCheck(color)){
                    canPreventCheck = true;
                }

                toTile.setPiece(toPiece);
                fromTile.setPiece(piece);
                if(canPreventCheck){
                    System.out.printf("Prevented with from:" + fromTile + ", to: " + toTile);
                    return canPreventCheck;
                }
            }
        }
        return canPreventCheck;
    }

    private boolean isColorCheckMate(PieceColor color){
        if(!isKingCheck(color)) return false;
        return !isCheckPreventable(color);
    }

    private boolean isKingCheck(PieceColor kingColor){
        Tuple kingLocation = board.getKingLocation(kingColor);
        return canOpponentTakeLocation(kingLocation, kingColor);
    }

    private boolean canOpponentTakeLocation(Tuple location, PieceColor color){
        PieceColor opponentColor = ChessPiece.opponent(color);
        Tuple[] piecesLocation = board.getAllPiecesLocationForColor(opponentColor);

        for(Tuple fromTuple: piecesLocation) {
            if (isValidMove(fromTuple, location, true))
                return true;
        }
        return false;
    }

    /**
     * @param from the position from which the player tries to move from
     * @param to the position the player tries to move to
     * @param hypothetical if the move is hypothetical, we disregard if it sets the from player check
     * @return a boolean indicating whether the move is valid or not
     */
    private boolean isValidMove(Tuple from, Tuple to, boolean hypothetical){
        Tile fromTile = board.getTileFromTuple(from);
        Tile toTile = board.getTileFromTuple(to);
        ChessPiece fromPiece = fromTile.getPiece();
        ChessPiece toPiece = toTile.getPiece();

        if (fromPiece == null){
            return false;
        } else if (fromPiece.getColor() != currentPlayer) {
            return false;
        } else if (toPiece != null && toPiece.getColor() == currentPlayer) {
            return false;
        } else if (isValidMoveForPiece(from, to)){
            if(hypothetical) return true;

            toTile.setPiece(fromPiece);
            fromTile.empty();
            if (isKingCheck(currentPlayer)){
                toTile.setPiece(toPiece);
                fromTile.setPiece(fromPiece);
                return false;
            }

            if (isColorCheckMate(ChessPiece.opponent(currentPlayer)))
                isFinished = true;

            toTile.setPiece(toPiece);
            fromTile.setPiece(fromPiece);

            return true;
        }
        return false;
    }

    private Tuple[] validMovesForPiece(ChessPiece piece, Tuple currentLocation){
            return piece.hasRepeatableMoves()
                ? validMovesRepeatable(piece, currentLocation)
                : validMovesNonRepeatable(piece, currentLocation);
    }

    private Tuple[] validMovesRepeatable(ChessPiece piece, Tuple currentLocation) {
        Move[] moves = piece.getMoves();
        ArrayList<Tuple> possibleMoves = new ArrayList<>();

        for(Move move: moves){
            for(int i = 1; i < 7; i++){
                int newX = currentLocation.X() + move.x * i;
                int newY = currentLocation.Y() + move.y * i;
                if (newX < 0 || newX > 7 || newY < 0 || newY > 7) break;

                Tuple toLocation = new Tuple(newX, newY);
                Tile tile = board.getTileFromTuple(toLocation);
                if (tile.isEmpty()) {
                    possibleMoves.add(toLocation);
                } else {
                    if (tile.getPiece().getColor() != piece.getColor())
                        possibleMoves.add(toLocation);
                    break;
                }
            }
        }
        return possibleMoves.toArray(new Tuple[0]);
    }

    private Tuple[] validMovesNonRepeatable(ChessPiece piece, Tuple currentLocation) {
        Move[] moves = piece.getMoves();
        ArrayList<Tuple> possibleMoves = new ArrayList<>();

        for(Move move: moves){
            int currentX = currentLocation.X();
            int currentY = currentLocation.Y();
            int newX = currentX + move.x;
            int newY = currentY + move.y;
            if (newX < 0 || newX > 7 || newY < 0 || newY > 7) continue;
            Tuple newLocation = new Tuple(newX,newY);
            if (isValidMoveForPiece(currentLocation, newLocation)) possibleMoves.add(newLocation);
        }
        return possibleMoves.toArray(new Tuple[0]);
    }

    private boolean isValidMoveForPiece(Tuple from, Tuple to){
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        boolean repeatableMoves = fromPiece.hasRepeatableMoves();

        return repeatableMoves
            ? isValidMoveForPieceRepeatable(from, to)
            : isValidMoveForPieceNonRepeatable(from, to);
    }


    private boolean isValidMoveForPieceRepeatable(Tuple from, Tuple to) {
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        Move[] validMoves = fromPiece.getMoves();

        int xMove = to.X() - from.X();
        int yMove = to.Y() - from.Y();

        for(int i = 1; i <= 7; i++){
            for(Move move : validMoves) {

                if (move.x * i == xMove && move.y * i == yMove) {

                    for (int j = 1; j <= i; j++){
                        Tile tile = board.getTileFromTuple(new Tuple(from.X() + move.x * j, from.Y() +move.y * j));
                       
                        if (j != i && !tile.isEmpty())
                            return false;

                        if (j == i && (tile.isEmpty() || tile.getPiece().getColor() != currentPlayer))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isValidMoveForPieceNonRepeatable(Tuple from, Tuple to){
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        Move[] validMoves = fromPiece.getMoves();
        Tile toTile = board.getTileFromTuple(to);

        int xMove = to.X() - from.X();
        int yMove = to.Y() - from.Y();

        for (Move move : validMoves) {
            if (move.x == xMove && move.y == yMove) {
                if (move.onTakeOnly){
                    if (toTile.isEmpty()) return false;

                    ChessPiece toPiece = toTile.getPiece();
                    return fromPiece.getColor() != toPiece.getColor();

                    
                } else if (move.firstMoveOnly) {
                    return toTile.isEmpty() && isFirstMoveForPawn(from, board);
                } else {
                    return toTile.isEmpty();
                }
            }
        }
        return false;
    }

    public boolean isFirstMoveForPawn(Tuple from, ChessBoard board){
        Tile tile = board.getTileFromTuple(from);
        if (tile.isEmpty() || tile.getPiece().getPieceType() != PieceType.Pawn) {
            return false;
        } else {
            PieceColor color = tile.getPiece().getColor();
            return (color == PieceColor.White)
                ? from.Y() == 6
                : from.Y() == 1;
        }
    }
}
