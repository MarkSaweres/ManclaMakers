/**
 * CS151 Team Project Mancala
 * Instructor: Dr. Kim
 * Group 9: MancalaMakers
 * Members: Mark B Saweres, Thinh Vo, Steven Stansberry
 */
public class MancalaTest {

    /**
     * @param args This is the main program
     */
    public static void main(String[] args) {
        BoardModel mancalaBoard = new BoardModel();
        MancalaView view = new MancalaView(mancalaBoard);
        mancalaBoard.attach(view);
    }
}