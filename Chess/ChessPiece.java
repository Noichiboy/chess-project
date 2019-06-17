package Chess;

public abstract class ChessPiece {
    private final PieceType type;
    private final PieceColor color;
    private final Move[] moves;
    private final String name;
    private char charValue;
    private final boolean repeatableMoves;

    public ChessPiece(PieceType type, PieceColor color, Move[] moves, boolean repeatableMoves){
        this.type = type;
        this.color = color;
        this.moves = moves;
        this.repeatableMoves = repeatableMoves;
        name = type.name();
        charValue = type.name().trim().charAt(0);
    }
    //Reine appelée Queen pour la différencier du Roi
    public enum PieceType {
        Pion, Tour, Cavalier, Fou, Queen, Roi
    }

    public enum PieceColor {
        White, Black
    }
    public Move[] getMoves(){ return moves; }

    public String getName(){ return name; }

    public PieceColor getColor(){ return color; }

    public char getCharValue(){
        /*if (charValue == 'P'){
            if (color == PieceColor.White) {
                charValue = '\u2659';
            } else {
                charValue = '\u265F';
            }
        }
        else if (charValue == 'T'){
            if (color == PieceColor.White) {
                charValue = '\u2656';
            } else {
                charValue = '\u265C';
            }
        } else if (charValue == 'C') {
            if (color == PieceColor.White) {
                charValue = '\u2658';
            } else {
                charValue = '\u265E';
            }
        } else if (charValue == 'F') {
            if (color == PieceColor.White) {
                charValue = '\u2657';
            } else {
                charValue = '\u265D';
            }
        } else if (charValue == 'Q') {
            if (color == PieceColor.White) {
                charValue = '\u2655';
            } else {
                charValue = '\u265B';
            }
        } else if (charValue == 'R') {
            if (color == PieceColor.White) {
                charValue = '\u2654';
            } else {
                charValue = '\u265A';
            }
        }*/
        return charValue; }

    public boolean hasRepeatableMoves(){ return repeatableMoves; }

    public PieceType getPieceType() {return type; }

    public static PieceColor opponent(PieceColor color) {
        return (color == PieceColor.Black) ? PieceColor.White : PieceColor.Black;
    }

}
