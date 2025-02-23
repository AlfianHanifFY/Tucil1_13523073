
import java.util.*;

import lib.*;

public class main {
    static Scanner inputScanner = new Scanner(System.in);

    public static void main() {
        System.out.print(
                ".___________    __________                     .__           __________                   _________      .__                     \n"
                        + //
                        "|   \\_____  \\   \\______   \\__ _________________|  |   ____   \\______   \\_______  ____    /   _____/ ____ |  |___  __ ___________ \n"
                        + //
                        "|   |/  / \\  \\   |     ___/  |  \\___   /\\___   /  | _/ __ \\   |     ___/\\_  __ \\/  _ \\   \\_____  \\ /  _ \\|  |\\  \\/ // __ \\_  __ \\\n"
                        + //
                        "|   /   \\_/.  \\  |    |   |  |  //    /  /    /|  |_\\  ___/   |    |     |  | \\(  <_> )  /        (  <_> )  |_\\   /\\  ___/|  | \\/\n"
                        + //
                        "|___\\_____\\ \\_/  |____|   |____//_____ \\/_____ \\____/\\___  >  |____|     |__|   \\____/  /_______  /\\____/|____/\\_/  \\___  >__|   \n"
                        + //
                        "           \\__>                       \\/      \\/         \\/                                     \\/                      \\/       \n");

        System.out.println();
        System.out.println("\u001B[34m[INFO]\u001B[0m" + " : Wilujeng Sumping...\n");

        app();
    }

    public static void app() {
        String fileName;
        int[] boardInfo;
        String boardType;
        Puzzle[] puzzleList;
        boolean run = true;
        boolean valid = true;

        while (run) {
            IO io;
            do {
                System.out.println("\n\u001B[34m[INFO]\u001B[0m" + " : Masukkan konfigurasi puzzle dan papan !");
                System.out.println("\u001B[33m[WARNING]\u001B[0m" + " : Pastikan Input memiliki .txt (XXX.txt)...");
                fileName = IO.readFileName();
                System.out.println();
                io = new IO(fileName);
                boardInfo = io.getBoardInfo();
                boardType = io.getBoardType();
                if (boardInfo[0] <= 0 || boardInfo[1] <= 0 || boardInfo[2] <= 0 || !boardType.equals("DEFAULT")) {
                    valid = false;
                } else {
                    valid = true;
                }

            } while (!valid);

            puzzleList = new Puzzle[boardInfo[2]];
            puzzleList = io.getPuzzle(boardInfo[2], boardInfo[0], boardInfo[1]);
            if (puzzleList.length != boardInfo[2]) {
                valid = false;
            } else {
                valid = true;
            }

            if (valid) {
                Board board = new Board(boardInfo[0], boardInfo[1], boardInfo[2], boardType);
                board.initiateMap();
                long[] stats;
                System.out.println();
                System.out.printf("\u001B[34m[INFO]\u001B[0m" + " : Informasi board...\n\n" +
                        "+---------------------+\n" +
                        "|                     |\n" +
                        "|  M       :  %s       |\n" +
                        "|  N       :  %s       |\n" +
                        "|  P       :  %s       |\n" +
                        "|  Tipe    :  %s |\n" +
                        "|                     |\n" +
                        "+---------------------+\n\n",
                        boardInfo[0], boardInfo[1],
                        boardInfo[2], boardType);
                System.out.printf("\u001B[34m[INFO]\u001B[0m" + " : Hasil Penyelesaian board...\n\n");
                stats = board.solve(puzzleList);

                boolean validOpt = false;
                System.out.printf("\n\n\u001B[34m[INFO]\u001B[0m" + " : Simpan hasil ? (y/n)\n");
                do {
                    System.out.print("\u001B[38;5;214m[INPUT]\u001B[0m" + " : ");
                    String option = inputScanner.nextLine();

                    if (option.equals("y")) {
                        System.out.printf("\n\n\u001B[34m[INFO]\u001B[0m"
                                + " : Simpan dalam bentuk ? (1/2/3)\n [1] .txt\n [2] .png\n [3] .txt dan .png\n");
                        do {
                            System.out.print("\u001B[38;5;214m[INPUT]\u001B[0m" + " : ");
                            option = inputScanner.nextLine();

                            if (option.equals("1")) {
                                io.saveBoardTxt(board, stats[0], stats[1], stats[2]);
                                validOpt = true;
                            } else if (option.equals("2")) {
                                io.saveBoardImage(board, stats[0], stats[1], stats[2]);
                                validOpt = true;
                            } else if (option.equals("3")) {
                                io.saveBoardTxt(board, stats[0], stats[1], stats[2]);
                                io.saveBoardImage(board, stats[0], stats[1], stats[2]);
                                validOpt = true;
                            } else {
                                validOpt = false;
                                System.out.println(
                                        "\u001B[33m[WARNING]\u001B[0m" + " : Pastikan Input benar ! (1/2/3)...");
                            }
                        } while (!validOpt);
                    } else if (option.equals("n")) {
                        validOpt = true;
                    } else {
                        validOpt = false;
                        System.out.println("\u001B[33m[WARNING]\u001B[0m" + " : Pastikan Input benar ! (y/n)...");
                    }
                } while (!validOpt);
                System.out.println("\n\u001B[34m[INFO]\u001B[0m" + " : Keluar program ? (y/n)");
                validOpt = false;
                do {
                    System.out.print("\u001B[38;5;214m[INPUT]\u001B[0m" + " : ");
                    String option = inputScanner.nextLine();
                    if (option.equals("y")) {
                        validOpt = true;
                        run = false;
                    } else if (option.equals("n")) {
                        validOpt = true;
                    } else {
                        validOpt = false;
                        System.out.println("\u001B[33m[WARNING]\u001B[0m" + " : Pastikan Input benar ! (y/n)...");
                    }
                } while (!validOpt);

            }

        }
        System.out.println("\n\u001B[34m[INFO]\u001B[0m" + " : dadah !");
    }

}