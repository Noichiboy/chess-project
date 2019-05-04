public class Grille{
    public static final int BOARD_SIZE = 8;

    public static String creerGrille(){
      String Grid = "";
      //char piece = '\u2654';
      for (int row = 0; row < BOARD_SIZE; row++)
      {
          Grid = Grid + "---------------------------------" + "\n";

          for (int column = 0; column < BOARD_SIZE; column++)
          {
              Grid = Grid + "| " + " " + " ";
          }
          Grid = Grid + "|" + "\n";           
    }
    Grid = Grid + "---------------------------------" + "\n";
    return Grid;
  }


  public static void main(String[] args) {
    char piece = '\u2654';
    System.out.println(piece);
    String Test = creerGrille();
    System.out.println(Test);
  }


}