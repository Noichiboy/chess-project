package Chess;

import Chess.Pieces.*;

import java.util.ArrayList;

public class ChessBoard {
    private final Tile[][] board;

    public ChessBoard(){
        board = new Tile[8][8];
        initializeBoard();
        fillBoard();
    }


    public Tile[][] getBoardArray(){
        return board;
    }

    private void initializeBoard(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                if (j % 2 + i == 0) board[i][j] = new Tile(Tile.TileColor.Black);
                else board[i][j] = new Tile(Tile.TileColor.White);
            }
        }
    }

    //Vérifie si les Rois sont bien présents sur le plateau
    public Tuple getKingLocation(ChessPiece.PieceColor color){
        Tuple location = new Tuple(-1,-1);
        for (int x = 0; x <= 7; x++){
            for (int y = 0; y <= 7 ; y++){
                if (!board[y][x].isEmpty()) {
                    ChessPiece piece = board[y][x].getPiece();
                    if (piece.getColor() == color && piece instanceof Roi){
                       location = new Tuple(x, y);
                    }
                }
            }
        }
        return location;
    }

    public Tuple[] getAllPiecesLocationForColor(ChessPiece.PieceColor color){
        ArrayList<Tuple> locations = new ArrayList<>();
        for (int x = 0; x <= 7; x++){
            for (int y = 0; y <= 7; y++){
               if(!board[y][x].isEmpty() && board[y][x].getPiece().getColor() == color)
                   locations.add(new Tuple(x,y));
            }
        }
        return locations.toArray(new Tuple[0]);//alloue new array automatiquement
    }

    public Tile getTileFromTuple(Tuple tuple){
        return board[tuple.Y()][tuple.X()];
    }

    
    //Remplissage plateau début de partie
    private void fillBoard(){
        //pions
        for(int i = 0; i < 8; i++){
        board[1][i].setPiece(new Pion(ChessPiece.PieceColor.Black));
        board[6][i].setPiece(new Pion(ChessPiece.PieceColor.White));
        }

        //tours
        board[0][0].setPiece(new Tour(ChessPiece.PieceColor.Black));
        board[0][7].setPiece(new Tour(ChessPiece.PieceColor.Black));
        board[7][0].setPiece(new Tour(ChessPiece.PieceColor.White));
        board[7][7].setPiece(new Tour(ChessPiece.PieceColor.White));

        //cavaliers
        board[0][1].setPiece(new Cavalier(ChessPiece.PieceColor.Black));
        board[0][6].setPiece(new Cavalier(ChessPiece.PieceColor.Black));
        board[7][1].setPiece(new Cavalier(ChessPiece.PieceColor.White));
        board[7][6].setPiece(new Cavalier(ChessPiece.PieceColor.White));

        //fous
        board[0][2].setPiece(new Fou(ChessPiece.PieceColor.Black));
        board[0][5].setPiece(new Fou(ChessPiece.PieceColor.Black));
        board[7][2].setPiece(new Fou(ChessPiece.PieceColor.White));
        board[7][5].setPiece(new Fou(ChessPiece.PieceColor.White));

        //reines
        board[0][3].setPiece(new Queen(ChessPiece.PieceColor.Black));
        board[7][3].setPiece(new Queen(ChessPiece.PieceColor.White));

        //rois
        board[0][4].setPiece(new Roi(ChessPiece.PieceColor.Black));
        board[7][4].setPiece(new Roi(ChessPiece.PieceColor.White));
    }

    /*private void fillBoardLoaded(ChessBoard ){


    }*/
}
