
import java.util.*;

import lib.*;

public class main {
    static Scanner inputScanner = new Scanner(System.in);

    public static void main() {
        System.out.print("Welcome !\n");
        inputMenu();
    }

    public static void inputMenu() {
        String fileName;
        int[] boardInfo;
        String boardType;
        Puzzle[] puzzleList;

        System.out.print("Masukkan input berupa file .txt : ");
        fileName = IO.readFileName();
        IO io = new IO(fileName);
        boardInfo = io.getBoardInfo();
        boardType = io.getBoardType();

        Board board = new Board(boardInfo[0], boardInfo[1], boardInfo[2], boardType);
        board.initiateMap();
        System.out.printf("Board Info : \n M : %s \n N : %s \n P : %s\n", boardInfo[0], boardInfo[1], boardInfo[2]);
        System.out.printf(" Type : %s\n\n", boardType);

        if (boardType.equals("DEFAULT")) {
            System.out.println("Board Valid !");
        } else {
            System.out.println("Board Tidak Valid !");
        }

        puzzleList = new Puzzle[boardInfo[2]];
        puzzleList = io.getPuzzle(boardInfo[2], boardInfo[0], boardInfo[1]);
        System.out.println("sukse ambil puzzle");
        board.solve(puzzleList);
    }

}