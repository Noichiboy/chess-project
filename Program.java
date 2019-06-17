import Chess.ChessBoard;
import Chess.ChessGame;
import Chess.Tile;
import Chess.Tuple;
import Chess.ChessPiece.PieceColor;
import Console.InputHandler;
import Console.BoardDisplay;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class Program {

    private static int loopy;

    public static void main(String args[]) throws Exception {
        InputHandler handler = new InputHandler();
        Scanner scanner = new Scanner(System.in);
        String[] save = new String[]{""};
        int loopy = 0;

        ChessGame game = new ChessGame();
        BoardDisplay.clearConsole();
        BoardDisplay.printBoard(game.getBoard());
        while (!game.isFinished()) {
            System.out.println("Saisir coup (ex: A2-A3) : ");
            String input = scanner.nextLine();

            if (!handler.isValid(input)) {
                System.out.println("Saisie invalide !");
                System.out.println("Exemple de saisie valide : A2-A3");
            } else {
                Tuple from = handler.getFrom(input);
                Tuple to = handler.getTo(input);
                save[save.length-1]=input;

                boolean movePlayed = game.playMove(from, to);
                if (!movePlayed)
                    System.out.println("Coup illegal petit tricheur !");
                else {
                    BoardDisplay.clearConsole();
                    BoardDisplay.printBoard(game.getBoard());
                    sauvegarder(save);
                    loopy++;
                }
            }
        }
        scanner.close();
        System.out.println("Partie terminée. Merci d'avoir joué.");
    }

    public static void sauvegarder(String[] sauvegarde) {
        try {
            FileWriter fw = new FileWriter("sauvegarde.txt",true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i=0; i<sauvegarde.length; i++) {
                bw.write(sauvegarde[i]+"\n");
            }
            bw.close();
            fw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void charger(String file) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            while (br.readLine()!=null) { 
                br.readLine();
            }

            for (String move : foolsMate) {
                Tuple from = handler.getFrom(move);
                Tuple to = handler.getTo(move);

                boolean movePlayed = game.playMove(from, to);
                if (!movePlayed)
                    fail("Should be legal move");
            }
            br.close();
            fr.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
