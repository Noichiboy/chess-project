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

    /**
     * @return retourne true si le coup a été joué, false si il est impossible
     */
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

    /**
     * @return retourne le plateau
     */
    public ChessBoard getBoard(){
        return board;
    }

    /**
     * @return retourne si la partie est finie
     */
    public boolean isFinished(){
        return isFinished;
    }

    private void endTurn(){
        currentPlayer = (currentPlayer == PieceColor.White)
            ? PieceColor.Black
            : PieceColor.White;
    }

    // Fonction qui vérifie si une pièce peu empêcher l'échec pour une couleur donnée
    // Cela inclus si le Roi peut lui-même se sortir d'échec
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

                //Joue temporairement un coup pour voir si cela nous met en échec
                toTile.setPiece(piece);
                fromTile.empty();

                //si nous ne sommes plus en échec
                if (!isKingCheck(color)){
                    canPreventCheck = true;
                }

                //annule temporairement le coup
                toTile.setPiece(toPiece);
                fromTile.setPiece(piece);
                if(canPreventCheck){
                    System.out.printf("Coup annulé de :" + fromTile + ", à : " + toTile);
                    return canPreventCheck;
                }
            }
        }
        return canPreventCheck;
    }

    private boolean isColorCheckMate(PieceColor color){
        if(!isKingCheck(color)) return false;//Si pas échec pas mat
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
     * @param from point de départ du coup
     * @param to destination du coup
     * @param faisable Si le coup est faisable sans vérifier si le joueur est mis en échec
     * @return un booléen qui indique si le coup est valide ou non
     */
    private boolean isValidMove(Tuple from, Tuple to, boolean faisable){
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
            //si faisable et valide return true
            if(faisable) return true;

            // Joue temporairement un coup pour voir si cela nous met en échec
            toTile.setPiece(fromPiece);
            fromTile.empty();
            if (isKingCheck(currentPlayer)){//Vérifie si le move ne nous met pas nous même en échec
                toTile.setPiece(toPiece);
                fromTile.setPiece(fromPiece);
                return false;
            }

            //si mat, fin de jeu
            if (isColorCheckMate(ChessPiece.opponent(currentPlayer)))
                isFinished = true;

            //annule coup temporaire
            toTile.setPiece(toPiece);
            fromTile.setPiece(fromPiece);

            return true;
        }
        return false;
    }

    // Une fontion qui renvoie tout les coup possibles d'une pièce sans ses coups illégaux
    // NOTICE: Ne vérifie pas les contres échecs en évaluant la "légalité" des coups
    //         Vérifie juste si le coup est faisable et n'est pas géné par une autre de nos pièces
    // Retourne les Tuples représentant les cases sur lesquels les pièces peuvent bouger
    private Tuple[] validMovesForPiece(ChessPiece piece, Tuple currentLocation){
            return piece.hasRepeatableMoves()
                ? validMovesRepeatable(piece, currentLocation)
                : validMovesNonRepeatable(piece, currentLocation);
    }

    // Retourne les Tuples représentant les cases sur lesquels les pièces peuvent bouger
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

    // Vérifie si le déplacement d'un tuple à un autre est valide
    private boolean isValidMoveForPiece(Tuple from, Tuple to){
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        boolean repeatableMoves = fromPiece.hasRepeatableMoves();

        return repeatableMoves
            ? isValidMoveForPieceRepeatable(from, to)
            : isValidMoveForPieceNonRepeatable(from, to);
    }

    // Vérifie si un coup donnée pour une pièce sans coups répétables
    private boolean isValidMoveForPieceRepeatable(Tuple from, Tuple to) {
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        Move[] validMoves = fromPiece.getMoves();

        int xMove = to.X() - from.X();
        int yMove = to.Y() - from.Y();

        for(int i = 1; i <= 7; i++){
            for(Move move : validMoves) {

                //vérifie grossièrement si coup possible
                if (move.x * i == xMove && move.y * i == yMove) {

                    //si coup grossièrement valide - vérifie si le coup est valide jusqu'à
                    for (int j = 1; j <= i; j++){
                        Tile tile = board.getTileFromTuple(new Tuple(from.X() + move.x * j, from.Y() +move.y * j));
                        //si passe par une case occupée return false
                        if (j != i && !tile.isEmpty())
                            return false;

                        //si dernier coup et toTile est vide ou occupé par des pièces ennemies return true
                        if (j == i && (tile.isEmpty() || tile.getPiece().getColor() != currentPlayer))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    // Vérifie si le coup est valide pour une pièce avec des coups répétés.
    private boolean isValidMoveForPieceNonRepeatable(Tuple from, Tuple to){
        ChessPiece fromPiece = board.getTileFromTuple(from).getPiece();
        Move[] validMoves = fromPiece.getMoves();
        Tile toTile = board.getTileFromTuple(to);

        int xMove = to.X() - from.X();
        int yMove = to.Y() - from.Y();

        for (Move move : validMoves) {
            if (move.x == xMove && move.y == yMove) {
                if (move.onTakeOnly){//if move is only legal on take (pawns)
                    if (toTile.isEmpty()) return false;

                    ChessPiece toPiece = toTile.getPiece();
                    return fromPiece.getColor() != toPiece.getColor();//si couleur différente, déplacement valide

                    //s'occupe du premier move des pions seulement - ils ne doivent pas avoir déjà bougé
                } else if (move.firstMoveOnly) {
                    return toTile.isEmpty() && isFirstMoveForPawn(from, board);
                } else {
                    return toTile.isEmpty();
                }
            }
        }
        return false;
    }

    // Determine si le Pion a déjà bougé une fois.
    public boolean isFirstMoveForPawn(Tuple from, ChessBoard board){
        Tile tile = board.getTileFromTuple(from);
        if (tile.isEmpty() || tile.getPiece().getPieceType() != PieceType.Pion) {
            return false;
        } else {
            PieceColor color = tile.getPiece().getColor();
            return (color == PieceColor.White)
                ? from.Y() == 6
                : from.Y() == 1;
        }
    }
}
